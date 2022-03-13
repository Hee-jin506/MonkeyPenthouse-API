package com.monkeypenthouse.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@IdClass(DibsId.class)
@Builder
@Table(name="dibs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dibs {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "amenity_id")
    private Amenity amenity;

}
