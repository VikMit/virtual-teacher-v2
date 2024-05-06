package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Topic;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CourseBaseDetailsOutDto {

    private int id;

    private String title;

    private LocalDate startDate;

    private Boolean isPublished;

    private int passingGrade;

    private String description;

    private UserOutDto creator;

    private Set<Topic> topics;

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }
}
