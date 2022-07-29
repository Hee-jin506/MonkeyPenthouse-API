package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.OrderStatus;
import com.monkeypenthouse.core.repository.entity.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends CrudRepository<Purchase, Long>, PurchaseRepositoryCustom {

    Optional<Purchase> findTop1ByOrderByIdDesc();

    Optional<Purchase> findByOrderId(String orderId);

    List<Purchase> findAllByAmenityIdAndOrderStatus(Long amenityId, OrderStatus orderStatus);
}
