package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Lecture;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseFullDetailsOutDto extends CourseBaseDetailsOutDto {

    private Set<Lecture> lectures;

    private Set<UserOutDto> enrolledStudents;
}
