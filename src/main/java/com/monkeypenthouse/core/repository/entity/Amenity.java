package com.monkeypenthouse.core.repository.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name="amenity", indexes = @Index(name = "ix_amenity_status_deadline_date", columnList = "status,deadline_date"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=30, unique = true, nullable=false)
    private String title;

    @Column(nullable=false)
    private String address;

    @Column(name="deadline_date", nullable=false)
    private LocalDate deadlineDate;

    @Column(name="start_date", nullable=false)
    private LocalDate startDate;

    @CreatedDate
    @Column(name="created_at", updatable=false, nullable=false)
    private LocalDateTime createdAt;

    @Column(name="detail", length=50, nullable=false)
    private String detail;

    @Column(name="thumbnail_name", nullable = false)
    private String thumbnailName;

    // 0 : 추천 X
    // 1 : 추천 O
    @Column(nullable = false)
    private int recommended;

    @Column(name="min_person_num", nullable = false)
    private int minPersonNum;

    @Column(name="max_person_num", nullable = false)
    private int maxPersonNum;

    @Column(nullable = false)
    private AmenityStatus status;

    @OneToMany(mappedBy = "amenity")
    @ToString.Exclude
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "amenity")
    @ToString.Exclude
    private List<AmenityCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "amenity")
    @ToString.Exclude
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "amenity")
    @ToString.Exclude
    private List<Dibs> dibs = new ArrayList<>();

    public void changeStatus(AmenityStatus status) {
        this.status = status;
    }
}
