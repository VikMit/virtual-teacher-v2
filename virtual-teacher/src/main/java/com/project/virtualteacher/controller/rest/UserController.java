package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.UserBaseDetailsInDto;
import com.project.virtualteacher.dto.UserFullDetailsInDto;
import com.project.virtualteacher.dto.UserOutDto;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.service.contracts.UserService;
import com.project.virtualteacher.utility.Mapper;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final Mapper mapper;
    private final ValidatorHelper validatorHelper;

    public UserController(UserService userService, Mapper mapper, ValidatorHelper validatorHelper) {
        this.userService = userService;
        this.mapper = mapper;
        this.validatorHelper = validatorHelper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserFullDetailsInDto userDetailedInDto, BindingResult errors) {

        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        validatorHelper.validatePassAndConfirmPass(userDetailedInDto);
        User userToCreate = mapper.fromUserFullDetailsInDtoToUser(userDetailedInDto);
        userService.createUser(userToCreate);
        return new ResponseEntity<>(String.format("User with username: %s was created", userDetailedInDto.getUsername())
                , HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutDto> getUser(@PathVariable(name = "id") int id, Authentication loggedUser) {
        User userDb = userService.getUserById(id,loggedUser);
        UserOutDto userToReturn = mapper.fromUserToUserOutDto(userDb);
        return new ResponseEntity<>(userToReturn, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") int id, Authentication loggedUser) {
        userService.delete(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<String> block(@PathVariable(name = "id") int id, Authentication loggedUser) {
        userService.blockUser(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was blocked", HttpStatus.OK);
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblock(@PathVariable(name = "id") int id, Authentication loggedUser) {
        userService.unBlockUser(id, loggedUser);
        return new ResponseEntity<>("User with ID: " + id + " was unblocked", HttpStatus.OK);
    }

    @PutMapping("/{id}/basic-details")
    public ResponseEntity<String> updateBaseDetails(@PathVariable(name = "id") int id, @RequestBody @Valid UserBaseDetailsInDto userBaseDetailsInDto, Authentication loggedUser) {
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
