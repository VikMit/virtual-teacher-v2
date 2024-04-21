package com.project.virtualteacher.utility;

import com.project.virtualteacher.dto.UserFullDetailsInDto;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectConfirmPasswordException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.project.virtualteacher.exception_handling.error_message.ErrorMessage.INCORRECT_CONFIRM_PASSWORD;

@Component
public class ValidatorHelper {

    public void validatePassAndConfirmPass(UserFullDetailsInDto user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IncorrectConfirmPasswordException(INCORRECT_CONFIRM_PASSWORD);
        }
    }

    public boolean isTeacher(Authentication loggedUser) {
        return loggedUser
                .getAuthorities()
                .stream()
                .anyMatch(e -> e.getAuthority().equalsIgnoreCase("ROLE_TEACHER"));
    }

    public boolean isAdmin(Authentication loggedUser) {
        return loggedUser
                .getAuthorities()
                .stream()
                .anyMatch(e -> e.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }

    public boolean isStudent(Authentication loggedUser) {
        return loggedUser
                .getAuthorities()
                .stream()
                .anyMatch(e -> e.getAuthority().equalsIgnoreCase("ROLE_STUDENT"));
    }

    public boolean isTeacherOrAdmin(Authentication loggedUser) {
        return (isAdmin(loggedUser) || isTeacher(loggedUser));
    }

}
