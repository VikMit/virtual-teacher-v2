package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.dto.UserCreateDto;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.entity.Student;
import com.project.virtualteacher.entity.Teacher;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityExistException;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.service.contracts.MailService;
import com.project.virtualteacher.service.contracts.UserService;
import com.project.virtualteacher.utility.contracts.Mapper;
import com.project.virtualteacher.utility.contracts.UserValidatorHelper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.virtualteacher.exception_handling.error_message.ErrorMessage.*;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserValidatorHelper userValidator;
    private final MailService mailService;
    private final Mapper mapper;

    @Value("${app.domain.address}")
    private String domain;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, UserValidatorHelper userValidator, MailService mailService, Mapper mapper) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userValidator = userValidator;
        this.mailService = mailService;
        this.mapper = mapper;
    }

    @Override
    public User getUserById(int userId, User loggedUser) {
        User userToReturn;
        if (userValidator.isTeacherOrAdmin(loggedUser)) {
            userToReturn = userDao.findById(userId).orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, userId));
            return userToReturn;
        }
        if (userValidator.isStudent(loggedUser)) {
            userToReturn = userDao.findById(userId).orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, userId));
            if (userValidator.isUsersMatch(loggedUser, userToReturn)) {
                return userToReturn;
            }
        }
        throw new UnAuthorizeException(USER_NOT_AUTHORIZED, loggedUser.getUsername());
    }

    @Override
    @Transactional
    public void createUser(UserCreateDto newUser) throws MessagingException {
        userValidator.throwIfPassAndConfirmPassNotMatch(newUser);
        Student newStudent = mapper.fromUserCreateDtoToStudent(newUser);
        validateUsernameAndEmailNotExist(newStudent);
        userDao.create(newStudent);
        mailService.sendConfirmRegistration(newStudent.getUsername(), newStudent.getFirstName() + newStudent.getLastName(), domain + "user/verification/" + newStudent.getEmailCode(), newStudent.getEmail());
    }

    @Override
    @Transactional
    public void delete(int id, User loggedUser) {
        User userToDelete = userDao.findById(id).orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, id));
        userValidator.throwsIfUsersNotMatch(loggedUser, userToDelete);
        userDao.delete(userToDelete);
    }


    @Override
    @Transactional
    public void updateBaseUserDetails(User userToUpdate, int userToUpdateId, User loggedUser) {
        User userDb = userDao.findById(userToUpdateId)
                .orElseThrow(() -> new EntityNotExistException(USER_ID_NOT_FOUND, userToUpdateId));
        userValidator.throwsIfUsersNotMatch(loggedUser, userDb);
        updateBaseDetails(userToUpdate, userDb);
        userDao.update(userDb);
    }

    @Override
    @Transactional
    public void blockUser(int id, User loggedUser) {
        userValidator.throwIfNotAdmin(loggedUser);
        if (userValidator.isAdmin(loggedUser)) {
            userDao.block(id);
        } else {
            throw new UnAuthorizeException(ADMIN_BLOCK_PERMIT);
        }
    }

    @Override
    @Transactional
    public void unBlockUser(int id, User loggedUser) {
        if (userValidator.isAdmin(loggedUser)) {
            userDao.unblock(id);
        } else {
            throw new UnAuthorizeException(ADMIN_UNBLOCK_PERMIT);
        }
    }

    @Override
    @Transactional
    public void updateRole(int userId, int roleId, User loggedUser) {
        userValidator.throwIfNotAdmin(loggedUser);
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

    @Override
    public Student getStudentById(int studentId, User loggedUser) {
        Student student = userDao.findStudentById(studentId).orElseThrow(() -> new EntityNotExistException(STUDENT_ID_NOT_FOUND, studentId));
        if (userValidator.isTeacherOrAdmin(loggedUser)) {
            return student;
        } else if (userValidator.isUsersMatch(loggedUser, student)) {
            return student;
        }
        throw new UnAuthorizeException(USER_NOT_AUTHORIZED, loggedUser.getUsername());
    }

    @Override
    public Teacher getTeacherById(int teacherId, User loggedUser) {
        if (userValidator.isTeacherOrAdmin(loggedUser)) {

            return userDao.findTeacherById(teacherId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.TEACHER_ID_NOT_FOUND, teacherId));
        }
        throw new UnAuthorizeException(USER_NOT_AUTHORIZED, loggedUser.getUsername());
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

    private void updateBaseDetails(User userToUpdate, User userDb) {
        userDb.setDob(userToUpdate.getDob());
        userDb.setFirstName(userToUpdate.getFirstName());
        userDb.setLastName(userToUpdate.getLastName());
    }

}
