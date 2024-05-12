package com.project.virtualteacher.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @Column(name = "role")
    private String value;
}

