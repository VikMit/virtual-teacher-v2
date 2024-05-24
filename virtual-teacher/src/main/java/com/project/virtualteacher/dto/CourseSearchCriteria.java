package com.project.virtualteacher.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Data
public class CourseSearchCriteria {

    Optional<String> title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Optional<LocalDate> startDate;
    Set<String> topics;
    Optional<String> teacher;
    Optional<String> description;

}
