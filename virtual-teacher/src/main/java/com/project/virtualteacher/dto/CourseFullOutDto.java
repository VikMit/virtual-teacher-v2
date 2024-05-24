package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Lecture;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseFullOutDto extends CourseBaseOutDto implements Comparable<CourseFullOutDto> {

    private Set<Lecture> lectures;

    private Set<UserOutDto> enrolledStudents;

    @Override
    public int compareTo(CourseFullOutDto o) {
        return this.getId() - o.getId();
    }
}
