package com.project.virtualteacher.service;

import com.project.virtualteacher.entity.User;
import org.springframework.security.core.Authentication;


public interface UserService {

    User getUserById(int userId,Authentication loggedUser);

    void createUser(User user);

    void delete(int id,Authentication loggedUser);

    User update(User userToUpdate,int userToUpdateId ,Authentication authentication);

    void blockUser(int id, Authentication loggedUser);
    void unBlockUser(int id, Authentication loggedUser);
}
