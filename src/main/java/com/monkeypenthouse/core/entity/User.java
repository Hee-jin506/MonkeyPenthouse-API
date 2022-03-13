package com.monkeypenthouse.core.entity;

import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=15, nullable=false)
    private String name;

    @CreatedDate
    @Column(name="created_at", updatable=false, nullable=false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name="last_modified_at")
    private LocalDateTime lastModifiedDateTime;

    @Column(nullable=false)
    private LocalDate birth;


    // 0 : 여자
    // 1: 남자
    @Column(nullable = false)
    private int gender;

    @Column(unique = true, length=50, nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(name="phone_num", unique = true, length=20, nullable=false)
    private String phoneNum;

    @Column(name="receive_info", nullable=false)
    private int infoReceivable;

    @OneToOne
    @JoinColumn(name="room_id", unique = true)
    private Room room;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="user_role", nullable = false)
    private Authority authority;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="login_type", nullable = false)
    private LoginType loginType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="life_style")
    private LifeStyle lifeStyle;
}
