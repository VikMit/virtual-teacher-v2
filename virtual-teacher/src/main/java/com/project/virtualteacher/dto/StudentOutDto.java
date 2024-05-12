package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentOutDto extends UserOutDto {
    private final Set<Course> enrolledCourses;

    public StudentOutDto(Student student) {
        super();
        super.setUsername(student.getUsername());
        super.setFirstName(student.getFirstName());
        super.setLastName(student.getLastName());
        super.setEmail(student.getEmail());
        super.setRole(student.getRole());
        super.setUserId(student.getId());
        super.setPictureUrl(student.getPictureUrl());
        this.enrolledCourses = student.getEnrolledCourses();
    }
}
