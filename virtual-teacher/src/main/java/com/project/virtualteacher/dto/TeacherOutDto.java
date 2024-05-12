package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TeacherOutDto extends UserOutDto {

    private Set<CreatedCourses> createdCourses = new HashSet<>();

    public TeacherOutDto(Teacher teacher) {
        super.setUsername(teacher.getUsername());
        super.setFirstName(teacher.getFirstName());
        super.setLastName(teacher.getLastName());
        super.setEmail(teacher.getEmail());
        super.setRole(teacher.getRole());
        super.setUserId(teacher.getId());
        super.setPictureUrl(teacher.getPictureUrl());
        for (Course c : teacher.getCreatedCourses()) {
            this.createdCourses.add(fromCourseToCreatedCourse(c));
        }
    }


private CreatedCourses fromCourseToCreatedCourse(Course course){
        CreatedCourses createdCourses = new CreatedCourses();
        createdCourses.setId(course.getId());
        createdCourses.setTitle(course.getTitle());
        createdCourses.setDescription(course.getDescription());
        createdCourses.setPassingGrade(course.getPassingGrade());
        createdCourses.setTopics(course.getTopics());
        createdCourses.setIsPublished(course.isPublished());
        createdCourses.setStartDate(course.getStartDate());
        createdCourses.setLectures(course.getLectures());
        return createdCourses;
}

}
