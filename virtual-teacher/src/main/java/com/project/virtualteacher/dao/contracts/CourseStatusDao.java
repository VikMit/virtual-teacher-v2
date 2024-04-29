package com.project.virtualteacher.dao.contracts;

import com.project.virtualteacher.entity.CourseStatus;

import java.util.Optional;

public interface CourseStatusDao {

    Optional<CourseStatus> findCourseStatusByValue(String value);
}
