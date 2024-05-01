package com.project.virtualteacher.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "user_lecture_solution")
public class Solution {

    @EmbeddedId
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
    }
}
