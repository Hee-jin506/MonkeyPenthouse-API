package com.monkeypenthouse.core.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name="purchase")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 관계 반정규화 (Amenity-Ticket-PurchaseTicketMapping-Purchase => Amenity-Purchase)
    @Column(name="amenity_id", nullable = false, unique = false)
    private Long amenityId;

    @Column(name="order_id", nullable=false, unique = true)
    private String orderId;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name="order_status", nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name="created_at", updatable=false, nullable=false)
    private LocalDateTime createdAt;

    @Column(name="payments_key", updatable = false)
    private String paymentsKey;

    @Enumerated(EnumType.STRING)
    @Column(name="cancel_reason")
    private CancelReason cancelReason;

    @OneToMany(mappedBy = "purchase")
    @ToString.Exclude
    private List<PurchaseTicketMapping> purchaseTicketMappingList;

    public Purchase(User user, Long amenityId, String orderId, String orderName, int amount, OrderStatus orderStatus) {
        this.user = user;
        this.amenityId = amenityId;
        this.orderId = orderId;
        this.orderName = orderName;
        this.amount = amount;
        this.orderStatus = orderStatus;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCancelReason(CancelReason cancelReason) { this.cancelReason = cancelReason; }
    public void setPaymentsKey(String paymentsKey) { this.paymentsKey = paymentsKey; }

}

