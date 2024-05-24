package com.project.virtualteacher.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Course implements Comparable<Course> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private LocalDate startDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Lecture> lectures;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_topic", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Set<Topic> topics;

    @ManyToOne()
    @JoinColumn(name = "creator_id")
    private Teacher teacher;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "passing_grade")
    private int passingGrade;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    Set<Student> enrolledStudents;


    @Override
    public int compareTo(Course o) {
        return this.getId() - o.getId();
    }
}
