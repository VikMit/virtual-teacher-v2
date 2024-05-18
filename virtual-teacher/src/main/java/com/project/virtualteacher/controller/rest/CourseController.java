package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.CourseBaseOutDto;
import com.project.virtualteacher.dto.CourseCreateDto;
import com.project.virtualteacher.dto.CourseFullOutDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.ExtractEntityHelper;
import com.project.virtualteacher.utility.Mapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService courseService;
    private final Mapper mapper;
    private final ExtractEntityHelper extractEntityHelper;

    public CourseController(CourseService courseService, Mapper mapper, ExtractEntityHelper extractEntityHelper) {
        this.courseService = courseService;
        this.mapper = mapper;
        this.extractEntityHelper = extractEntityHelper;
    }

    @PostMapping()
    public ResponseEntity<CourseFullOutDto> course(@RequestBody CourseCreateDto courseCreateDto, Authentication loggedUser) {
        Course courseToCreate = mapper.fromCourseCreateDtoToCourse(courseCreateDto);
        User user = extractEntityHelper.extractUserFromAuthentication(loggedUser);
        Course createdCourse = courseService.create(courseToCreate, user);
        CourseFullOutDto courseFullDetailsOutDto = mapper.fromCourseToCourseFullOutDto(createdCourse);
        return new ResponseEntity<>(courseFullDetailsOutDto, HttpStatus.CREATED);
    }

    //TESTED with POSTMAN
    @GetMapping("/{courseId}/basic")
    public ResponseEntity<CourseBaseOutDto> courseBasicDetailsById(@PathVariable(name = "courseId") int courseId,Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseById(courseId,loggedUser);
        CourseBaseOutDto result = mapper.fromCourseToCourseBaseOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //TESTED with POSTMAN
    @GetMapping("/basic")
    public ResponseEntity<CourseBaseOutDto> courseBaseDetails(@RequestParam(name = "title") String title, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseByTitle(title,loggedUser);
        CourseBaseOutDto result = mapper.fromCourseToCourseBaseOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{courseId}/full")
    public ResponseEntity<CourseFullOutDto> courseFullDetailsById(@PathVariable(name = "courseId") int courseId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseById(courseId, loggedUser);
        CourseFullOutDto result = mapper.fromCourseToCourseFullOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/title/full")
    public ResponseEntity<CourseFullOutDto> courseByTitle(@RequestParam(name = "title") String title, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseByTitle(title, loggedUser);
        CourseFullOutDto result = mapper.fromCourseToCourseFullOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> update(@PathVariable(name = "courseId") int courseId, @RequestBody @Valid CourseCreateDto courseCreateDto, BindingResult errors, Authentication authentication) {
        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course courseToUpdate = mapper.fromCourseCreateDtoToCourse(courseCreateDto);
        Course updatedCourse = courseService.update(courseId, courseToUpdate, loggedUser);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @GetMapping("/all-public/basic")
    public ResponseEntity<Set<CourseBaseOutDto>> getAllPublicWithBaseDetails() {
        Set<Course> allCourses = courseService.getAllPublic();
        Set<CourseBaseOutDto> extractedCourseBaseDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseBaseOutDto b = mapper.fromCourseToCourseBaseOutDto(course);
            extractedCourseBaseDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseBaseDetails, HttpStatus.OK);
    }

    @GetMapping("/all/full")
    public ResponseEntity<Set<CourseFullOutDto>> getAllWithFullDetails(@RequestParam(name = "page",required = false,defaultValue = "1") @Min(value = 1,message = "Page must be a positive integer") int page,
                                                                       @RequestParam(name = "size",required = false,defaultValue = "10") @Min(value = 1,message = "Size must be a positive integer") int size, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Set<Course> allCourses = courseService.getAll(loggedUser,page,size);
        Set<CourseFullOutDto> extractedCourseFullDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseFullOutDto b = mapper.fromCourseToCourseFullOutDto(course);
            extractedCourseFullDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseFullDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> delete(@PathVariable(name = "courseId") int courseId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        courseService.delete(courseId, loggedUser);
        return new ResponseEntity<>("Course with ID: " + courseId + " was deleted", HttpStatus.OK);
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enroll(@PathVariable(name = "courseId") int courseId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        courseService.enroll(courseId,loggedUser);
        return new ResponseEntity<>("User with username: "+loggedUser.getUsername()+" was enrolled for course with ID: "+courseId,HttpStatus.OK);
    }
}
