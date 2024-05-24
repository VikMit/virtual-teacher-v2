package com.project.virtualteacher.utility.contracts;

import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.User;

public interface CourseValidatorHelper {
     void throwIfNotCourseCreator(Course course, User loggedUser);

     void throwIfLectureTitleExistInCourse(Course course, String title);
}
