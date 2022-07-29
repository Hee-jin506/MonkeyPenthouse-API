package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Purchase;
import com.monkeypenthouse.core.repository.entity.PurchaseTicketMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseTicketMappingRepository extends CrudRepository<PurchaseTicketMapping, Long>{

    @Query("SELECT pt FROM PurchaseTicketMapping pt" +
            " INNER JOIN pt.purchase p" +
            " WHERE p.orderId = :orderId" +
            " AND p.orderStatus = IN_PROGRESS")
    List<PurchaseTicketMapping> findAllByOrderId(@Param("orderId") String orderId);
}
