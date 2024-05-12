package com.project.virtualteacher.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class LectureBaseDto {

    private Integer id;

    @Positive(message = "Course ID must be positive INTEGER")
    private int courseId;

    @NotNull(message = "Lecture title can not be NULL.")
    @Size(min = 10, max = 128, message = "Lecture title must be in range 10 and 128 characters")
    private String title;


    @NotNull(message = "Lecture description can not be NULL.")
    @Size(min = 10, max = 512, message = "Lecture description must be in range 10 and 512 characters")
    private String description;

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

}
