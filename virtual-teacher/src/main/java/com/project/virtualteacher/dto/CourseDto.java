package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.Topic;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.checkerframework.common.value.qual.IntRange;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CourseDto {
    @NotBlank
    @Size(min = 10,max = 128)
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;


    private Set<Lecture> lectures;

    private Set<Topic> topics;

    @NotNull
    private boolean isPublished;

    @IntRange(from = 2,to = 6)
    @NotNull
    private double passingGrade;

    @NotBlank(message = "Description can not contain only white spaces")
    @Size(min = 10,max = 1024,message = "Description must have min: 10 and max: 1024 characters")
    private String description;

}
