package com.project.virtualteacher.dao;


import com.project.virtualteacher.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao {
    Optional<User> getByUsername(String username);

    void create(User user);

    Optional<User> getById(int userId);

    void delete(User user);

    User update(User user);

    boolean isEmailExist(String email);

    boolean isUsernameExist(String username);

    void blockUser(int userId);

    void unBlockUser(int id);
}
