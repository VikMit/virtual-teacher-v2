package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.RoleDao;
import com.project.virtualteacher.dao.UserDao;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User getUserById(int userId) throws EntityNotFoundException {
        return userDao.getById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, userId));
    }

    @Override
    @Transactional
    public void createUser(User user) {
        validateUsernameAndEmailNotExist(user);
        user.setRequestedRole(user.getRole());
        user.setRole(roleDao.getRoleByName("ROLE_STUDENT"));
        user.setPassword(encoder.encode(user.getPassword()));
        user.setBlocked(false);
        userDao.create(user);
    }

    @Override
    @Transactional
    public void delete(int id, Authentication loggedUser) {
        User userToDelete = userDao.getById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, id));
        if (!isUsernamesMatch(loggedUser, userToDelete)) {
            throw new UnAuthorizeException(ErrorMessage.USER_NOT_RESOURCE_OWNER);
        }
        userDao.delete(userToDelete);
    }

    //TODO
    @Override
    @Transactional
    public void updateBaseUserDetails(User userToUpdate, int userToUpdateId, Authentication loggedUser) {
        User userDb = userDao.getById(userToUpdateId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, userToUpdateId));
        if (!isUsernamesMatch(loggedUser, userDb)){
            throw new UnAuthorizeException(USER_NOT_RESOURCE_OWNER);
        }
        updateBaseDetails(userToUpdate,userDb);
        userDao.update(userDb);
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

    @Override
    @Transactional
    public void updateRole(int userId, int roleId) {
        User userDb = userDao.getById(userId).orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND,userId));
        Role role = roleDao.getRoleById(roleId).orElseThrow(()-> new RoleNotFoundException(ROLE_ID_NOT_FOUND,roleId));
        userDb.setRole(role);
        userDao.update(userDb);
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
                .anyMatch(e -> e.getAuthority().equalsIgnoreCase("ROLE_TEACHER"));
    }

    private boolean isAdmin(Authentication loggedUser) {
        return loggedUser
                .getAuthorities()
                .stream()
                .anyMatch(e -> e.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }

    private boolean isTeacherOrAdmin(Authentication loggedUser) {
        return (isAdmin(loggedUser) || isTeacher(loggedUser));
    }

    private boolean isUsernamesMatch(Authentication loggedUser, User requestedUser) {
        return loggedUser.getName().equals(requestedUser.getUsername());
    }

    private void updateBaseDetails(User userToUpdate, User userDb) {
        userDb.setDob(userToUpdate.getDob());
        userDb.setFirstName(userToUpdate.getFirstName());
        userDb.setLastName(userToUpdate.getLastName());
    }
}
