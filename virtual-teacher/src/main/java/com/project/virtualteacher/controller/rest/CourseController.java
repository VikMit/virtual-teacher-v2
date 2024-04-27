package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.CourseBaseDetailsDto;
import com.project.virtualteacher.dto.CourseFullDetailsDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.service.contracts.CourseService;
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

    public CourseController(CourseService courseService, Mapper mapper) {
        this.courseService = courseService;
        this.mapper = mapper;
    }

    //Only teacher has access to create course end-point
    @PostMapping()
    public ResponseEntity<String> course(@RequestBody CourseFullDetailsDto courseDetailedInfoDto, Authentication loggedUser) {
        Course courseToCreate = mapper.fromCourseFullDetailsDtoToCourse(courseDetailedInfoDto);
        Course createdCourse = courseService.create(courseToCreate, loggedUser);
        return new ResponseEntity<>("Course with title: " + createdCourse.getTitle() + " was created", HttpStatus.CREATED);
    }

    //All users include anonymous has access to basic details of each public course
    @GetMapping("/{courseId}/public/basic-details")
    public ResponseEntity<CourseBaseDetailsDto> courseBasicDetailsById(@PathVariable(name = "courseId") int courseId) {
        Course course = courseService.getPublicCourseById(courseId);
        CourseBaseDetailsDto result = mapper.fromCourseToCourseBaseDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //All users include anonymous has access to basic details of each public course
    @GetMapping("/title/public/basic-details")
    public ResponseEntity<CourseBaseDetailsDto> courseBaseDetails(@RequestParam(name = "title") String title) {
        Course course = courseService.getPublicCourseByTitle(title);
        CourseBaseDetailsDto result = mapper.fromCourseToCourseBaseDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{courseId}/full-details")
    public ResponseEntity<CourseFullDetailsDto> courseFullDetailsById(@PathVariable(name = "courseId") int courseId, Authentication loggedUser) {
        Course course = courseService.getCourseById(courseId, loggedUser);
        CourseFullDetailsDto result = mapper.fromCourseToCourseFullDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/title/full-details")
    public ResponseEntity<CourseFullDetailsDto> courseByTitle(@RequestParam(name = "title") String title, Authentication loggedUser) {
        Course course = courseService.getCourseByTitle(title, loggedUser);
        CourseFullDetailsDto result = mapper.fromCourseToCourseFullDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> update(@PathVariable(name = "courseId") int courseId, @RequestBody @Valid CourseFullDetailsDto courseFullDetailsDto, BindingResult errors, Authentication loggedUser) {
        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        Course courseToUpdate = mapper.fromCourseFullDetailsDtoToCourse(courseFullDetailsDto);
        Course updatedCourse = courseService.update(courseId, courseToUpdate, loggedUser);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    //All users include anonymous has access to basic details of each public course
    @GetMapping("/all-public/basic-details")
    public ResponseEntity<Set<CourseBaseDetailsDto>> getAllPublicWithBaseDetails() {
        Set<Course> allCourses = courseService.getAllPublic();
        Set<CourseBaseDetailsDto> extractedCourseBaseDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseBaseDetailsDto b = mapper.fromCourseToCourseBaseDetailsDto(course);
            extractedCourseBaseDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseBaseDetails, HttpStatus.OK);
    }

    @GetMapping("/all/full-details")
    public ResponseEntity<Set<CourseFullDetailsDto>> getAllWithFullDetails(Authentication loggedUser) {
        Set<Course> allCourses = courseService.getAll(loggedUser);
        Set<CourseFullDetailsDto> extractedCourseFullDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseFullDetailsDto b = mapper.fromCourseToCourseFullDetailsDto(course);
            extractedCourseFullDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseFullDetails, HttpStatus.OK);
    }
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> delete(@PathVariable(name = "courseId")int courseId, Authentication loggedUser){
        courseService.delete(courseId,loggedUser);
        return new ResponseEntity<>("Course with ID: "+courseId+" was deleted",HttpStatus.OK);
    }
}
