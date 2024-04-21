package com.project.virtualteacher.utility;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.dto.CourseDto;
import com.project.virtualteacher.dto.UserFullDetailsInDto;
import com.project.virtualteacher.dto.UserOutDto;
import com.project.virtualteacher.dto.UserBaseDetailsInDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.entity.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public final class Mapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RoleDao roleDao;


    public Mapper(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
    }

    public User userFullDetailsInDtoToUser(UserFullDetailsInDto detailedUserInDto) {
        User user = userBaseDetailsInDtoToUser(detailedUserInDto);
        Role role = roleDao.getRoleById(detailedUserInDto.getRoleId()).orElseThrow();
        user.setUsername(detailedUserInDto.getUsername());
        user.setPassword(detailedUserInDto.getPassword());
        user.setEmail(detailedUserInDto.getEmail());
        user.setRole(role);
        if (user.getPictureUrl()==null){
            user.setPictureUrl("Default picture URL");
        }
        else{
            user.setPictureUrl(detailedUserInDto.getPictureUrl());
        }
        return user;
    }

    public UserOutDto userToUserOutDto(User user) {
        UserOutDto userOutDto = new UserOutDto();
        userOutDto.setUserId(user.getId());
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
        userToUpdate.setDob(userBaseDetailsInDto.getDob());
        return userToUpdate;
    }
    public Course courseDtoToCourse(CourseDto courseDto){
        Course course = new Course();
        course.setDescription(courseDto.getDescription());
        course.setPublished(courseDto.isPublished());
        course.setPassingGrade(courseDto.getPassingGrade());
        course.setTitle(courseDto.getTitle());
        course.setStartDate(courseDto.getStartDate());
        course.setLectures(courseDto.getLectures());
        return course;
    }
}
