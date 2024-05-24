package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.*;
import com.project.virtualteacher.entity.Student;
import com.project.virtualteacher.entity.Teacher;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.service.contracts.UserService;
import com.project.virtualteacher.utility.MapperImpl;
import com.project.virtualteacher.utility.UserValidatorHelperImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final MapperImpl mapperImpl;
    private final UserValidatorHelperImpl userValidatorHelperImpl;
    private final Session emailSession;


    public UserController(UserService userService, MapperImpl mapperImpl, UserValidatorHelperImpl userValidatorHelperImpl, Session emailSession) {
        this.userService = userService;
        this.mapperImpl = mapperImpl;
        this.userValidatorHelperImpl = userValidatorHelperImpl;
        this.emailSession = emailSession;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserCreateDto userCreateDto) throws MessagingException {
        userService.createUser(userCreateDto);
        return new ResponseEntity<>(String.format("User with username: %s was created", userCreateDto.getUsername())
                , HttpStatus.CREATED);

    }

    @GetMapping("/verification/{code}")
    public ResponseEntity<String> emailVerification(@PathVariable(name = "code") String code) {
        userService.emailVerification(code);
        return new ResponseEntity<>("Verification successful.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutDto> getUser(@PathVariable(name = "id") int id, Authentication authentication) {
        userValidatorHelperImpl.throwIfNoLoggedUser(authentication);
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        User userDB = userService.getUserById(id, loggedUser);
        UserOutDto userOutDtoToReturn = mapperImpl.fromUserToUserOutDto(userDB);
        return new ResponseEntity<>(userOutDtoToReturn, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") int id, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        userService.delete(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<String> block(@PathVariable(name = "id") int id, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        userService.blockUser(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was blocked", HttpStatus.OK);
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblock(@PathVariable(name = "id") int id, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        userService.unBlockUser(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was unblocked", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBaseDetails(@PathVariable(name = "id") int id, @RequestBody @Valid UserUpdateDto userUpdateDto, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        User userToUpdate = mapperImpl.fromUserUpdateDtoToUser(userUpdateDto);
        userService.updateBaseUserDetails(userToUpdate, id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was updated", HttpStatus.OK);
    }

    @PutMapping("{userId}/role/{roleId}")
    public ResponseEntity<String> updateRole(@PathVariable(name = "userId") int userId, @PathVariable(name = "roleId") int roleId, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        userService.updateRole(userId, roleId, loggedUser);
        return new ResponseEntity<>("Role of the user with ID: " + userId + " was changed", HttpStatus.OK);

    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentOutDto> getStudent(@PathVariable(name = "studentId") int studentId, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        Student student = userService.getStudentById(studentId, loggedUser);
        StudentOutDto studentToReturn = mapperImpl.fromStudentToStudentOutDto(student);
        return new ResponseEntity<>(studentToReturn, HttpStatus.OK);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<TeacherOutDto> getTeacher(@PathVariable(name = "teacherId") int teacherId, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        Teacher teacher = userService.getTeacherById(teacherId, loggedUser);
        TeacherOutDto teacherToReturn = mapperImpl.fromTeacherToTeacherOutDto(teacher);
        return new ResponseEntity<>(teacherToReturn, HttpStatus.OK);
    }
}
