package com.project.virtualteacher.utility;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.dto.*;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.entity.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public final class Mapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RoleDao roleDao;


    public Mapper(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
    }

    public User fromUserFullDetailsInDtoToUser(UserFullDetailsInDto detailedUserInDto) {
        User user = fromUserBaseDetailsInDtoToUser(detailedUserInDto);
        Role role = roleDao.findById(detailedUserInDto.getRoleId()).orElseThrow();
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

    public UserOutDto fromUserToUserOutDto(User user) {
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

    public User fromUserBaseDetailsInDtoToUser(UserBaseDetailsInDto userBaseDetailsInDto) {
        User userToUpdate = new User();
        userToUpdate.setFirstName(userBaseDetailsInDto.getFirstName());
        userToUpdate.setLastName(userBaseDetailsInDto.getLastName());
        userToUpdate.setDob(userBaseDetailsInDto.getDob());
        return userToUpdate;
    }
    public Course fromCourseFullDetailsDtoToCourse(CourseFullDetailsDto courseDetailedInfoDto){
        Course course = new Course();
        course.setDescription(courseDetailedInfoDto.getDescription());
        course.setPublished(courseDetailedInfoDto.getIsPublished());
        course.setPassingGrade(courseDetailedInfoDto.getPassingGrade());
        course.setTitle(courseDetailedInfoDto.getTitle());
        course.setStartDate(courseDetailedInfoDto.getStartDate());
        course.setLectures(courseDetailedInfoDto.getLectures());
        return course;
    }

    public Course fromCourseBaseDetailsDtoToCourse(CourseBaseDetailsDto courseBaseDetailsDto){
        Course course = new Course();
        course.setTitle(courseBaseDetailsDto.getTitle());
        course.setStartDate(courseBaseDetailsDto.getStartDate());
        course.setPublished(courseBaseDetailsDto.getIsPublished());
        course.setPassingGrade(courseBaseDetailsDto.getPassingGrade());
        course.setDescription(courseBaseDetailsDto.getDescription());
        return course;
    }

    public CourseBaseDetailsDto fromCourseToCourseBaseDetailsDto(Course updatedCourse) {
        CourseBaseDetailsDto result = new CourseBaseDetailsDto();
        result.setCreator(fromUserToUserOutDto(updatedCourse.getTeacher()));
        result.setIsPublished(updatedCourse.isPublished());
        result.setDescription(updatedCourse.getDescription());
        result.setTitle(updatedCourse.getTitle());
        result.setStartDate(updatedCourse.getStartDate());
        result.setPassingGrade(updatedCourse.getPassingGrade());
        return result;
    }

    public CourseFullDetailsDto fromCourseToCourseFullDetailsDto(Course course){
        CourseFullDetailsDto result = new CourseFullDetailsDto();
        result.setCreator(fromUserToUserOutDto(course.getTeacher()));
        result.setIsPublished(course.isPublished());
        result.setDescription(course.getDescription());
        result.setPassingGrade(course.getPassingGrade());
        result.setTitle(course.getTitle());
        result.setStartDate(course.getStartDate());
        result.setTopics(course.getTopics());
        result.setLectures(course.getLectures());
        result.setEnrolledStudents(course.getEnrolledStudents().stream().map(this::fromUserToUserOutDto).collect(Collectors.toSet()));
        return result;
    }
}
