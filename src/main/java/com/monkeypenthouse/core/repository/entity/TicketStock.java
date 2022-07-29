package com.monkeypenthouse.core.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Table(name="ticket_stock")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TicketStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable=false)
    private Long ticketId;

    @Column(nullable=false)
    private Integer totalQuantity;

    @Column(nullable=false)
    private Integer purchasedQuantity;

    public void increasePurchasedQuantity(int quantity) {
        purchasedQuantity += quantity;
    }

    public void decreasePurchasedQuantity(int quantity) {
        purchasedQuantity -= quantity;
    }
}
