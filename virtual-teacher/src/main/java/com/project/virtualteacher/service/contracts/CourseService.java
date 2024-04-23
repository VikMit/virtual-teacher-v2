package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.dto.CourseFullDetailsDto;
import com.project.virtualteacher.entity.Course;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CourseService {
    Course create(Course course, Authentication loggedUser);

    Course update(int courseToUpdateId,Course updateCourse, Authentication loggedUser);

    Course getCourseById(int courseId, Authentication loggedUser);

    Course getPublicCourseById(int courseId);

    CourseFullDetailsDto getCourseByTitle(String title, Authentication loggedUser);

    Course getPublicCourseByTitle(String title);

    Set<Course> getAllPublic();



    // CourseBaseDetailsDto getCourseBasicDetailsById(int courseId);

   // CourseBaseDetailsDto getCourseBasicDetailsByTitle(String title);
}