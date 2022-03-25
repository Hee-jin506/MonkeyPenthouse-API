package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.connect.CloudFrontManager;
import com.monkeypenthouse.core.connect.S3Uploader;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.dto.querydsl.AmenitySimpleDTO;
import com.monkeypenthouse.core.dto.querydsl.CurrentPersonAndFundingPriceAndDibsOfAmenityDTO;
import com.monkeypenthouse.core.dto.querydsl.TicketOfAmenityDto;
import com.monkeypenthouse.core.entity.*;
import com.monkeypenthouse.core.dto.AmenityDTO.*;
import com.monkeypenthouse.core.dto.TicketDTO;
import com.monkeypenthouse.core.exception.CommonException;
import com.monkeypenthouse.core.repository.*;
import com.monkeypenthouse.core.vo.*;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;
    private final PhotoRepository photoRepository;
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final AmenityCategoryRepository amenityCategoryRepository;
    private final DibsRepository dibsRepository;
    private final UserService userService;
    private final S3Uploader s3Uploader;
    private final ModelMapper modelMapper;
    private final CloudFrontManager cloudFrontManager;

    @Override
    @Transactional
    public void add(List<MultipartFile> bannerPhotos, List<MultipartFile> detailPhotos, SaveReqDTO amenityDTO) throws Exception {
        // DB에 저장할 어메니티 객체
        Amenity amenity = modelMapper.map(amenityDTO, Amenity.class);
        amenity.setStartDate(LocalDate.now());
        amenity.setMaxPersonNum(0);
        amenity.setThumbnailName("");
        Amenity savedAmenity = amenityRepository.save(amenity);

        List<Category> categories = new ArrayList<>();
        List<AmenityCategory> amenityCategories = new ArrayList<>();
        // 각 카테고리에 대하여
        for (String categoryName : amenityDTO.getCategories()) {
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
            if (optionalCategory.isPresent()) {
                // 존재하면 가져옴
                categories.add(optionalCategory.get());
            } else {
                // 없으면 저장 후 가져옴
                Category category = Category
                        .builder()
                        .name(categoryName)
                        .build();
                categories.add(categoryRepository.save(category));
            }
        }
        // amenityCategory 저장
        for (Category category : categories) {
            AmenityCategory amenityCategory = AmenityCategory
                    .builder()
                    .category(category)
                    .amenity(savedAmenity)
                    .build();
            amenityCategories.add(amenityCategory);
        }
        amenityCategoryRepository.saveAll(amenityCategories);

        // 티켓 저장
        List<Ticket> tickets = new ArrayList<>();
        LocalDate startDate = null;
        int maxPersonNum = 0;
        for (TicketDTO.saveDTO ticketDTO : amenityDTO.getTickets()) {
            Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
            if (startDate ==  null || startDate.isAfter(ticketDTO.getEventDateTime().toLocalDate())) {
                startDate = ticketDTO.getEventDateTime().toLocalDate();
            }
            maxPersonNum += ticket.getCapacity();
            ticket.setAmenity(savedAmenity);
            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
        // 어메니티 정보 업데이트
        amenity.setStartDate(startDate);
        amenity.setMaxPersonNum(maxPersonNum);

        List<Photo> photos = new ArrayList<>();
       // 배너 사진 리스트 저장
        for (int i = 0; i < bannerPhotos.size(); i++) {
            String fileName = s3Uploader.upload(bannerPhotos.get(i), "banner");
            if (i == 0) {
                amenity.setThumbnailName("banner/" + fileName);
            }
            Photo photo = Photo
                    .builder()
                    .name(fileName)
                    .type(PhotoType.BANNER)
                    .amenity(savedAmenity)
                    .build();
            photos.add(photo);
        }

        // 상세 사진 리스트 저장
        for (MultipartFile detailPhoto : detailPhotos) {
            String fileName = s3Uploader.upload(detailPhoto, "detail");
            Photo photo = Photo
                    .builder()
                    .name(fileName)
                    .type(PhotoType.DETAIL)
                    .amenity(savedAmenity)
                    .build();
            photos.add(photo);
        }
        photoRepository.saveAll(photos);
    }

    @Override
    @Transactional(readOnly = true)
    public GetByIdResponseVo getById(Long id) throws CloudFrontServiceException, IOException {
        Amenity amenity = amenityRepository.findWithPhotosById(id).orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
        CurrentPersonAndFundingPriceAndDibsOfAmenityDTO currentPersonAndFundingPriceAndDibs = amenityRepository.findcurrentPersonAndFundingPriceAndDibsOfAmenityById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
        return amenityDetailDtoToVo(amenity, currentPersonAndFundingPriceAndDibs);
    }

    private GetByIdResponseVo amenityDetailDtoToVo(Amenity amenity,
                                                   CurrentPersonAndFundingPriceAndDibsOfAmenityDTO currentPersonAndFundingPriceAndDibs)
            throws CloudFrontServiceException, IOException {
        List<Photo> photos = amenity.getPhotos();
        List<String> bannerPhotos = new ArrayList<>();
        List<String> detailPhotos = new ArrayList<>();

        // 사진에 대한 signed url 만들기
        for (Photo photo : photos) {
            String filename = photo.getType().name().toLowerCase() + "/" + photo.getName();
            if (photo.getType() == PhotoType.BANNER) {
                bannerPhotos.add(cloudFrontManager.getSignedUrlWithCannedPolicy(filename));
            } else {
                detailPhotos.add(cloudFrontManager.getSignedUrlWithCannedPolicy(filename));
            }
        }

        return GetByIdResponseVo.builder()
                .id(amenity.getId())
                .title(amenity.getTitle())
                .detail(amenity.getDetail())
                .address(amenity.getAddress())
                .startDate(amenity.getStartDate())
                .deadlineDate(amenity.getDeadlineDate())
                .bannerImages(bannerPhotos)
                .detailImages(detailPhotos)
                .categories(amenity.getCategories().stream().map(e -> e.getCategory().getName()).collect(Collectors.toList()))
                .recommended(amenity.getRecommended())
                .minPersonNum(amenity.getMinPersonNum())
                .maxPersonNum(amenity.getMaxPersonNum())
                .currentPersonNum(currentPersonAndFundingPriceAndDibs.getCurrentPersonNum())
                .status(amenity.getStatus())
                .fundingPrice(currentPersonAndFundingPriceAndDibs.getFundingPrice())
                .dibs(currentPersonAndFundingPriceAndDibs.getDibs())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public GetPageResponseVo getAmenitiesDibsOn(final UserDetails userDetails, Pageable pageable) throws CloudFrontServiceException, IOException {
        final User user = userService.getUserByEmail(userDetails.getUsername());
        return amenitySimpleDtoToVo(amenityRepository.findPageByDibsOfUser(user.getId(), pageable));
    }

    @Override
    public GetPageResponseVo getPage(Pageable pageable) throws CloudFrontServiceException, IOException {
        Page<AmenitySimpleDTO> pages = amenityRepository.findPage(pageable);
        return amenitySimpleDtoToVo(pages);
    }

    @Override
    public GetPageResponseVo getPageByCategory(Long category, Pageable pageable) throws CloudFrontServiceException, IOException {
        Page<AmenitySimpleDTO> pages = amenityRepository.findPageByCategory(category, pageable);
        return amenitySimpleDtoToVo(pages);
    }

    @Override
    public GetPageResponseVo getPageByRecommended(Pageable pageable) throws CloudFrontServiceException, IOException {
        Page<AmenitySimpleDTO> pages = amenityRepository.findPageByRecommended(1, pageable);
        return amenitySimpleDtoToVo(pages);
    }

    @Override
    public GetTicketsOfAmenityResponseVo getTicketsOfAmenity(final Long amenityId) {

        final List<TicketOfAmenityDto> ticketsOfAmenity = amenityRepository.getTicketsOfAmenity(amenityId);

        return GetTicketsOfAmenityResponseVo.builder()
                .tickets(
                        ticketsOfAmenity
                                .stream()
                                .map(TicketOfAmenityDto -> TicketOfAmenityVo.builder()
                                        .id(TicketOfAmenityDto.getId())
                                        .title(TicketOfAmenityDto.getTitle())
                                        .description(TicketOfAmenityDto.getDescription())
                                        .price(TicketOfAmenityDto.getPrice())
                                        .maxCount(TicketOfAmenityDto.getMaxCount())
                                        .availableCount(TicketOfAmenityDto.getMaxCount()- TicketOfAmenityDto.getReservedCount())
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    @Transactional
    public void updateStatusOfAmenity() {
        LocalDate today = LocalDate.now();
        amenityRepository.updateStatusByDeadlineDate(today);
    }

    private GetPageResponseVo amenitySimpleDtoToVo(Page<AmenitySimpleDTO> pages) throws CloudFrontServiceException, IOException {
        List<AmenitySimpleVo> amenitySimpleVos = new ArrayList<>();
        for (AmenitySimpleDTO dto : pages.getContent()) {
            String signedUrl =  cloudFrontManager.getSignedUrlWithCannedPolicy(dto.getThumbnailName());
            amenitySimpleVos.add(AmenitySimpleVo.builder()
                    .id(dto.getId())
                    .title(dto.getTitle())
                    .minPersonNum(dto.getMinPersonNum())
                    .maxPersonNum(dto.getMaxPersonNum())
                    .currentPersonNum(dto.getCurrentPersonNum())
                    .thumbnailName(signedUrl)
                    .address(dto.getAddress())
                    .startDate(dto.getStartDate())
                    .status(dto.getStatus())
                    .build());
        }
        return new GetPageResponseVo(pages, amenitySimpleVos);
    }

}
