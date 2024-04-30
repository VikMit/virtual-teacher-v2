package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;

import java.util.Set;

public interface CourseService {
    Course create(Course course, User loggedUser);

    Course update(int courseToUpdateId,Course updateCourse, User loggedUser);

    Course getCourseById(int courseId, User loggedUser);

    Course getPublicCourseById(int courseId);

    Course getCourseByTitle(String title, User loggedUser);

    Course getPublicCourseByTitle(String title);

    Set<Course> getAllPublic();

    Set<Course> getAll(User loggedUser);

    void delete(int courseId,User loggedUser);

    void enroll(int courseId, User loggedUser);


    // CourseBaseDetailsDto getCourseBasicDetailsById(int courseId);

   // CourseBaseDetailsDto getCourseBasicDetailsByTitle(String title);
}
