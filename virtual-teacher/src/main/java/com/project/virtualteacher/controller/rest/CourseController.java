package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.CourseBaseDetailsOutDto;
import com.project.virtualteacher.dto.CourseCreateDto;
import com.project.virtualteacher.dto.CourseFullDetailsOutDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.ExtractEntityHelper;
import com.project.virtualteacher.utility.Mapper;
import jakarta.validation.Valid;
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
    public ResponseEntity<CourseFullDetailsOutDto> course(@RequestBody CourseCreateDto courseCreateDto, Authentication loggedUser) {
        Course courseToCreate = mapper.fromCourseCreateDtoToCourse(courseCreateDto);
        User user = extractEntityHelper.extractUserFromAuthentication(loggedUser);
        Course createdCourse = courseService.create(courseToCreate, user);
        CourseFullDetailsOutDto courseFullDetailsOutDto = mapper.fromCourseToCourseFullDetailsOutDto(createdCourse);
        return new ResponseEntity<>(courseFullDetailsOutDto, HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}/public/basic")
    public ResponseEntity<CourseBaseDetailsOutDto> courseBasicDetailsById(@PathVariable(name = "courseId") int courseId) {
        Course course = courseService.getPublicCourseById(courseId);
        CourseBaseDetailsOutDto result = mapper.fromCourseToCourseBaseDetailsOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/title/public/basic")
    public ResponseEntity<CourseBaseDetailsOutDto> courseBaseDetails(@RequestParam(name = "title") String title) {
        Course course = courseService.getPublicCourseByTitle(title);
        CourseBaseDetailsOutDto result = mapper.fromCourseToCourseBaseDetailsOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{courseId}/full")
    public ResponseEntity<CourseFullDetailsOutDto> courseFullDetailsById(@PathVariable(name = "courseId") int courseId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseById(courseId, loggedUser);
        CourseFullDetailsOutDto result = mapper.fromCourseToCourseFullDetailsOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/title/full")
    public ResponseEntity<CourseFullDetailsOutDto> courseByTitle(@RequestParam(name = "title") String title, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseByTitle(title, loggedUser);
        CourseFullDetailsOutDto result = mapper.fromCourseToCourseFullDetailsOutDto(course);
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
    public ResponseEntity<Set<CourseBaseDetailsOutDto>> getAllPublicWithBaseDetails() {
        Set<Course> allCourses = courseService.getAllPublic();
        Set<CourseBaseDetailsOutDto> extractedCourseBaseDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseBaseDetailsOutDto b = mapper.fromCourseToCourseBaseDetailsOutDto(course);
            extractedCourseBaseDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseBaseDetails, HttpStatus.OK);
    }

    @GetMapping("/all/full")
    public ResponseEntity<Set<CourseFullDetailsOutDto>> getAllWithFullDetails(Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Set<Course> allCourses = courseService.getAll(loggedUser);
        Set<CourseFullDetailsOutDto> extractedCourseFullDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseFullDetailsOutDto b = mapper.fromCourseToCourseFullDetailsOutDto(course);
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
