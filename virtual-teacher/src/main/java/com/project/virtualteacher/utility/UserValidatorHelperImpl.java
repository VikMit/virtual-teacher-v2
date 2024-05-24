package com.project.virtualteacher.utility;

import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.dto.UserCreateDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Student;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.exception_handling.exceptions.UserException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.project.virtualteacher.exception_handling.error_message.ErrorMessage.INCORRECT_CONFIRM_PASSWORD;

@Component
public class UserValidatorHelperImpl implements com.project.virtualteacher.utility.contracts.UserValidatorHelper {

    private final UserDao userDao;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    private static final String ROLE_TEACHER = "ROLE_TEACHER";

    public UserValidatorHelperImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public User extractUserFromAuthentication(Authentication loggedUser) {
        throwIfNoLoggedUser(loggedUser);
        String username = loggedUser.getName();
        return userDao.findByUsename(username).orElseThrow(() -> new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND));
    }

    public void throwIfNoLoggedUser(Authentication authentication){
        if (authentication==null){
            throw new UserException(ErrorMessage.USER_REQUIRED);
        }
    }

    public void throwIfLoggedUser(Authentication authentication){
        if (authentication!=null){
            throw new UserException(ErrorMessage.ANONYMOUS_USER_REQUIRED);
        }
    }
    public boolean isUsersMatch(User loggedUser, User result) {
        return loggedUser.getId() == result.getId();
    }

    public void throwIfNotTeacherOrAdmin(User user) {
        if (!isTeacherOrAdmin(user)) {
            throw new UnAuthorizeException(ErrorMessage.ADMIN_TEACHER_PERMISSION);
        }
    }

    public void throwIfNotAdmin(User user) {
        if (!isAdmin(user)) {
            throw new UnAuthorizeException(ErrorMessage.ADMIN_REQUIRED);
        }
    }

    public void throwIfNotTeacher(User user) {
        if (!isTeacher(user)) {
            throw new UnAuthorizeException(ErrorMessage.TEACHER_REQUIRED);
        }
    }

    public void throwIfUserNotEnrolled(Student student, Course course) {
        if (!course.getEnrolledStudents().contains(student)) {
            throw new UnAuthorizeException(ErrorMessage.USER_NOT_ENROLLED_FOR_COURSE_ID, student.getUsername(), course.getId());
        }
    }
    public boolean isUserEnrolledForCourse(User user, Course course) {
        return course.getEnrolledStudents().contains((Student) user);
    }
    public boolean isTeacher(User loggedUser) {
        return loggedUser.getRole().getValue().equalsIgnoreCase(ROLE_TEACHER);
    }

    public boolean isAdmin(User loggedUser) {
        return loggedUser.getRole().getValue().equalsIgnoreCase(ROLE_ADMIN);
    }

    public boolean isStudent(User loggedUser) {
        return loggedUser.getRole().getValue().equalsIgnoreCase(ROLE_STUDENT);

    }

    public boolean isTeacherOrAdmin(User loggedUser) {
        return (isAdmin(loggedUser) || isTeacher(loggedUser));
    }

    public void throwsIfUsersNotMatch(User loggedUser, User userDb) {
        if (!isUsersMatch(loggedUser,userDb)){
            throw new UnAuthorizeException(ErrorMessage.USER_NOT_RESOURCE_OWNER);
        }
    }

    @Override
    public void throwIfPassAndConfirmPassNotMatch(UserCreateDto user) {
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                throw new IncorrectInputException(INCORRECT_CONFIRM_PASSWORD);
            }
        }
}
