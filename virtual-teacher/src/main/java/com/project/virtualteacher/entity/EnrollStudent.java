package com.project.virtualteacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "course_id")
    private int courseId;

    @ManyToOne()
    @JoinColumn(name = "status")
    private CourseStatus status;
}
