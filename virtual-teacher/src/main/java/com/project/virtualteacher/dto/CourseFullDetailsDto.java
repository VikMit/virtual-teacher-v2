package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseFullDetailsDto extends CourseBaseDetailsDto {

    private Set<Lecture> lectures;

    private Set<Topic> topics;

    private Set<UserOutDto> enrolledStudents;
}
