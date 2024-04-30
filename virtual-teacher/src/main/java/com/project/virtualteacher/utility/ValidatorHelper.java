package com.project.virtualteacher.utility;

import com.project.virtualteacher.dto.UserFullDetailsInDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import org.springframework.stereotype.Component;

import static com.project.virtualteacher.exception_handling.error_message.ErrorMessage.INCORRECT_CONFIRM_PASSWORD;

@Component
public class ValidatorHelper {

    public void validatePassAndConfirmPass(UserFullDetailsInDto user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IncorrectInputException(INCORRECT_CONFIRM_PASSWORD);
        }
    }

    public boolean isTeacher(User loggedUser) {
        return loggedUser.getRole().getValue().equalsIgnoreCase("ROLE_TEACHER");
    }

    public boolean isAdmin(User loggedUser) {
        return loggedUser.getRole().getValue().equalsIgnoreCase("ROLE_ADMIN");
    }

    public boolean isStudent(User loggedUser) {
        return loggedUser.getRole().getValue().equalsIgnoreCase("ROLE_STUDENT");

    }

    public boolean isTeacherOrAdmin(User loggedUser) {
        return (isAdmin(loggedUser) || isTeacher(loggedUser));
    }

    public void isCreatorOfCourse(Course courseDB, User loggedUser) {
        if (!courseDB.getTeacher().getUsername().equals(loggedUser.getUsername())) {
            throw new UnAuthorizeException(ErrorMessage.NOT_COURSE_CREATOR_ERROR);
        }
    }

    public boolean isUserEnrolledForCourse(User user, Course course){
        return course.getEnrolledStudents().contains(user);
    }

}
