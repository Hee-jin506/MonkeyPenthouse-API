package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.dto.querydsl.*;
import com.monkeypenthouse.core.entity.Amenity;
import com.monkeypenthouse.core.entity.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import static com.monkeypenthouse.core.entity.QAmenity.*;
import static com.monkeypenthouse.core.entity.QOrderProduct.*;
import static com.monkeypenthouse.core.entity.QTicket.*;
import static com.monkeypenthouse.core.entity.QAmenityCategory.*;
import static com.monkeypenthouse.core.entity.QCategory.*;
import static com.monkeypenthouse.core.entity.QDibs.*;
import static com.monkeypenthouse.core.entity.QUser.*;


@RequiredArgsConstructor
public class AmenityRepositoryImpl implements AmenityRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CurrentPersonAndFundingPriceAndDibsOfAmenityDTO> findcurrentPersonAndFundingPriceAndDibsOfAmenityById(Long id) {
        List<CurrentPersonAndFundingPriceAndDibsOfAmenityDTO> list = queryFactory
                .from(amenity)
                .leftJoin(amenity.tickets, ticket)
                .leftJoin(ticket.orderProducts, orderProduct)
                .leftJoin(amenity.dibs, dibs)
                .where(amenity.id.eq(id))
                .groupBy(amenity.id)
                .select(
                        new QCurrentPersonAndFundingPriceAndDibsOfAmenityDTO(
                                orderProduct.quantity.sum().coalesce(0),
                                ticket.price.multiply(orderProduct.quantity.coalesce(0)).sum(),
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
    public Page<AmenitySimpleDTO> findPageByRecommended(int recommended, Pageable pageable) {
        JPQLQuery<AmenitySimpleDTO> query = getQueryForAmenitySimpleDTO()
                .where(amenity.recommended.eq(recommended));
        List<AmenitySimpleDTO> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity).where(amenity.recommended.eq(recommended)).fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AmenitySimpleDTO> findPage(Pageable pageable) {
        JPQLQuery<AmenitySimpleDTO> query = getQueryForAmenitySimpleDTO();
        List<AmenitySimpleDTO> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity).fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AmenitySimpleDTO> findPageByCategory(Long categoryId, Pageable pageable) {
        JPQLQuery<AmenitySimpleDTO> query = getQueryForAmenitySimpleDTO()
                .leftJoin(amenity.categories, amenityCategory)
                .leftJoin(amenityCategory.category, category)
                .where(category.id.eq(categoryId));
        List<AmenitySimpleDTO> content = applicatePageable(query, pageable).fetch();
        long totalCount = queryFactory.selectFrom(amenity)
                .leftJoin(amenity.categories, amenityCategory)
                .leftJoin(amenityCategory.category, category)
                .where(category.id.eq(categoryId))
                .fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AmenitySimpleDTO> findPageByDibsOfUser(Long userId, Pageable pageable) {
        JPQLQuery<AmenitySimpleDTO> query = getQueryForAmenitySimpleDTO()
                .leftJoin(amenity.dibs, dibs)
                .leftJoin(dibs.user, user)
                .where(user.id.eq(userId));
        List<AmenitySimpleDTO> content = applicatePageable(query, pageable).fetch();
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
                        orderProduct.quantity.sum().coalesce(0)
                ))
                .from(ticket)
                .where(ticket.amenity.id.eq(amenityId))
                .rightJoin(ticket.amenity, amenity)
                .leftJoin(orderProduct).on(ticket.id.eq(orderProduct.ticket.id))
                .groupBy(ticket.id)
                .fetch();
    }

    private JPQLQuery<AmenitySimpleDTO> getQueryForAmenitySimpleDTO() {
        return queryFactory.from(amenity)
                .leftJoin(amenity.tickets, ticket)
                .leftJoin(ticket.orderProducts, orderProduct)
                .groupBy(amenity.id)
                .select(new QAmenitySimpleDTO(
                        amenity.id,
                        amenity.title,
                        amenity.minPersonNum,
                        amenity.maxPersonNum,
                        orderProduct.quantity.sum().coalesce(0).as("currentPersonNum"),
                        amenity.thumbnailName,
                        amenity.address,
                        amenity.startDate,
                        amenity.status
                )
        );
    }

    private JPQLQuery<AmenitySimpleDTO> applicatePageable(JPQLQuery query, Pageable pageable) {
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        pageable.getSort().stream().forEach(e -> {
            PathBuilder<Amenity> orderByExpression = new PathBuilder<Amenity>(Amenity.class, "amenity");
            query.orderBy(new OrderSpecifier(e.isAscending() ?
                    Order.ASC : Order.DESC, orderByExpression.get(e.getProperty())));
        });
        return query;
    }
}
