package com.project.virtualteacher.service;

import com.project.virtualteacher.entity.User;
import org.springframework.security.core.Authentication;


public interface UserService {

    User getUserById(int userId,Authentication loggedUser);

    void createUser(User user);

    void delete(int id);

    User update(User userToUpdate,int userToUpdateId ,Authentication authentication);

}
