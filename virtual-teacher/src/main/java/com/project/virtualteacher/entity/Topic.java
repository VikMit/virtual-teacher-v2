package com.project.virtualteacher.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topic")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "topic")
    private String topic;

}
