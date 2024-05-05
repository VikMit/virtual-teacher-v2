package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.UserBaseDetailsInDto;
import com.project.virtualteacher.dto.UserFullDetailsInDto;
import com.project.virtualteacher.dto.UserOutDto;
import com.project.virtualteacher.service.MailTemplatesGeneratorServiceImpl;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.service.contracts.UserService;
import com.project.virtualteacher.utility.BindingResultCatcher;
import com.project.virtualteacher.utility.ExtractEntityHelper;
import com.project.virtualteacher.utility.Mapper;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController()
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final Mapper mapper;
    private final ValidatorHelper validatorHelper;
    private final ExtractEntityHelper extractEntityHelper;
    private final BindingResultCatcher catchInputErrors;
    private final Properties properties;
    private final Session emailSession;


    public UserController(UserService userService, Mapper mapper, ValidatorHelper validatorHelper, ExtractEntityHelper extractEntityHelper, BindingResultCatcher catchInputErrors, Properties properties, Session emailSession) {
        this.userService = userService;
        this.mapper = mapper;
        this.validatorHelper = validatorHelper;
        this.extractEntityHelper = extractEntityHelper;
        this.catchInputErrors = catchInputErrors;
        this.properties = properties;
        this.emailSession = emailSession;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserFullDetailsInDto userDetailedInDto, BindingResult errors) throws MessagingException {
        catchInputErrors.proceedInputError(errors);
        validatorHelper.validatePassAndConfirmPass(userDetailedInDto);
        User userToCreate = mapper.fromUserFullDetailsInDtoToUser(userDetailedInDto);
        userService.createUser(userToCreate);
        return new ResponseEntity<>(String.format("User with username: %s was created", userDetailedInDto.getUsername())
                , HttpStatus.CREATED);

    }
    @GetMapping("/verification/{code}")
    public ResponseEntity<String> emailVerification(@PathVariable(name = "code")String code){
        userService.emailVerification(code);
        return new ResponseEntity<>("Verification successful.",HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutDto> getUser(@PathVariable(name = "id") int id, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        User userDb = userService.getUserById(id, loggedUser);
        UserOutDto userToReturn = mapper.fromUserToUserOutDto(userDb);
        return new ResponseEntity<>(userToReturn, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") int id, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        userService.delete(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<String> block(@PathVariable(name = "id") int id, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        userService.blockUser(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was blocked", HttpStatus.OK);
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblock(@PathVariable(name = "id") int id, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        userService.unBlockUser(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was unblocked", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBaseDetails(@PathVariable(name = "id") int id, @RequestBody @Valid UserBaseDetailsInDto userBaseDetailsInDto, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        User userToUpdate = mapper.fromUserBaseDetailsInDtoToUser(userBaseDetailsInDto);
        userService.updateBaseUserDetails(userToUpdate, id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was updated", HttpStatus.OK);
    }

    @PutMapping("{userId}/role/{roleId}")
    public ResponseEntity<String> updateRole(@PathVariable(name = "userId") int userId, @PathVariable(name = "roleId") int roleId) {
        userService.updateRole(userId, roleId);
        return new ResponseEntity<>("Role of the user with ID: " + userId + " was changed", HttpStatus.OK);

    }

}
