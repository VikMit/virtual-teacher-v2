package com.project.virtualteacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LectureDto {

    private int id;

    @NotBlank(message = "Lecture title can not be NULL and can not contains only white spaces")
    @Size(min = 10, max = 128, message = "Lecture title must be in range 10 and 128 characters")
    private String title;

    private String videoUrl;

    private String assignmentUrl;

    @NotBlank(message = "Lecture description can not be NULL and can not contains only white spaces")
    @Size(min = 10, max = 512, message = "Lecture description must be in range 10 and 512 characters")
    private String description;

    @Positive(message = "Course ID must be positive INTEGER")
    private int courseId;

}
