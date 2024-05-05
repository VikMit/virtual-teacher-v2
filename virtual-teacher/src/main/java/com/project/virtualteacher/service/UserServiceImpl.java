package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityExistException;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.service.contracts.MailService;
import com.project.virtualteacher.service.contracts.UserService;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.project.virtualteacher.exception_handling.error_message.ErrorMessage.*;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final RoleDao roleDao;
    private final ValidatorHelper validator;
    private final MailService mailService;
    @Value("${app.domain.address}")
    private String domain;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder encoder, RoleDao roleDao, ValidatorHelper validator, MailService mailService) {
        this.userDao = userDao;
        this.encoder = encoder;
        this.roleDao = roleDao;
        this.validator = validator;
        this.mailService = mailService;
    }

    @Override
    public User getUserById(int userId, User loggedUser) {
        if (validator.isTeacherOrAdmin(loggedUser)) {
            return userDao.findById(userId).orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, userId));
        }
        User userDB = userDao.findByUsename(loggedUser.getUsername()).orElseThrow(() -> new EntityNotExistException(USER_WITH_USERNAME_NOT_FOUND, loggedUser.getUsername()));
        if (userDB.getId() == userId) {
            return userDao.findById(userId).orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, userId));
        }
        throw new UnAuthorizeException(USER_NOT_AUTHORIZED, loggedUser.getUsername());
    }

    @Override
    @Transactional
    //TODO refactor code for more readability
    public void createUser(User user) throws MessagingException {
        validateUsernameAndEmailNotExist(user);
        user.setRequestedRole(user.getRole());
        user.setRole(roleDao.findByName("ROLE_STUDENT").orElseThrow());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setBlocked(false);
        user.setEmailVerified(false);
        byte[] emailToByteArray = convertEmailToByteArr(user.getEmail());
        user.setEmailCode(String.valueOf(UUID.nameUUIDFromBytes(emailToByteArray)));
        mailService.sendConfirmRegistration(user.getUsername(), user.getFirstName() + user.getLastName(), domain + "user/verification/" + user.getEmailCode(), user.getEmail());
        userDao.create(user);
    }

    @Override
    @Transactional
    public void delete(int id, User loggedUser) {
        User userToDelete = userDao.findById(id).orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, id));
        if (!isUsernamesMatch(loggedUser, userToDelete)) {
            throw new UnAuthorizeException(ErrorMessage.USER_NOT_RESOURCE_OWNER);
        }
        userDao.delete(userToDelete);
    }

    @Override
    @Transactional
    public void updateBaseUserDetails(User userToUpdate, int userToUpdateId, User loggedUser) {
        User userDb = userDao.findById(userToUpdateId)
                .orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, userToUpdateId));

        if (!isUsernamesMatch(loggedUser, userDb)) {
            throw new UnAuthorizeException(USER_NOT_RESOURCE_OWNER);
        }
        updateBaseDetails(userToUpdate, userDb);
        userDao.update(userDb);
    }

    @Override
    @Transactional
    public void blockUser(int id, User loggedUser) {
        if (validator.isAdmin(loggedUser)) {
            userDao.block(id);
        } else {
            throw new UnAuthorizeException(ADMIN_BLOCK_PERMIT);
        }
    }

    @Override
    @Transactional
    public void unBlockUser(int id, User loggedUser) {
        if (validator.isAdmin(loggedUser)) {
            userDao.unblock(id);
        } else {
            throw new UnAuthorizeException(ADMIN_UNBLOCK_PERMIT);
        }
    }

    @Override
    @Transactional
    public void updateRole(int userId, int roleId) {
        User userDb = userDao.findById(userId).orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, userId));
        Role role = roleDao.findById(roleId).orElseThrow(() -> new EntityNotExistException(ROLE_ID_NOT_FOUND, roleId));
        userDb.setRole(role);
        userDao.update(userDb);
    }

    @Override
    @Transactional
    public void emailVerification(String code) {
        userDao.verifyEmail(code);
    }

    //TODO
    private void verifyFieldIsUniqueIfChanged() {

    }

    private void validateUsernameAndEmailNotExist(User user) {
        if (userDao.isUsernameExist(user.getUsername())) {
            throw new EntityExistException(USERNAME_EXIST, user.getUsername());
        }
        if (userDao.isEmailExist(user.getEmail())) {
            throw new EntityExistException(EMAIL_EXIST, user.getEmail());
        }
    }

    private boolean isUsernamesMatch(User loggedUser, User requestedUser) {
        return loggedUser.getUsername().equals(requestedUser.getUsername());
    }

    private void updateBaseDetails(User userToUpdate, User userDb) {
        userDb.setDob(userToUpdate.getDob());
        userDb.setFirstName(userToUpdate.getFirstName());
        userDb.setLastName(userToUpdate.getLastName());
    }

    private byte[] convertEmailToByteArr(String email) {
        return email.transform(String::getBytes);
    }
}
