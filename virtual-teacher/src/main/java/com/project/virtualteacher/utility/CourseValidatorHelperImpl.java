package com.project.virtualteacher.utility;

import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.utility.contracts.CourseValidatorHelper;
import org.springframework.stereotype.Component;

@Component
public class CourseValidatorHelperImpl implements CourseValidatorHelper {

    public void throwIfNotCourseCreator(Course course, User loggedUser) {
        if (!course.getTeacher().getUsername().equals(loggedUser.getUsername())) {
            throw new UnAuthorizeException(ErrorMessage.NOT_COURSE_CREATOR_ERROR);
        }
    }
    public void throwIfLectureTitleExistInCourse(Course course, String title) {
        boolean titleExist = course.getLectures().stream().anyMatch(lecture -> lecture.getTitle().equalsIgnoreCase(title));
        if (titleExist) {
            throw new EntityExistException(ErrorMessage.LECTURE_TITLE_EXIST, title);
        }
    }
}
