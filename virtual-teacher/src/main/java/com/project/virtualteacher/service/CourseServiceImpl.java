package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.*;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.Mapper;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;
    private final UserDao userDao;
    private final ValidatorHelper validator;
    private final Mapper mapper;

    public CourseServiceImpl(CourseDao courseDao, UserDao userDao, ValidatorHelper validator, Mapper mapper) {
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Course create(Course course, Authentication loggedUser) {
        if (!courseDao.isCourseTitleExist(course.getTitle())) {
            String username = loggedUser.getName();
            User creator = userDao.findByUsename(username).orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
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

        if (validator.isTeacherOrAdmin(loggedUser)) {
            return course;
        } else {
            String username = loggedUser.getName();
            User userFromDB = userDao.findByUsename(username).orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
            return getCourseIfEnrolled(course, userFromDB);
        }
    }

    @Override
    public Course getPublicCourseById(int courseId) {
        return courseDao.getPublicCourseById(courseId).orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));
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

    @Override
    public Course getPublicCourseByTitle(String title) {
        return courseDao.getPublicCourseByTitle(title).orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND, title));
    }

    @Override
    public Set<Course> getAllPublic() {
        return courseDao.getAllPublic();
    }

    @Override
    public Set<Course> getAll(Authentication loggedUser) {
        Set<Course> allCourses = courseDao.getAll();
        if (validator.isTeacherOrAdmin(loggedUser)) {
            return allCourses;
        }
        if (validator.isStudent(loggedUser)) {
            User user = userDao.findByUsename(loggedUser.getName()).orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, loggedUser.getName()));
            return allCourses.stream().filter(Course::isPublished).filter(course -> course.getEnrolledStudents().contains(user)).collect(Collectors.toSet());
        } else {
            return allCourses.stream().filter(Course::isPublished).collect(Collectors.toSet());
        }
    }

    @Override
    @Transactional
    public void delete(int courseId, Authentication loggedUser) {
        String username = loggedUser.getName();
        Course courseToDelete = courseDao.getCourseById(courseId).orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));
        User userFromDB = userDao.findByUsename(username).orElseThrow(()->new UserNotFoundException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
        isEnrolledStudents(courseToDelete);
        deleteIfCreator(courseToDelete,userFromDB);
    }

    private void isEnrolledStudents(Course courseToDelete) {
        if (!courseToDelete.getEnrolledStudents().isEmpty()){
            throw new UnsupportedDeleteCourseException(ErrorMessage.COURSE_DELETE_WITH_ENROLLED_NOT_SUPPORTED);
        }
    }

    @Override
    @Transactional
    public Course update(int courseToUpdateId, Course updateCourse, Authentication loggedUser) {
        Course courseToUpdate = courseDao.getCourseById(courseToUpdateId).orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseToUpdateId));
        validator.isCreatorOfCourse(courseToUpdate, loggedUser);
        validateTitleUniqueIfChanged(updateCourse, courseToUpdate);
        applyChanges(courseToUpdate, updateCourse);
        return courseDao.update(courseToUpdate);
    }

   /* @Override
    public CourseBaseDetailsDto getCourseBasicDetailsById(int courseId) {
        Course courseFullDetails = courseDao.getCourseById(courseId).orElseThrow(()->new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND,courseId));
        return mapper.fromCourseToCourseBaseDetailsDto(courseFullDetails);
    }

    @Override
    public CourseBaseDetailsDto getCourseBasicDetailsByTitle(String title) {
        Course courseFullDetails = courseDao.getCourseByTitle(title).orElseThrow(()->new CourseNotFoundException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND,title));
        return mapper.fromCourseToCourseBaseDetailsDto(courseFullDetails);    }*/

    private void validateTitleUniqueIfChanged(Course updateCourse, Course courseToUpdate) {
        if (!courseToUpdate.getTitle().equals(updateCourse.getTitle())) {
            if ((courseDao.getCourseByTitle(updateCourse.getTitle()).isPresent())) {
                throw new CourseTitleExistException(ErrorMessage.COURSE_TITLE_EXIST, updateCourse.getTitle());
            }
        }
    }

    private void applyChanges(Course mainCourse, Course updateDetails) {
        mainCourse.setTitle(updateDetails.getTitle());
        mainCourse.setStartDate(updateDetails.getStartDate());
        mainCourse.setPublished(updateDetails.isPublished());
        mainCourse.setPassingGrade(updateDetails.getPassingGrade());
        mainCourse.setDescription(updateDetails.getDescription());
    }

    private Course getCourseIfEnrolled(Course course, User user) {
        if (course.getEnrolledStudents().contains(user)) {
            return course;
        }
        throw new UnAuthorizeException(ErrorMessage.USER_NOT_ENROLLED, user.getUsername(), course.getTitle());
    }
    private void deleteIfCreator(Course course, User user){
        if (course.getTeacher().equals(user)){
            courseDao.delete(course);
        }else{
            throw new UnAuthorizeException(ErrorMessage.NOT_COURSE_CREATOR_ERROR);
        }

    }
}
