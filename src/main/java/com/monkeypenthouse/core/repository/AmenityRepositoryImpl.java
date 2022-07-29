package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Amenity;
import com.monkeypenthouse.core.repository.dto.*;
import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import com.monkeypenthouse.core.repository.entity.OrderStatus;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.monkeypenthouse.core.repository.entity.QAmenity.*;
import static com.monkeypenthouse.core.repository.entity.QPurchaseTicketMapping.purchaseTicketMapping;
import static com.monkeypenthouse.core.repository.entity.QPurchase.*;
import static com.monkeypenthouse.core.repository.entity.QTicket.*;
import static com.monkeypenthouse.core.repository.entity.QAmenityCategory.*;
import static com.monkeypenthouse.core.repository.entity.QCategory.*;
import static com.monkeypenthouse.core.repository.entity.QDibs.*;
import static com.monkeypenthouse.core.repository.entity.QUser.*;


@RequiredArgsConstructor
public class AmenityRepositoryImpl implements AmenityRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CurrentPersonAndFundingPriceAndDibsOfAmenityDto> findcurrentPersonAndFundingPriceAndDibsOfAmenityById(Long id) {
        List<CurrentPersonAndFundingPriceAndDibsOfAmenityDto> list = queryFactory
                .from(amenity)
                .leftJoin(amenity.tickets, ticket)
                .leftJoin(ticket.purchaseTicketMappings, purchaseTicketMapping)
                .leftJoin(purchaseTicketMapping.purchase, purchase)
                .leftJoin(amenity.dibs, dibs)
                .where(
                        amenity.id.eq(id),
                        purchase.orderStatus.eq(OrderStatus.COMPLETED)
                )
                .groupBy(amenity.id)
                .select(
                        new QCurrentPersonAndFundingPriceAndDibsOfAmenityDto(
                                purchaseTicketMapping.quantity.sum().coalesce(0),
                                ticket.price.multiply(purchaseTicketMapping.quantity.coalesce(0)).sum(),
                                ExpressionUtils.as(
                                        JPAExpressions.select(dibs.count().coalesce(0L))
                                        .from(dibs)
                                        .where(dibs.amenity.id.eq(id)),
                                        "dibs"
                                )
                        )
                ).fetch();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Page<AmenitySimpleDto> findPageByRecommended(int recommended, Pageable pageable) {
        JPQLQuery<AmenitySimpleDto> query = getQueryForAmenitySimpleDTO()
                .where(amenity.recommended.eq(recommended));
        List<AmenitySimpleDto> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity).where(amenity.recommended.eq(recommended)).fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AmenitySimpleDto> findPage(Pageable pageable) {
        JPQLQuery<AmenitySimpleDto> query = getQueryForAmenitySimpleDTO();
        List<AmenitySimpleDto> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity).fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AmenitySimpleDto> findPageByCategory(Long categoryId, Pageable pageable) {
        JPQLQuery<AmenitySimpleDto> query = getQueryForAmenitySimpleDTO()
                .leftJoin(amenity.categories, amenityCategory)
                .leftJoin(amenityCategory.category, category)
                .where(category.id.eq(categoryId));
        List<AmenitySimpleDto> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity)
                .leftJoin(amenity.categories, amenityCategory)
                .leftJoin(amenityCategory.category, category)
                .where(category.id.eq(categoryId))
                .fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AmenitySimpleDto> findPageByDibsOfUser(Long userId, Pageable pageable) {
        JPQLQuery<AmenitySimpleDto> query = getQueryForAmenitySimpleDTO()
                .leftJoin(amenity.dibs, dibs)
                .leftJoin(dibs.user, user)
                .where(user.id.eq(userId))
                .orderBy(dibs.createdAt.desc());
        List<AmenitySimpleDto> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity)
                .leftJoin(amenity.dibs, dibs)
                .leftJoin(dibs.user, user)
                .where(user.id.eq(userId))
                .fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public List<TicketOfAmenityDto> getTicketsOfAmenity(Long amenityId) {
        return queryFactory
                .select(new QTicketOfAmenityDto(
                        ticket.id,
                        ticket.name,
                        ticket.detail,
                        ticket.capacity,
                        ticket.price,
                        purchaseTicketMapping.quantity.sum().coalesce(0)
                ))
                .from(ticket)
                .where(ticket.amenity.id.eq(amenityId))
                .rightJoin(ticket.amenity, amenity)
                .leftJoin(purchaseTicketMapping).on(ticket.id.eq(purchaseTicketMapping.ticket.id))
                .groupBy(ticket.id)
                .fetch();
    }

