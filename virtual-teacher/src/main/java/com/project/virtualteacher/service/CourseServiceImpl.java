package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.dao.contracts.CourseStatusDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.CourseStatus;
import com.project.virtualteacher.entity.EnrollStudent;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityExistException;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.exception_handling.exceptions.UnsupportedOperationException;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.Mapper;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;
    private final UserDao userDao;
    private final ValidatorHelper validator;
    private final CourseStatusDao courseStatus;

    public CourseServiceImpl(CourseDao courseDao, UserDao userDao, ValidatorHelper validator, CourseStatusDao courseStatus) {
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.validator = validator;
        this.courseStatus = courseStatus;
    }

    @Override
    @Transactional
    public Course create(Course course, User loggedUser) {
        if (!courseDao.isCourseTitleExist(course.getTitle())) {
            String username = loggedUser.getUsername();
            User creator = userDao.findByUsename(username).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
            course.setTeacher(creator);
            return courseDao.createCourse(course);
        } else {
            throw new EntityExistException(ErrorMessage.COURSE_TITLE_EXIST, course.getTitle());
        }
    }

    @Override
    public Course getCourseById(int courseId, User loggedUser) {
        Course course = courseDao.getCourseById(courseId)
                .orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));

        if (validator.isTeacherOrAdmin(loggedUser)) {
            return course;
        } else {
            String username = loggedUser.getUsername();
            User userFromDB = userDao.findByUsename(username).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
            return getCourseIfEnrolled(course, userFromDB);
        }
    }

    @Override
    public Course getPublicCourseById(int courseId) {
        return courseDao.getPublicCourseById(courseId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));
    }

    @Override
    public Course getCourseByTitle(String title, User loggedUser) {
        Course course = courseDao.getCourseByTitle(title)
                .orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND, title));

        if (course.isPublished() || validator.isTeacherOrAdmin(loggedUser)) {
            return course;
        }

        throw new EntityNotExistException(ErrorMessage.PUBLIC_COURSE_WITH_TITLE_NOT_FOUND, title);
    }

    @Override
    public Course getPublicCourseByTitle(String title) {
        return courseDao.getPublicCourseByTitle(title).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND, title));
    }

    @Override
    public Set<Course> getAllPublic() {
        return courseDao.getAllPublic();
    }

    @Override
    public Set<Course> getAll(User loggedUser) {
        Set<Course> allCourses = courseDao.getAll();
        if (validator.isTeacherOrAdmin(loggedUser)) {
            return allCourses;
        }
        if (validator.isStudent(loggedUser)) {
            User user = userDao.findByUsename(loggedUser.getUsername()).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, loggedUser.getUsername()));
            return allCourses.stream().filter(Course::isPublished).filter(course -> course.getEnrolledStudents().contains(user)).collect(Collectors.toSet());
        } else {
            return allCourses.stream().filter(Course::isPublished).collect(Collectors.toSet());
        }
    }

    @Override
    @Transactional
    public void delete(int courseId, User loggedUser) {
        String username = loggedUser.getUsername();
        Course courseToDelete = courseDao.getCourseById(courseId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));
        User userFromDB = userDao.findByUsename(username).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
        throwIfCourseHasEnrolledStudents(courseToDelete);
        deleteIfCreator(courseToDelete, userFromDB);
    }

    @Override
    @Transactional
    public void enroll(int courseId, User loggedUser) {
        String username = loggedUser.getUsername();
        User student = userDao.findByUsename(loggedUser.getUsername()).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
        Course courseToEnroll = courseDao.getPublicCourseById(courseId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));
        throwIfUserEnrolled(student, courseToEnroll);
        enrollStudent(student, courseToEnroll);
    }


    @Override
    @Transactional
    public Course update(int courseToUpdateId, Course updateCourse, User loggedUser) {
        Course courseToUpdate = courseDao.getCourseById(courseToUpdateId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseToUpdateId));
        validator.isCreatorOfCourse(courseToUpdate, loggedUser);
        validateTitleUniqueIfChanged(updateCourse, courseToUpdate);
        applyChanges(courseToUpdate, updateCourse);
        return courseDao.update(courseToUpdate);
    }


    private void validateTitleUniqueIfChanged(Course updateCourse, Course courseToUpdate) {
        if (!courseToUpdate.getTitle().equals(updateCourse.getTitle())) {
            if ((courseDao.getCourseByTitle(updateCourse.getTitle()).isPresent())) {
                throw new EntityExistException(ErrorMessage.COURSE_TITLE_EXIST, updateCourse.getTitle());
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

    private void deleteIfCreator(Course course, User user) {
        if (course.getTeacher().equals(user)) {
            courseDao.delete(course);
        } else {
            throw new UnAuthorizeException(ErrorMessage.NOT_COURSE_CREATOR_ERROR);
        }
    }

    private void throwIfUserEnrolled(User user, Course course) {
        if (course.getEnrolledStudents().contains(user)) {
            throw new UnsupportedOperationException(ErrorMessage.USER_ENROLLED);
        }
    }

    private void enrollStudent(User student, Course courseToEnroll) {
        EnrollStudent enrollStudent = new EnrollStudent();
        enrollStudent.setCourseId(courseToEnroll.getId());
        enrollStudent.setUserId(student.getId());
        CourseStatus status = courseStatus.findCourseStatusByValue("Enrolled").orElseThrow(() -> new EntityNotExistException("Status does not Exist"));
        enrollStudent.setStatus(status);
        courseDao.enrollUserForCourse(enrollStudent);
    }

    private void throwIfCourseHasEnrolledStudents(Course courseToDelete) {
        if (!courseToDelete.getEnrolledStudents().isEmpty()) {
            throw new UnsupportedOperationException(ErrorMessage.COURSE_DELETE_WITH_ENROLLED_NOT_SUPPORTED);
        }
    }
}
