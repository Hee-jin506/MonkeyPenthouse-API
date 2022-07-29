package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.component.ImageManager;
import com.monkeypenthouse.core.connect.CloudFrontManager;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.repository.entity.Amenity;
import com.monkeypenthouse.core.repository.entity.Photo;
import com.monkeypenthouse.core.repository.entity.PhotoType;
import com.monkeypenthouse.core.exception.CommonException;
import com.monkeypenthouse.core.repository.AmenityRepository;
import com.monkeypenthouse.core.repository.PhotoRepository;
import com.monkeypenthouse.core.service.dto.photo.PhotoAddCarouselReqS;
import com.monkeypenthouse.core.service.dto.photo.PhotoCarouselDataS;
import com.monkeypenthouse.core.service.dto.photo.PhotoCarouselFileS;
import com.monkeypenthouse.core.service.dto.photo.PhotoGetCarouselsResS;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final ImageManager imageManager;
    private final CloudFrontManager cloudFrontManager;
    private final AmenityRepository amenityRepository;
    private final PhotoRepository photoRepository;
    private final RedisTemplate redisTemplate;


    @Override
    @Transactional
    public void addCarousels(final PhotoAddCarouselReqS params) throws IOException {
        List<Photo> photos = new ArrayList<>();
        for (PhotoCarouselFileS carousel : params.getCarousels()) {
            String fileName = imageManager.uploadImageOnS3(carousel.getFile(), "carousel");
            long amenityId = carousel.getAmenityId();
            Amenity amenity = null;
            if (amenityId != 0) {
                amenity = amenityRepository.findById(amenityId)
                        .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
            }
            photos.add(Photo
                    .builder()
                    .name(fileName)
                    .type(PhotoType.CAROUSEL)
                    .amenity(amenity)
                    .build());
        }
        photoRepository.saveAll(photos);
    }

    @Override
    @Transactional(readOnly = true)
    public PhotoGetCarouselsResS getCarousels() throws CloudFrontServiceException, IOException {
        ListOperations<String, String> listStringOperations = redisTemplate.opsForList();
        ListOperations<String, Long> listLongOperations = redisTemplate.opsForList();

        List<PhotoCarouselDataS> carousels = new ArrayList<>();

        Long size;
        List<String> photoFileNames;
        List<Long> photoAmenityIds;

        if ((size = listStringOperations.size("carouselNames")) > 0) {
            photoFileNames = listStringOperations.range("carouselNames", 0, size);
            photoAmenityIds = listLongOperations.range("carouselAmenityId", 0, size);
        } else {
            photoFileNames = new ArrayList<>();
            photoAmenityIds = new ArrayList<>();
            photoRepository.findAllByType(PhotoType.CAROUSEL)
                    .stream().forEach(e -> {
                        photoFileNames.add(e.getName());
                        photoAmenityIds.add(e.getAmenity() != null? e.getAmenity().getId() : -1);
                    });
            size = Long.valueOf(photoFileNames.size());
        }

        for (int i = 0; i < size; i++) {
            String filename = "carousel/" + photoFileNames.get(i);
            String url = cloudFrontManager.getSignedUrlWithCannedPolicy(filename);
            PhotoCarouselDataS carousel = PhotoCarouselDataS.builder()
                    .url(url)
                    .amenityId(photoAmenityIds.get(i))
                    .build();
            carousels.add(carousel);
        }
        return PhotoGetCarouselsResS.builder()
                .carousels(carousels)
                .build();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    @Transactional
    public void syncCarouselToRedis() {
        ListOperations<String, String> listStringOperations = redisTemplate.opsForList();
        ListOperations<String, Long> listLongOperations = redisTemplate.opsForList();
        photoRepository.findAllByType(PhotoType.CAROUSEL)
                .stream().forEach(e -> {
                            listStringOperations.rightPush("carouselNames", e.getName());
                            listLongOperations.rightPush("carouselAmenityId",
                                    e.getAmenity() != null? e.getAmenity().getId() : -1L);
                        }
                );

    }
}
