package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.CourseNotFoundException;
import com.project.virtualteacher.exception_handling.exceptions.CourseTitleExistException;
import com.project.virtualteacher.exception_handling.exceptions.UserNotFoundException;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;
    private final UserDao userDao;
    private final ValidatorHelper validator;

    public CourseServiceImpl(CourseDao courseDao, UserDao userDao, ValidatorHelper validator) {
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.validator = validator;
    }

    @Override
    @Transactional
    public Course createCourse(Course course, Authentication loggedUser) {
        if (!courseDao.isCourseTitleExist(course.getTitle())) {
            String username = loggedUser.getName();
            User creator = userDao.getByUsername(username).orElseThrow(() -> new UserNotFoundException(ErrorMessage.USERNAME_NOT_FOUND, username));
            course.setTeacher(creator);
            return courseDao.createCourse(course);
        } else {
            throw new CourseTitleExistException(ErrorMessage.COURSE_TITLE_EXIST, course.getTitle());
        }
    }

    @Override
    public Course getCourseById(int courseId, Authentication loggedUser) {
        Course course = courseDao.getCourseById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));

        if (course.isPublished() || validator.isTeacherOrAdmin(loggedUser)) {
            return course;
        }

        throw new CourseNotFoundException(ErrorMessage.PUBLIC_COURSE_WITH_ID_NOT_FOUND, courseId);
    }

    @Override
    public Course getCourseByTitle(String title, Authentication loggedUser) {
        Course course = courseDao.getCourseByTitle(title)
                .orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND, title));

        if (course.isPublished() || validator.isTeacherOrAdmin(loggedUser)) {
            return course;
        }

        throw new CourseNotFoundException(ErrorMessage.PUBLIC_COURSE_WITH_TITLE_NOT_FOUND, title);
    }

}
