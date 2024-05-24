package com.project.virtualteacher.utility.contracts;

import com.project.virtualteacher.dto.UserCreateDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Student;
import com.project.virtualteacher.entity.User;
import org.springframework.security.core.Authentication;

public interface UserValidatorHelper {
    User extractUserFromAuthentication(Authentication loggedUser);

    void throwIfNoLoggedUser(Authentication authentication);

    void throwIfLoggedUser(Authentication authentication);

    boolean isUsersMatch(User loggedUser, User result);

    void throwIfNotTeacherOrAdmin(User user);

    void throwIfNotAdmin(User user);

    void throwIfNotTeacher(User user);

    void throwIfUserNotEnrolled(Student student, Course course);

    boolean isUserEnrolledForCourse(User user, Course course);

    boolean isTeacher(User loggedUser);

    boolean isAdmin(User loggedUser);

    boolean isStudent(User loggedUser);

    boolean isTeacherOrAdmin(User loggedUser);

    void throwsIfUsersNotMatch(User loggedUser, User userDb);
    void throwIfPassAndConfirmPassNotMatch(UserCreateDto user);
}
