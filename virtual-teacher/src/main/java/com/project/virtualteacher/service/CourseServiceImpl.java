package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.CourseStatusDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.dto.CourseSearchCriteria;
import com.project.virtualteacher.dto.PaginationResult;
import com.project.virtualteacher.entity.*;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityExistException;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.exception_handling.exceptions.UnsupportedOperationException;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.contracts.CourseValidatorHelper;
import com.project.virtualteacher.utility.contracts.UserValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final com.project.virtualteacher.dao.contracts.CourseDao courseDao;
    private final UserDao userDao;
    private final UserValidatorHelper userValidator;
    private final CourseValidatorHelper courseValidator;
    private final CourseStatusDao courseStatus;

    public CourseServiceImpl(com.project.virtualteacher.dao.contracts.CourseDao courseDao, UserDao userDao, UserValidatorHelper userValidator, CourseValidatorHelper courseValidator, CourseStatusDao courseStatus) {
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.courseValidator = courseValidator;
        this.courseStatus = courseStatus;
    }

    @Override
    @Transactional
    public Course create(Course course, User loggedUser) {
        userValidator.throwIfNotTeacher(loggedUser);
        if (!courseDao.isCourseTitleExist(course.getTitle())) {
            String username = loggedUser.getUsername();
            Teacher creator = (Teacher) userDao.findByUsename(username).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, username));
            course.setTeacher(creator);
            return courseDao.createCourse(course);
        }
        throw new EntityExistException(ErrorMessage.COURSE_TITLE_EXIST, course.getTitle());
    }

    @Override
    public Course getCourseById(int courseId, User loggedUser) {
        Course course = courseDao.getCourseById(courseId)
                .orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));
        if (userValidator.isTeacherOrAdmin(loggedUser) || userValidator.isUserEnrolledForCourse(loggedUser, course)) {
            return course;
        }
        throw new UnAuthorizeException(ErrorMessage.USER_NOT_AUTHORIZED, loggedUser.getUsername());
    }

    @Override
    public PaginationResult<Course> getCoursesByTitle(String title, User loggedUser, int page, int size) {

        if (userValidator.isTeacherOrAdmin(loggedUser)) {
            PaginationResult<Course> courses = courseDao.getCoursesByTitle(title, page, size);
            return courses;
        }
        if (userValidator.isStudent(loggedUser)) {
            return courseDao.getEnrolledCoursesByTitle(title, page, size, (Student) loggedUser);
        }
        throw new UnAuthorizeException(ErrorMessage.USER_NOT_AUTHORIZED, loggedUser.getUsername());
    }


    @Override
    public Set<Course> getAllPublic() {
        return courseDao.getAllPublic();
    }

    @Override
    public Set<Course> getAll(User loggedUser, int page, int size) {
        Set<Course> allCourses = courseDao.getAll(page, size);
        if (userValidator.isTeacherOrAdmin(loggedUser)) {
            return allCourses;
        }
        if (userValidator.isStudent(loggedUser)) {
            User user = userDao.findByUsename(loggedUser.getUsername()).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND, loggedUser.getUsername()));
            return allCourses.stream().filter(Course::isPublished).filter(course -> course.getEnrolledStudents().contains(user)).collect(Collectors.toSet());
        }
        throw new UnAuthorizeException(ErrorMessage.USER_NOT_AUTHORIZED, loggedUser.getUsername());

    }

    @Override
    @Transactional
    public void delete(int courseId, User loggedUser) {
        Course courseToDelete = courseDao.getCourseById(courseId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));
        courseValidator.throwIfNotCourseCreator(courseToDelete, loggedUser);
        throwIfCourseHasEnrolledStudents(courseToDelete);
        deleteIfCreator(courseToDelete, loggedUser);
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
    public PaginationResult<Course> getCoursesBySearchCriteria(CourseSearchCriteria searchCriteria, User loggedUser, int page, int size) {
        if (userValidator.isTeacherOrAdmin(loggedUser)){
            return courseDao.getAllBySearchCriteria(searchCriteria,page,size);
        }
        /*if (validator.isStudent(loggedUser)){
            return courseDao.getEnrolledBySearchCriteria(searchCriteria,loggedUser,page,size);
        }*/
        throw new UnAuthorizeException(ErrorMessage.USER_NOT_AUTHORIZED, loggedUser.getUsername());
    }


    @Override
    @Transactional
    public Course update(int courseToUpdateId, Course newCourse, User loggedUser) {
        Course courseToUpdate = courseDao.getCourseById(courseToUpdateId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseToUpdateId));
        courseValidator.throwIfNotCourseCreator(courseToUpdate, loggedUser);
        throwIfCourseTitleExist(newCourse, courseToUpdate);
        applyChanges(courseToUpdate, newCourse);
        return courseDao.update(courseToUpdate);
    }


    private void throwIfCourseTitleExist(Course newCourse, Course courseToUpdate) {
        if (!courseToUpdate.getTitle().equals(newCourse.getTitle())) {
            if (courseDao.isCourseTitleExist(newCourse.getTitle())) {
                throw new EntityExistException(ErrorMessage.COURSE_TITLE_EXIST, newCourse.getTitle());
            }
        }
    }

    private void applyChanges(Course courseToUpdate, Course newCourse) {
        courseToUpdate.setTitle(newCourse.getTitle());
        courseToUpdate.setStartDate(newCourse.getStartDate());
        courseToUpdate.setPublished(newCourse.isPublished());
        courseToUpdate.setPassingGrade(newCourse.getPassingGrade());
        courseToUpdate.setDescription(newCourse.getDescription());
    }

    private Course getCourseIfEnrolled(Course course, User user) {
        if (course.getEnrolledStudents().contains(user)) {
            return course;
        }
        throw new UnAuthorizeException(ErrorMessage.USER_NOT_ENROLLED_FOR_COURSE, user.getUsername(), course.getTitle());
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

   /* private static PaginationResult<Course> extractPublicOrEnrolledCourses(User loggedUser, PaginationResult<Course> courses) {
        int removedRecords = 0;
        Iterator<Course> iterator = courses.getData().iterator();
        while (iterator.hasNext()) {
            Course next = iterator.next();
            if (!next.isPublished() && !next.getEnrolledStudents().contains(loggedUser)) {
                iterator.remove();
                removedRecords++;
            }
        }
        courses.setTotalRecords(courses.getTotalRecords()-removedRecords);
        courses.setLastPage(getLastPage(courses.getTotalRecords(),courses.getSize()));
        return courses;
    }
    private static int getLastPage(long totalRecords, int pageSize) {
        int lastPage = (int)totalRecords / pageSize;
        if (totalRecords % pageSize != 0) {
            lastPage++;
        }
        return lastPage;
    }*/
}
