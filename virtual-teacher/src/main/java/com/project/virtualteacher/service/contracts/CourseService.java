package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.dto.CourseSearchCriteria;
import com.project.virtualteacher.dto.PaginationResult;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Teacher;
import com.project.virtualteacher.entity.User;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;

public interface CourseService {

    Course create(Course course, User loggedUser);

    Course update(int courseToUpdateId,Course updateCourse, User loggedUser);

    Course getCourseById(int courseId, User loggedUser);

    PaginationResult<Course> getCoursesByTitle(String title, User loggedUser, int page, int size);

    Set<Course> getAllPublic();

    Set<Course> getAll(User loggedUser,int page,int size);

    void delete(int courseId,User loggedUser);

    void enroll(int courseId, User loggedUser);

    PaginationResult<Course> getCoursesBySearchCriteria(CourseSearchCriteria searchCriteria, User loggedUser, int page, int size);
}
