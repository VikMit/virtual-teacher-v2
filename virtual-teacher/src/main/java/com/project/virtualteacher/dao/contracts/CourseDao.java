package com.project.virtualteacher.dao.contracts;

import com.project.virtualteacher.entity.Course;

import java.util.Optional;

public interface CourseDao {
    Course createCourse(Course course);

    Optional<Course> getCourseByTitle(String title);

    Optional<Course> getCourseById(int id);

    boolean isCourseTitleExist(String title);
}
