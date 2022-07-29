package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.component.CacheManager;
import com.monkeypenthouse.core.component.ImageManager;
import com.monkeypenthouse.core.connect.CloudFrontManager;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.exception.CommonException;
import com.monkeypenthouse.core.repository.*;
import com.monkeypenthouse.core.repository.dto.AmenitySimpleDto;
import com.monkeypenthouse.core.repository.dto.CurrentPersonAndFundingPriceAndDibsOfAmenityDto;
import com.monkeypenthouse.core.repository.dto.TicketOfAmenityDto;
import com.monkeypenthouse.core.repository.dto.TicketOfOrderedDto;
import com.monkeypenthouse.core.repository.entity.*;
import com.monkeypenthouse.core.service.dto.amenity.*;
import com.monkeypenthouse.core.service.dto.purchase.PurchaseByAmenityAndUserReqS;
import com.monkeypenthouse.core.service.dto.purchase.PurchaseByAmenityAndUserResS;
import com.monkeypenthouse.core.service.dto.purchase.PurchaseRefundAllByAmenityReqS;
import com.monkeypenthouse.core.service.dto.ticket.TicketOfAmenityS;
import com.monkeypenthouse.core.service.dto.ticket.TicketOfOrderedS;
import com.monkeypenthouse.core.service.dto.ticket.TicketSaveReqS;
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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;
    private final PhotoRepository photoRepository;
    private final TicketRepository ticketRepository;
    private final TicketStockRepository ticketStockRepository;
    private final CategoryRepository categoryRepository;
    private final AmenityCategoryRepository amenityCategoryRepository;
    private final UserService userService;
    private final PurchaseService purchaseService;

    private final ImageManager imageManager;
    private final ModelMapper modelMapper;
    private final CloudFrontManager cloudFrontManager;
    private final CacheManager cacheManager;

    @Override
    @Transactional
    public void add(final AmenitySaveReqS params) throws Exception {
        // DB에 저장할 어메니티 객체
        Amenity amenity = Amenity.builder()
                .title(params.getTitle())
                .address(params.getAddress())
                .deadlineDate(params.getDeadlineDate())
                .detail(params.getDetail())
                .recommended(params.getRecommended())
                .minPersonNum(params.getMinPersonNum())
                .startDate(LocalDate.now())
                .maxPersonNum(0)
                .thumbnailName("")
                .status(AmenityStatus.RECRUITING)
                .build();

        Amenity savedAmenity = amenityRepository.save(amenity);

        List<Category> categories = new ArrayList<>();
        List<AmenityCategory> amenityCategories = new ArrayList<>();
        // 각 카테고리에 대하여
        for (String categoryName : params.getCategories()) {
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
        for (TicketSaveReqS ticketS : params.getTickets()) {
            Ticket ticket = modelMapper.map(ticketS, Ticket.class);
            if (startDate == null || startDate.isAfter(ticketS.getEventDateTime().toLocalDate())) {
                startDate = ticketS.getEventDateTime().toLocalDate();
            }
            maxPersonNum += ticket.getCapacity();
            ticket.setAmenity(savedAmenity);
            tickets.add(ticket);


        }

        ticketRepository.saveAll(tickets);
        List<TicketStock> ticketStocks = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketStocks.add(TicketStock
                    .builder()
                    .ticketId(ticket.getId())
                    .totalQuantity(ticket.getCapacity())
                    .purchasedQuantity(0)
                    .build());
        }
        ticketStockRepository.saveAll(ticketStocks);
        // 어메니티 정보 업데이트
        amenity.setStartDate(startDate);
        amenity.setMaxPersonNum(maxPersonNum);

        List<Photo> photos = new ArrayList<>();
        // 배너 사진 리스트 저장
        for (int i = 0; i < params.getBannerPhotos().size(); i++) {
            String fileName = imageManager.uploadImageOnS3(params.getBannerPhotos().get(i), "banner");
            if (i == 0) {
                String thumbnailFileName = imageManager.uploadThumbnailOnS3(params.getBannerPhotos().get(i));
                amenity.setThumbnailName(thumbnailFileName);
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
        for (MultipartFile detailPhoto : params.getDetailPhotos()) {
            String fileName = imageManager.uploadImageOnS3(detailPhoto, "detail");
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
    public AmenityGetByIdResS getById(Long id) throws CloudFrontServiceException, IOException {
        Amenity amenity = amenityRepository.findWithPhotosById(id).orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
        CurrentPersonAndFundingPriceAndDibsOfAmenityDto currentPersonAndFundingPriceAndDibs = amenityRepository.findcurrentPersonAndFundingPriceAndDibsOfAmenityById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
        return amenityDetailDtoToVo(amenity, currentPersonAndFundingPriceAndDibs);
    }

    private AmenityGetByIdResS amenityDetailDtoToVo(Amenity amenity,
                                                    CurrentPersonAndFundingPriceAndDibsOfAmenityDto currentPersonAndFundingPriceAndDibs)
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

        return AmenityGetByIdResS.builder()
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
    public AmenityGetPagesResS getAmenitiesDibsOn(final UserDetails userDetails, Pageable pageable)
            throws CloudFrontServiceException, IOException {
        final User user = userService.getUserByEmail(userDetails.getUsername());
        return GetPageResS(amenityRepository.findPageByDibsOfUser(user.getId(), pageable));
    }

    @Override
    public AmenityGetPagesResS getPage(Pageable pageable) throws CloudFrontServiceException, IOException {
        return GetPageResS(amenityRepository.findPage(pageable));
    }

    @Override
    public AmenityGetPagesResS getPageByCategory(Long category, Pageable pageable) throws CloudFrontServiceException, IOException {
        return GetPageResS(amenityRepository.findPageByCategory(category, pageable));
    }

    @Override
    public AmenityGetPagesResS getPageByRecommended(Pageable pageable) throws CloudFrontServiceException, IOException {
        return GetPageResS(amenityRepository.findPageByRecommended(1, pageable));
    }

    @Override
    public AmenityTicketsByIdResS getTicketsOfAmenity(final Long amenityId) {

        final List<TicketOfAmenityDto> ticketsOfAmenity = amenityRepository.getTicketsOfAmenity(amenityId);

        return AmenityTicketsByIdResS.builder()
                .tickets(
                        ticketsOfAmenity
                                .stream()
                                .map(TicketOfAmenityDto -> TicketOfAmenityS.builder()
                                        .id(TicketOfAmenityDto.getId())
                                        .title(TicketOfAmenityDto.getTitle())
                                        .description(TicketOfAmenityDto.getDescription())
                                        .price(TicketOfAmenityDto.getPrice())
                                        .maxCount(TicketOfAmenityDto.getMaxCount())
                                        .availableCount(TicketOfAmenityDto.getMaxCount() - TicketOfAmenityDto.getReservedCount())
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    @Transactional
    public void updateStatusOfAmenity() {
        // 모집마감 날짜 < 오늘 && 어메니티 상태: RECRUITING
        // 주문수 >= 최소 인원 or 주문수 < 최소 인원? => FIXED or CANCELLED
        LocalDate today = LocalDate.now();
        amenityRepository.findAllAmenitiesToBeClosed(today).forEach(
            amenity -> {
                if (cacheManager.getPurchasedQuantityOfAmenity(amenity.getId()) >= amenity.getMinPersonNum()) {
                    amenity.changeStatus(AmenityStatus.FIXED);
                } else {
                    amenity.changeStatus(AmenityStatus.CANCELLED);
                }
            }
        );

        // 마지막 이벤트 일시 < 오늘 && 어메니티 상태: FIXED or CANCELLED => ENDED
        amenityRepository.findAllAmenitiesToBeEnded(today)
                .forEach(amenity -> amenity.changeStatus(AmenityStatus.ENDED));
    }

    @Override
    public AmenityGetViewedResS getViewed(List<Long> amenityIds) throws CloudFrontServiceException, IOException {
        List<AmenitySimpleDto> amenitySimpleDtos = amenityRepository.findAllById(
                amenityIds.size() > 5 ? amenityIds.subList(0, 5) : amenityIds);
        List<AmenitySimpleResS> amenitySimpleList = new ArrayList<>();
        for (AmenitySimpleDto dto : amenitySimpleDtos) {
            String signedUrl = cloudFrontManager.getSignedUrlWithCannedPolicy(dto.getThumbnailName());
            amenitySimpleList.add(AmenitySimpleResS.builder()
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
        return AmenityGetViewedResS.builder()
                .amenities(amenitySimpleList)
                .build();
    }

    @Override
    public AmenityGetPagesResS getAmenitiesByOrdered(UserDetails userDetails, Pageable pageable)
            throws CloudFrontServiceException, IOException {
        final User user = userService.getUserByEmail(userDetails.getUsername());
        return GetPageResS(amenityRepository.findPageByOrdered(user.getId(), pageable));
    }

    @Override
    public AmenityPurchasesOfOrderedResS getPurchasesOfOrderedAmenity(UserDetails userDetails, Long amenityId)
            throws CloudFrontServiceException, IOException {

        // 회원 정보 조회
        final User user = userService.getUserByEmail(userDetails.getUsername());

        // 어메니티 정보 조회
        final Amenity amenity = amenityRepository.findWithPhotosById(amenityId)
                .orElseThrow(() -> new CommonException(ResponseCode.AMENITY_NOT_FOUND));

        final List<Photo> bannerPhotos = amenity.getPhotos()
                .stream().filter(e -> e.getType().equals(PhotoType.BANNER)).collect(Collectors.toList());
        final List<String> bannerUrls = new ArrayList<>();
        for (Photo bannerPhoto : bannerPhotos) {
            bannerUrls.add(cloudFrontManager.getSignedUrlWithCannedPolicy(bannerPhoto.getType().name().toLowerCase() + "/" + bannerPhoto.getName()));
        }

        final List<PurchaseByAmenityAndUserResS> purchases =
                purchaseService.getPurchaseByAmenityAndUser(new PurchaseByAmenityAndUserReqS(amenityId, user.getId()));
        return AmenityPurchasesOfOrderedResS
                .builder()
                .bannerImages(bannerUrls)
                .title(amenity.getTitle())
                .maxPersonNum(amenity.getMaxPersonNum())
                .currentPersonNum(cacheManager.getPurchasedQuantityOfAmenity(amenityId))
                .status(amenity.getStatus())
                .purchases(purchases)
                .build();
    }

    private AmenityGetPagesResS GetPageResS(Page<AmenitySimpleDto> pages) throws CloudFrontServiceException, IOException {
        List<AmenitySimpleResS> amenitySimpleList = new ArrayList<>();
        for (AmenitySimpleDto dto : pages.getContent()) {
            String filename = "thumbnail/" + dto.getThumbnailName();
            String signedUrl = cloudFrontManager.getSignedUrlWithCannedPolicy(filename);
            amenitySimpleList.add(AmenitySimpleResS.builder()
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
        return new AmenityGetPagesResS(pages, amenitySimpleList);
    }

    @PostConstruct
    @Transactional(readOnly = true)
    private void loadPurchaseDataOnRedis() {
        final List<Amenity> amenityList = amenityRepository.findAllWithTicketsUsingFetchJoin();

        for (Amenity amenity : amenityList) {
            Set<Long> ticketIds = amenity.getTickets().stream().map(t -> t.getId()).collect(Collectors.toSet());
            List<TicketStock> ticketStocks = ticketStockRepository.findAllByTicketIdIn(ticketIds);

            cacheManager.setTotalQuantityOfAmenity(amenity.getId(),
                    ticketStocks.stream().mapToInt(TicketStock::getTotalQuantity).sum());
            cacheManager.setPurchasedQuantityOfAmenity(amenity.getId(),
                    ticketStocks.stream().mapToInt(TicketStock::getPurchasedQuantity).sum());

            for (TicketStock ticketStock : ticketStocks) {
                cacheManager.setTotalQuantityOfTicket(ticketStock.getTicketId(),
                        ticketStock.getTotalQuantity());
                cacheManager.setPurchasedQuantityOfTicket(ticketStock.getTicketId(),
                        ticketStock.getPurchasedQuantity());
            }
        }
    }

    @Override
    @Scheduled(cron = "0 5 0 * * ?") // 매일 0시 5분에
    @Transactional
    public void handleClosingProcessOfAmenity(){
        amenityRepository.findAllByStatus(AmenityStatus.CANCELLED)
            .forEach(a -> {
                try {
                    purchaseService.refundAllPurchasesByAmenity(new PurchaseRefundAllByAmenityReqS(a.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
    }
}
