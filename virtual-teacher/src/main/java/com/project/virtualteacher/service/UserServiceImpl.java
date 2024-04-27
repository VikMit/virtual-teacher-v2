package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.*;
import com.project.virtualteacher.service.contracts.UserService;
import com.project.virtualteacher.utility.ValidatorHelper;
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
    private final ValidatorHelper validator;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder encoder, RoleDao roleDao, ValidatorHelper validator) {
        this.userDao = userDao;
        this.encoder = encoder;
        this.roleDao = roleDao;
        this.validator = validator;
    }

    @Override
    public User getUserById(int userId, Authentication loggedUser) {
        if (validator.isTeacherOrAdmin(loggedUser)) {
            return userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND, userId));
        }
        User userDB = userDao.findByUsename(loggedUser.getName()).orElseThrow(() -> new UserNotFoundException(USER_WITH_USERNAME_NOT_FOUND, loggedUser.getName()));
        if (userDB.getId() == userId) {
            return userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND, userId));
        }
        throw new UnAuthorizeException(USER_NOT_AUTHORIZED,loggedUser.getName());
    }

    @Override
    @Transactional
    public void createUser(User user) {
        validateUsernameAndEmailNotExist(user);
        user.setRequestedRole(user.getRole());
        user.setRole(roleDao.findByName("ROLE_STUDENT").orElseThrow());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setBlocked(false);
        userDao.create(user);
    }

    @Override
    @Transactional
    public void delete(int id, Authentication loggedUser) {
        User userToDelete = userDao.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND, id));
        if (!isUsernamesMatch(loggedUser, userToDelete)) {
            throw new UnAuthorizeException(ErrorMessage.USER_NOT_RESOURCE_OWNER);
        }
        userDao.delete(userToDelete);
    }

    @Override
    @Transactional
    public void updateBaseUserDetails(User userToUpdate, int userToUpdateId, Authentication loggedUser) {
        User userDb = userDao.findById(userToUpdateId)
                .orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND, userToUpdateId));

        if (!isUsernamesMatch(loggedUser, userDb)) {
            throw new UnAuthorizeException(USER_NOT_RESOURCE_OWNER);
        }
        updateBaseDetails(userToUpdate, userDb);
        userDao.update(userDb);
    }

    @Override
    @Transactional
    public void blockUser(int id, Authentication loggedUser) {
        if (validator.isAdmin(loggedUser)) {
            userDao.block(id);
        } else {
            throw new UnAuthorizeException(ADMIN_BLOCK_PERMIT);
        }
    }

    @Override
    @Transactional
    public void unBlockUser(int id, Authentication loggedUser) {
        if (validator.isAdmin(loggedUser)) {
            userDao.unblock(id);
        } else {
            throw new UnAuthorizeException(ADMIN_UNBLOCK_PERMIT);
        }
    }

    @Override
    @Transactional
    public void updateRole(int userId, int roleId) {
        User userDb = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND, userId));
        Role role = roleDao.findById(roleId).orElseThrow(() -> new RoleNotFoundException(ROLE_ID_NOT_FOUND, roleId));
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

    private boolean isUsernamesMatch(Authentication loggedUser, User requestedUser) {
        return loggedUser.getName().equals(requestedUser.getUsername());
    }

    private void updateBaseDetails(User userToUpdate, User userDb) {
        userDb.setDob(userToUpdate.getDob());
        userDb.setFirstName(userToUpdate.getFirstName());
        userDb.setLastName(userToUpdate.getLastName());
    }
}
