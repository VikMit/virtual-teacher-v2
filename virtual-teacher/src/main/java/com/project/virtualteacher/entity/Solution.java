package com.project.virtualteacher.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "solutions")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "solution_URL")
    private String solutionURL;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    @JsonIgnore
    private Lecture lecture;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User creator;

   /* @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User creator;*/



  /*  @EmbeddedId
    private UserSolutionId id;

    @OneToOne
    @JoinColumn(name = "lecture_id", insertable = false, updatable = false)
    @JsonIgnore
    private Lecture lecture;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "solution_url")
    private String solutionUrl;

    @Embeddable
    @Data
    private static final class UserSolutionId implements Serializable {
        @Column(name = "lecture_id")
        private int lectureId;

        @Column(name = "user_id")
        private int userId;
    }*/
}
