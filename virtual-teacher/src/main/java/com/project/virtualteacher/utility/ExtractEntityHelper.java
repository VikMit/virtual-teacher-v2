package com.project.virtualteacher.utility;

import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ExtractEntityHelper {

    private final UserDao userDao;

    public ExtractEntityHelper(UserDao userDao) {
        this.userDao = userDao;
    }

    public User extractUserFromAuthentication(Authentication loggedUser){
        if (loggedUser==null){
            return null;
        }
        String username = loggedUser.getName();
        return userDao.findByUsename(username).orElseThrow(()->new EntityNotExistException(ErrorMessage.USER_WITH_USERNAME_NOT_FOUND));
    }
}
