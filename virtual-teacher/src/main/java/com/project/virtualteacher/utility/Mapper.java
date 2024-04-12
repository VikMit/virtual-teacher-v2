package com.project.virtualteacher.utility;

import com.project.virtualteacher.dao.RoleDao;
import com.project.virtualteacher.dao.UserDao;
import com.project.virtualteacher.dto.UserFullDetailsInDto;
import com.project.virtualteacher.dto.UserOutDto;
import com.project.virtualteacher.dto.UserBaseDetailsInDto;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public final class Mapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private final RoleDao roleDao;
    private final UserDao userDao;

    public Mapper(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }

    public User userFullDetailsInDtoToUser(UserFullDetailsInDto userDetailedInDto) {
        User user = new User();
        Role role = roleDao.getRoleById(userDetailedInDto.getRoleId());
        LocalDate dob = LocalDate.parse(userDetailedInDto.getDob().format(DATE_FORMATTER));
        user.setDob(dob);
        user.setPassword(userDetailedInDto.getPassword());
        user.setUsername(userDetailedInDto.getUsername());
        user.setEmail(userDetailedInDto.getEmail());
        user.setFirstName(userDetailedInDto.getFirstName());
        user.setLastName(userDetailedInDto.getLastName());
        user.setPictureUrl(userDetailedInDto.getPictureUrl());
        user.setRole(role);
        return user;
    }

    public UserOutDto userToUserOutDto(User user) {
        UserOutDto userOutDto = new UserOutDto();
        userOutDto.setUsername(user.getUsername());
        userOutDto.setEmail(user.getEmail());
        userOutDto.setFirstName(user.getFirstName());
        userOutDto.setLastName(user.getLastName());
        userOutDto.setPictureUrl(user.getPictureUrl());
        userOutDto.setRole(user.getRole());
        return userOutDto;
    }

    public User userBaseDetailsInDtoToUser(UserBaseDetailsInDto userBaseDetailsInDto) {
        User userToUpdate = new User();
        userToUpdate.setFirstName(userBaseDetailsInDto.getFirstName());
        userToUpdate.setLastName(userBaseDetailsInDto.getLastName());

        userToUpdate.setPictureUrl(userBaseDetailsInDto.getPictureUrl());
        return userToUpdate;
    }
}
