package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.CourseDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> course(@RequestBody CourseDto courseDto, Authentication loggedUser) {
        Course courseToCreate = mapper.courseDtoToCourse(courseDto);
        Course createdCourse = courseService.createCourse(courseToCreate, loggedUser);
        return new ResponseEntity<>("Course with title: " + createdCourse.getTitle() + " was created", HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> course(@PathVariable(name = "courseId") int courseId, Authentication loggedUser) {
        Course course = courseService.getCourseById(courseId,loggedUser);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<Course> courseByTitle(@RequestParam(name = "title") String title, Authentication loggedUser){
        Course course = courseService.getCourseByTitle(title,loggedUser);
        return new ResponseEntity<>(course,HttpStatus.OK);
    }
}