    @Override
    public List<AmenitySimpleDto> findAllById(List<Long> amenityIds) {
        List<AmenitySimpleDto> amenities = getQueryForAmenitySimpleDTO()
                .where(amenity.id.in(amenityIds))
                .fetch();
        return reOrderAccordingToIndex(amenities, amenityIds);
    }

    @Override
    public Page<AmenitySimpleDto> findPageByOrdered(Long userId, Pageable pageable) {
        JPQLQuery<AmenitySimpleDto> query = getQueryForAmenitySimpleDTO()
                .leftJoin(purchaseTicketMapping.purchase, purchase)
                .where(purchase.orderStatus.in(Arrays.asList(OrderStatus.COMPLETED, OrderStatus.RESERVED)))
                .where(purchase.user.id.eq(userId))
                .orderBy(purchase.createdAt.desc());
        List<AmenitySimpleDto> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity)
                .leftJoin(amenity.tickets, ticket)
                .leftJoin(ticket.purchaseTicketMappings, purchaseTicketMapping)
                .leftJoin(purchaseTicketMapping.purchase, purchase)
                .where(purchase.orderStatus.in(Arrays.asList(OrderStatus.COMPLETED, OrderStatus.RESERVED)))
                .where(purchase.user.id.eq(userId))
                .fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public int countTotalQuantity(Long amenityId) {
        return queryFactory
                .from(amenity)
                .leftJoin(amenity.tickets, ticket)
                .groupBy(amenity.id)
                .where(amenity.id.eq(amenityId))
                .select(ticket.capacity.sum().coalesce(0))
                .fetch().get(0);
    }

    @Override
    public int countPurchasedQuantity(Long amenityId) {
        return queryFactory
                .from(purchase)
                .leftJoin(purchase.purchaseTicketMappingList, purchaseTicketMapping)
                .where(
                        amenity.id.eq(amenityId),
                        purchase.orderStatus.eq(OrderStatus.COMPLETED)
                )
                .groupBy(purchase.amenityId)
                .select(purchaseTicketMapping.quantity.sum().coalesce(0))
                .fetch().get(0);
    }


    @Override
    public List<Amenity> findAllAmenitiesToBeClosed(LocalDate today) {
        return queryFactory
                .selectFrom(amenity)
                .where(amenity.status.eq(AmenityStatus.RECRUITING), amenity.deadlineDate.lt(today))
                .fetch();
    }

    @Override
    public List<Amenity> findAllAmenitiesToBeEnded(LocalDate today) {
        return queryFactory
                .selectFrom(amenity)
                .where(amenity.status.in(Arrays.asList(AmenityStatus.FIXED, AmenityStatus.CANCELLED)))
                .leftJoin(amenity.tickets, ticket)
                .groupBy(amenity)
                .having(ticket.eventDateTime.max().after(LocalDateTime.from(today)))
                .fetch();
    }


    private JPQLQuery<AmenitySimpleDto> getQueryForAmenitySimpleDTO() {
        return queryFactory.from(amenity)
                .leftJoin(amenity.tickets, ticket)
                .leftJoin(ticket.purchaseTicketMappings, purchaseTicketMapping)
                .groupBy(amenity.id)
                .select(new QAmenitySimpleDto(
                        amenity.id,
                        amenity.title,
                        amenity.minPersonNum,
                        amenity.maxPersonNum,
                        purchaseTicketMapping.quantity.sum().coalesce(0).as("currentPersonNum"),
                        amenity.thumbnailName,
                        amenity.address,
                        amenity.startDate,
                        amenity.status
                )
        );
    }

    private JPQLQuery<AmenitySimpleDto> applicatePageable(JPQLQuery query, Pageable pageable) {
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        pageable.getSort().stream().forEach(e -> {
            PathBuilder<Amenity> orderByExpression = new PathBuilder<>(Amenity.class, "amenity");
            query.orderBy(new OrderSpecifier(e.isAscending() ?
                    Order.ASC : Order.DESC, orderByExpression.get(e.getProperty())));
        });
        return query;
    }

    private static List<AmenitySimpleDto> reOrderAccordingToIndex(List<AmenitySimpleDto> itemList,
                                                                  List<Long> indexList) {
        HashMap<Long, AmenitySimpleDto> hashMap = new HashMap<>(itemList.size());
        itemList.forEach(item -> hashMap.put(item.getId(), item));

        ArrayList<AmenitySimpleDto> output = new ArrayList<>(itemList.size());
        for (Long index : indexList) {
            AmenitySimpleDto item = hashMap.get(index);
            if (item != null) output.add(hashMap.get(index));
        }

        return output;
    }
}
