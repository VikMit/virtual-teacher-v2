package com.project.virtualteacher.dao.contracts;

import com.project.virtualteacher.dto.CourseSearchCriteria;
import com.project.virtualteacher.dto.PaginationResult;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.EnrollStudent;
import com.project.virtualteacher.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseDao {

    Course createCourse(Course course);

    PaginationResult<Course> getCoursesByTitle(String title, int page, int size);
    PaginationResult<Course> getEnrolledCoursesByTitle(String title, int page, int size, Student student);

    Optional<Course> getCourseById(int id);

    boolean isCourseTitleExist(String title);

    void delete(Course course);

    Course update(Course course);

    Optional<Course> getPublicCourseById(int courseId);

   // List<Course> getPublicCourseByTitle(String title);

    Set<Course> getAllPublic();

    Set<Course> getAll(int page,int size);

    void enrollUserForCourse(EnrollStudent student);

    Optional<Course> getCourseByLectureId(int lectureId);

    Set<Course> getCoursesByTopic(String topic);

    PaginationResult<Course> getAllBySearchCriteria(CourseSearchCriteria searchCriteria, int page, int size);
}

