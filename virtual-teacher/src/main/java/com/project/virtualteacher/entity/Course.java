package com.project.virtualteacher.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private LocalDate startDate;

    @OneToMany(mappedBy = "course")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
   // @JoinTable(name = "course_lecture", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "lecture_id"))
    private Set<Lecture> lectures;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_topic", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Set<Topic> topics;

    @ManyToOne()
    @JoinColumn(name = "creator_id")
    private User teacher;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "passing_grade")
    private int passingGrade;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> enrolledStudents = new HashSet<>();

}
