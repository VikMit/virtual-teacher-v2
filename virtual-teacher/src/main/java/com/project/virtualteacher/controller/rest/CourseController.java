package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.*;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.MapperImpl;
import com.project.virtualteacher.utility.UserValidatorHelperImpl;
import com.project.virtualteacher.utility.contracts.Mapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService courseService;
    private final Mapper mapper;
    private final UserValidatorHelperImpl userValidatorHelperImpl;
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_SIZE = "10";

    public CourseController(CourseService courseService, MapperImpl mapperImpl, Mapper mapper, UserValidatorHelperImpl userValidatorHelperImpl) {
        this.courseService = courseService;
        this.mapper = mapper;
        this.userValidatorHelperImpl = userValidatorHelperImpl;
    }

    @PostMapping()
    public ResponseEntity<CourseFullOutDto> course(@RequestBody CourseCreateDto courseCreateDto, Authentication loggedUser) {
        Course courseToCreate = mapper.fromCourseCreateDtoToCourse(courseCreateDto);
        User user = userValidatorHelperImpl.extractUserFromAuthentication(loggedUser);
        Course createdCourse = courseService.create(courseToCreate, user);
        CourseFullOutDto courseFullDetailsOutDto = mapper.fromCourseToCourseFullOutDto(createdCourse);
        return new ResponseEntity<>(courseFullDetailsOutDto, HttpStatus.CREATED);
    }

    //TESTED with POSTMAN
    @GetMapping("/{courseId}/basic-details")
    public ResponseEntity<CourseBaseOutDto> courseBasicDetailsById(@PathVariable(name = "courseId") @Min(value = 1, message = "Course ID must be positive Integer.") int courseId,
                                                                   Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseById(courseId, loggedUser);
        CourseBaseOutDto result = mapper.fromCourseToCourseBaseOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //TESTED with POSTMAN
 /*   @GetMapping("/basic-details")
    public ResponseEntity<PaginationResult<CourseBaseOutDto>> courseBaseDetailsBySearchCriteria(@RequestBody() CourseSearchCriteria searchCriteria,
                                                                           @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) @Min(value = 1, message = "Page must be positive integer greater or equal to 1") int page,
                                                                           @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) @Min(value = 1, message = "Size must be positive integer greater or equal to 1") int size,
                                                                           Authentication authentication)
    {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        PaginationResult<Course> courses = courseService.getCoursesBySearchCriteria(searchCriteria, loggedUser, page, size);
        Set<CourseBaseOutDto> result = new HashSet<>();
        courses.forEach(course -> result.add(mapper.fromCourseToCourseBaseOutDto(course)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/

    //TESTED with POSTMAN
    @GetMapping("/{courseId}/full-details")
    public ResponseEntity<CourseFullOutDto> courseFullDetailsById(@PathVariable(name = "courseId") @Min(value = 1, message = "Course ID must be positive Integer.") int courseId, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        Course course = courseService.getCourseById(courseId, loggedUser);
        CourseFullOutDto result = mapper.fromCourseToCourseFullOutDto(course);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/full-details")
    public ResponseEntity<PaginationResult<CourseFullOutDto>> courseByTitle(@RequestBody CourseSearchCriteria courseSearchCriteria,
                                                                            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) @Min(value = 1, message = "Page must be positive integer greater or equal to 1") int page,
                                                                            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) @Min(value = 1, message = "Size must be positive integer greater or equal to 1") int size,
                                                                            Authentication authentication) {

        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        PaginationResult<Course> courses = courseService.getCoursesBySearchCriteria(courseSearchCriteria, loggedUser, page, size);
        PaginationResult<CourseFullOutDto> result = new PaginationResult<>();
        result = mapper.fromCourseToCourseFullOutPaged(courses);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> update(@PathVariable(name = "courseId") int courseId, @RequestBody @Valid CourseCreateDto courseCreateDto, BindingResult errors, Authentication authentication) {
        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        Course newCourse = mapper.fromCourseCreateDtoToCourse(courseCreateDto);
        Course updatedCourse = courseService.update(courseId, newCourse, loggedUser);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

   /* @GetMapping("/all-public/basic-details")
    public ResponseEntity<Set<CourseBaseOutDto>> getAllPublicWithBaseDetails() {
        Set<Course> allCourses = courseService.getAllPublic();
        Set<CourseBaseOutDto> extractedCourseBaseDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseBaseOutDto b = mapper.fromCourseToCourseBaseOutDto(course);
            extractedCourseBaseDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseBaseDetails, HttpStatus.OK);
    }*/

   /* @GetMapping("/all/full")
    public ResponseEntity<Set<CourseFullOutDto>> getAllWithFullDetails(@RequestParam(name = "page", required = false, defaultValue = "1") @Min(value = 1, message = "Page must be a positive integer") int page,
                                                                       @RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1, message = "Size must be a positive integer") int size, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Set<Course> allCourses = courseService.getAll(loggedUser, page, size);
        Set<CourseFullOutDto> extractedCourseFullDetails = new HashSet<>();
        allCourses.forEach((course) -> {
            CourseFullOutDto b = mapper.fromCourseToCourseFullOutDto(course);
            extractedCourseFullDetails.add(b);
        });
        return new ResponseEntity<>(extractedCourseFullDetails, HttpStatus.OK);
    }*/

    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> delete(@PathVariable(name = "courseId") @Min(value = 1, message = "Course ID must be positive Integer.") int courseId,
                                         Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        courseService.delete(courseId, loggedUser);
        return new ResponseEntity<>("Course with ID: " + courseId + " was deleted", HttpStatus.OK);
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enroll(@PathVariable(name = "courseId") @Min(value = 1, message = "Course ID must be positive Integer.") int courseId,
                                         Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        courseService.enroll(courseId, loggedUser);
        return new ResponseEntity<>("User with username: " + loggedUser.getUsername() + " was enrolled for course with ID: " + courseId, HttpStatus.OK);
    }

}
