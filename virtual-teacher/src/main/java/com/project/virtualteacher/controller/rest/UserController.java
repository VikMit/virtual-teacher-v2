package com.project.virtualteacher.controller.rest;


import com.project.virtualteacher.dto.UserFullDetailsInDto;
import com.project.virtualteacher.dto.UserOutDto;
import com.project.virtualteacher.dto.UserBaseDetailsInDto;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.service.UserService;
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
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserFullDetailsInDto userDetailedInDto, BindingResult errors) {

        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        validatorHelper.validatePassAndConfirmPass(userDetailedInDto);
        User userToCreate = mapper.userFullDetailsInDtoToUser(userDetailedInDto);
        userService.createUser(userToCreate);
        return new ResponseEntity<>(String.format("User with username: %s was created", userDetailedInDto.getUsername())
                , HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutDto> getUser(@PathVariable(name = "id") int id,Authentication authentication) {

        User userDb = userService.getUserById(id,authentication);
        UserOutDto userToReturn = mapper.userToUserOutDto(userDb);
        return new ResponseEntity<>(userToReturn, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") int id) {
        userService.delete(id);
    }

    //TODO
    @PutMapping("/{id}")
    public ResponseEntity<UserOutDto> update(@PathVariable(name = "id") int id, @RequestBody @Valid UserBaseDetailsInDto userBaseDetailsInDto, Authentication authentication) {
        User userToUpdate = mapper.userBaseDetailsInDtoToUser(userBaseDetailsInDto);
        User updatedUser = userService.update(userToUpdate,id ,authentication);
        return null;
    }

}
