package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.Topic;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CreatedCourses {

    private int id;

    private String title;

    private LocalDate startDate;

    private Boolean isPublished;

    private int passingGrade;

    private String description;

    private Set<Lecture> lectures;

    private Set<Topic> topics;
}
