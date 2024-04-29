package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.Course;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CourseService {
    Course create(Course course, Authentication loggedUser);

    Course update(int courseToUpdateId,Course updateCourse, Authentication loggedUser);

    Course getCourseById(int courseId, Authentication loggedUser);

    Course getPublicCourseById(int courseId);

    Course getCourseByTitle(String title, Authentication loggedUser);

    Course getPublicCourseByTitle(String title);

    Set<Course> getAllPublic();

    Set<Course> getAll(Authentication loggedUser);

    void delete(int courseId,Authentication loggedUser);

    void enroll(int courseId, Authentication loggedUser);


    // CourseBaseDetailsDto getCourseBasicDetailsById(int courseId);

   // CourseBaseDetailsDto getCourseBasicDetailsByTitle(String title);
}
