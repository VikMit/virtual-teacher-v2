package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.CourseBaseDetailsDto;
import com.project.virtualteacher.dto.CourseFullDetailsDto;
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
    public ResponseEntity<CourseFullDetailsDto> course(@RequestBody CourseFullDetailsDto courseDetailedInfoDto, Authentication loggedUser) {
        Course courseToCreate = mapper.fromCourseFullDetailsDtoToCourse(courseDetailedInfoDto);
        User user = extractEntityHelper.extractUserFromAuthentication(loggedUser);
        Course createdCourse = courseService.create(courseToCreate, user);
        CourseFullDetailsDto courseFullDetailsDto = mapper.fromCourseToCourseFullDetailsDto(createdCourse);
        return new ResponseEntity<>(courseFullDetailsDto, HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}/public/basic")
    public ResponseEntity<CourseBaseDetailsDto> courseBasicDetailsById(@PathVariable(name = "courseId") int courseId) {
        Course course = courseService.getPublicCourseById(courseId);
        CourseBaseDetailsDto result = mapper.fromCourseToCourseBaseDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/title/public/basic")
    public ResponseEntity<CourseBaseDetailsDto> courseBaseDetails(@RequestParam(name = "title") String title) {
        Course course = courseService.getPublicCourseByTitle(title);
        CourseBaseDetailsDto result = mapper.fromCourseToCourseBaseDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{courseId}/full")
    public ResponseEntity<CourseFullDetailsDto> courseFullDetailsById(@PathVariable(name = "courseId") int courseId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseById(courseId, loggedUser);
        CourseFullDetailsDto result = mapper.fromCourseToCourseFullDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/title/full")
    public ResponseEntity<CourseFullDetailsDto> courseByTitle(@RequestParam(name = "title") String title, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseByTitle(title, loggedUser);
        CourseFullDetailsDto result = mapper.fromCourseToCourseFullDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> update(@PathVariable(name = "courseId") int courseId, @RequestBody @Valid CourseFullDetailsDto courseFullDetailsDto, BindingResult errors, Authentication authentication) {
        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Course courseToUpdate = mapper.fromCourseFullDetailsDtoToCourse(courseFullDetailsDto);
        Course updatedCourse = courseService.update(courseId, courseToUpdate, loggedUser);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @GetMapping("/all-public/basic")
    public ResponseEntity<Set<CourseBaseDetailsDto>> getAllPublicWithBaseDetails() {
        Set<Course> allCourses = courseService.getAllPublic();
        Set<CourseBaseDetailsDto> extractedCourseBaseDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseBaseDetailsDto b = mapper.fromCourseToCourseBaseDetailsDto(course);
            extractedCourseBaseDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseBaseDetails, HttpStatus.OK);
    }

    @GetMapping("/all/full")
    public ResponseEntity<Set<CourseFullDetailsDto>> getAllWithFullDetails(Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Set<Course> allCourses = courseService.getAll(loggedUser);
        Set<CourseFullDetailsDto> extractedCourseFullDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseFullDetailsDto b = mapper.fromCourseToCourseFullDetailsDto(course);
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
