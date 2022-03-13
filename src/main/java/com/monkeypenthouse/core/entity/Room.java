package com.monkeypenthouse.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Builder
@Table(name="room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Room {

    @Id
    @Column(length=8, updatable=false)
    private String id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_role", nullable = false, updatable = false)
    private Authority authority;

    @Column(name = "user_id", unique = true)
    private Long userId;
}
