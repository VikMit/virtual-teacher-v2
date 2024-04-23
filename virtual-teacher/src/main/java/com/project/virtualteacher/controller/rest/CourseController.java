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

    @PostMapping()
    public ResponseEntity<String> course(@RequestBody CourseFullDetailsDto courseDetailedInfoDto, Authentication loggedUser) {
        Course courseToCreate = mapper.fromCourseFullDetailsDtoToCourse(courseDetailedInfoDto);
        Course createdCourse = courseService.create(courseToCreate, loggedUser);
        return new ResponseEntity<>("Course with title: " + createdCourse.getTitle() + " was created", HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}/basic-details")
    public ResponseEntity<CourseBaseDetailsDto> courseBasicDetailsById(@PathVariable(name = "courseId") int courseId) {
        Course course = courseService.getPublicCourseById(courseId);
        CourseBaseDetailsDto result = mapper.fromCourseToCourseBaseDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{courseId}/full-details")
    public ResponseEntity<CourseFullDetailsDto> courseFullDetailsById(@PathVariable(name = "courseId") int courseId, Authentication loggedUser) {
        Course course = courseService.getCourseById(courseId, loggedUser);
        CourseFullDetailsDto result = mapper.fromCourseToCourseFullDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/title/basic-details")
    public ResponseEntity<CourseBaseDetailsDto> courseBaseDetails(@RequestParam(name = "title") String title) {
        Course course = courseService.getPublicCourseByTitle(title);
        CourseBaseDetailsDto result = mapper.fromCourseToCourseBaseDetailsDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/title/full-details")
    public ResponseEntity<CourseFullDetailsDto> courseByTitle(@RequestParam(name = "title") String title, Authentication loggedUser) {
        CourseFullDetailsDto course = courseService.getCourseByTitle(title, loggedUser);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> update(@PathVariable(name = "courseId") int courseId, @RequestBody @Valid CourseBaseDetailsDto courseBaseDetailsDto, BindingResult errors, Authentication loggedUser) {
        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        Course courseToUpdate = mapper.fromCourseBaseDetailsDtoToCourse(courseBaseDetailsDto);
        Course updatedCourse = courseService.update(courseId, courseToUpdate, loggedUser);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @GetMapping("/all-public/basic-details")
    public ResponseEntity<Set<CourseBaseDetailsDto>> getAllPublic(){
        Set<Course> publicCourses = courseService.getAllPublic();
        Set<CourseBaseDetailsDto> result = new HashSet<>();
        for (Course course : publicCourses) {
            result.add(mapper.fromCourseToCourseBaseDetailsDto(course));
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
