package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;


public interface UserService {

    User getUserById(int userId, User loggedUser);

    void createUser(User user) throws MessagingException;

    void delete(int id, User loggedUser);

    void updateBaseUserDetails(User userToUpdate, int userToUpdateId, User loggedUser);

    void blockUser(int id, User loggedUser);

    void unBlockUser(int id, User loggedUser);

    void updateRole(int userId, int roleId);

    void emailVerification(String code);
}
