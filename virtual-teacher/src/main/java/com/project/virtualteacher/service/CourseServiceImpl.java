package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.dto.CourseFullDetailsDto;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.CourseNotFoundException;
import com.project.virtualteacher.exception_handling.exceptions.CourseTitleExistException;
import com.project.virtualteacher.exception_handling.exceptions.UserNotFoundException;
import com.project.virtualteacher.service.contracts.CourseService;
import com.project.virtualteacher.utility.Mapper;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;
    private final UserDao userDao;
    private final ValidatorHelper validator;
    private final Mapper mapper;

    public CourseServiceImpl(CourseDao courseDao, UserDao userDao, ValidatorHelper validator, Mapper mapper) {
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Course create(Course course, Authentication loggedUser) {
        if (!courseDao.isCourseTitleExist(course.getTitle())) {
            String username = loggedUser.getName();
            User creator = userDao.getByUsername(username).orElseThrow(() -> new UserNotFoundException(ErrorMessage.USERNAME_NOT_FOUND, username));
            course.setTeacher(creator);
            return courseDao.createCourse(course);
        } else {
            throw new CourseTitleExistException(ErrorMessage.COURSE_TITLE_EXIST, course.getTitle());
        }
    }

    @Override
    public Course getCourseById(int courseId, Authentication loggedUser) {
        Course course = courseDao.getCourseById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseId));

        if (course.isPublished() || validator.isTeacherOrAdmin(loggedUser)) {
            return course;
        }
        throw new CourseNotFoundException(ErrorMessage.PUBLIC_COURSE_WITH_ID_NOT_FOUND, courseId);
    }

    @Override
    public Course getPublicCourseById(int courseId) {
        return courseDao.getPublicCourseById(courseId).orElseThrow(()->new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND,courseId));
    }

    @Override
    public CourseFullDetailsDto getCourseByTitle(String title, Authentication loggedUser) {
        Course course = courseDao.getCourseByTitle(title)
                .orElseThrow(() -> new CourseNotFoundException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND, title));

        if (course.isPublished() || validator.isTeacherOrAdmin(loggedUser)) {
            return mapper.fromCourseToCourseFullDetailsDto(course);
        }

        throw new CourseNotFoundException(ErrorMessage.PUBLIC_COURSE_WITH_TITLE_NOT_FOUND, title);
    }

    @Override
    public Course getPublicCourseByTitle(String title) {
        return courseDao.getPublicCourseByTitle(title).orElseThrow(()->new CourseNotFoundException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND,title));
    }

    @Override
    public Set<Course> getAllPublic() {
        return courseDao.getAllPublic();
    }

    @Override
    @Transactional
    public Course update(int courseToUpdateId, Course updateCourse, Authentication loggedUser) {
        Course courseToUpdate = courseDao.getCourseById(courseToUpdateId).orElseThrow(()-> new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, courseToUpdateId));
        validator.isCreatorOfCourse(courseToUpdate,loggedUser);
        validateTitleUniqueIfChanged(updateCourse,courseToUpdate);
        applyChanges(courseToUpdate,updateCourse);
        return courseDao.update(courseToUpdate);
    }

   /* @Override
    public CourseBaseDetailsDto getCourseBasicDetailsById(int courseId) {
        Course courseFullDetails = courseDao.getCourseById(courseId).orElseThrow(()->new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND,courseId));
        return mapper.fromCourseToCourseBaseDetailsDto(courseFullDetails);
    }

    @Override
    public CourseBaseDetailsDto getCourseBasicDetailsByTitle(String title) {
        Course courseFullDetails = courseDao.getCourseByTitle(title).orElseThrow(()->new CourseNotFoundException(ErrorMessage.COURSE_WITH_TITLE_NOT_FOUND,title));
        return mapper.fromCourseToCourseBaseDetailsDto(courseFullDetails);    }*/

    private void validateTitleUniqueIfChanged(Course updateCourse, Course courseToUpdate){
        if (!courseToUpdate.getTitle().equals(updateCourse.getTitle())){
            if ((courseDao.getCourseByTitle(updateCourse.getTitle()).isPresent())){
                throw new CourseTitleExistException(ErrorMessage.COURSE_TITLE_EXIST,updateCourse.getTitle());
            }
        }
    }

    private void applyChanges(Course mainCourse, Course updateDetails){
        mainCourse.setTitle(updateDetails.getTitle());
        mainCourse.setStartDate(updateDetails.getStartDate());
        mainCourse.setPublished(updateDetails.isPublished());
        mainCourse.setPassingGrade(updateDetails.getPassingGrade());
        mainCourse.setDescription(updateDetails.getDescription());
    }


}
