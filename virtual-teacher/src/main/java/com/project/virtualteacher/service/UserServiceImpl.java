package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.RoleDao;
import com.project.virtualteacher.dao.UserDao;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EmailExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.exception_handling.exceptions.UserNotFoundException;
import com.project.virtualteacher.exception_handling.exceptions.UsernameExistException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.project.virtualteacher.exception_handling.error_message.ErrorMessage.*;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final RoleDao roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder encoder, RoleDao roleDao) {
        this.userDao = userDao;
        this.encoder = encoder;
        this.roleDao = roleDao;
    }

    @Override
    public User getUserById(int userId, Authentication loggedUser) throws EntityNotFoundException {
        User userDb = userDao.getById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, userId));
        if (isTeacherOrAdmin(loggedUser) || isUsersMatch(loggedUser, userDb)) {
            return userDb;
        }
        throw new UnAuthorizeException("Only owner of the account, 'Teacher' or 'Admin' has access to this resource");
    }

    @Override
    @Transactional
    //TODO Think how to set Role of the user - using cache or invoke DB 
    public void createUser(User user) {
        validateUsernameAndEmailNotExist(user);
        user.setRequestedRole(user.getRole());
        user.setRole(roleDao.getRoleByName("Student"));
        user.setPassword(encoder.encode(user.getPassword()));
        user.setBlocked(false);
        userDao.create(user);
    }

    @Override
    @Transactional
    public void delete(int id, Authentication loggedUser) {
        User userToDelete = userDao.getById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, id));
        if (!isUsersMatch(loggedUser, userToDelete)) {
            throw new UnAuthorizeException(ErrorMessage.USER_NOT_RESOURCE_OWNER);
        }
        userDao.delete(userToDelete);
    }

    //TODO
    @Override
    public User update(User userToUpdate, int userToUpdateId, Authentication authentication) {
        Principal loggedUser = (Principal) authentication.getPrincipal();
        User userDb = userDao.getById(userToUpdateId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, userToUpdateId));
        if (!userToUpdate.getUsername().equals(loggedUser.getName())) {
            throw new UnAuthorizeException("Only owner of the account can update it");
        }
        if (!userDb.getEmail().equalsIgnoreCase(userToUpdate.getEmail())) {

        }

        return userDao.update(userToUpdate);

    }

    @Override
    @Transactional
    public void blockUser(int id, Authentication loggedUser) {
        if (isAdmin(loggedUser)) {
            userDao.blockUser(id);
        } else {
            throw new UnAuthorizeException("Only ADMIN can block users");
        }
    }

    @Override
    @Transactional
    public void unBlockUser(int id, Authentication loggedUser) {
        if (isAdmin(loggedUser)) {
            userDao.unBlockUser(id);
        } else {
            throw new UnAuthorizeException("Only ADMIN can unblock users");
        }
    }

    //TODO
    private void verifyFieldIsUniqueIfChanged() {

    }

    private void validateUsernameAndEmailNotExist(User user) {
        if (userDao.isUsernameExist(user.getUsername())) {
            throw new UsernameExistException(USERNAME_EXIST, user.getUsername());
        }
        if (userDao.isEmailExist(user.getEmail())) {
            throw new EmailExistException(EMAIL_EXIST, user.getEmail());
        }
    }

    private boolean isTeacher(Authentication loggedUser) {
        return loggedUser
                .getAuthorities()
                .stream()
                .anyMatch(e -> e.getAuthority().equalsIgnoreCase("TEACHER"));
    }

    private boolean isAdmin(Authentication loggedUser) {
        return loggedUser
                .getAuthorities()
                .stream()
                .anyMatch(e -> e.getAuthority().equalsIgnoreCase("ADMIN"));
    }

    private boolean isTeacherOrAdmin(Authentication loggedUser) {
        return (isAdmin(loggedUser) || isTeacher(loggedUser));
    }

    private boolean isUsersMatch(Authentication loggedUser, User requestedUser) {
        return loggedUser.getName().equals(requestedUser.getUsername());
    }
}
