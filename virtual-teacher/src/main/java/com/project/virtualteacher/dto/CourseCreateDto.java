package com.project.virtualteacher.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CourseCreateDto {

    private int id;

    @NotNull(message = "Title can not be NULL.")
    @Size(min = 10, max = 128, message = "Title must be between 10 and 128 characters.")
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Start date can not be null, date format: 'yyyy-MM-dd'.")
    private LocalDate startDate;

    @NotNull(message = "Course status must be set 'TRUE' or 'FALSE'.")
    private Boolean isPublished;

    @Min(value = 2, message = "Passing grade must be greater or equal than 2.")
    @Max(value = 6, message = "Passing grade must be lower or equal than 6.")
    private int passingGrade;

    @NotNull(message = "Description can not be NULL.")
    @Size(min = 10, max = 1024, message = "Description must have min: 10 and max: 1024 characters.")
    private String description;

    private UserOutDto creator;

    private Set<Integer> topics;


    public void setTitle(String title) {
        this.title = title.trim();
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }



}
