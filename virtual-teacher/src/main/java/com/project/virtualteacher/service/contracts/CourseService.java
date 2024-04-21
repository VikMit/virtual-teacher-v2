package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.Course;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface CourseService {
     Course createCourse(Course course, Authentication loggedUser);
     Course getCourseById(int courseId,Authentication loggedUser);
     Course getCourseByTitle(String title,Authentication loggedUser);
}
