package com.project.virtualteacher.utility.contracts;

import com.project.virtualteacher.dto.*;
import com.project.virtualteacher.entity.*;

public interface Mapper {
     User fromUserCreateDtoToUser(UserCreateDto userCreateDto);

     UserOutDto fromUserToUserOutDto(User user);

     User fromUserUpdateDtoToUser(UserUpdateDto userUpdateDto);

     Course fromCourseCreateDtoToCourse(CourseCreateDto courseCreateDto);

     Course fromCourseBaseOutDtoToCourse(CourseBaseOutDto courseBaseOutDto);

     CourseBaseOutDto fromCourseToCourseBaseOutDto(Course course);

     CourseFullOutDto fromCourseToCourseFullOutDto(Course course);

     Role fromRoleCreateDtoToRole(RoleCreateDto roleCreateDto);

     Lecture fromLectureFullDtoToLecture(LectureFullDto lectureFullDetailsDto);

     Lecture fromLectureBaseDtoToLecture(LectureBaseDto lectureBaseDto);

     LectureFullDto fromLectureToLectureFullDto(Lecture lecture);

     LectureBaseDto fromLectureToLectureBaseDto(Lecture lecture);

     Topic fromTopicDtoToTopic(TopicDto topicDto);

     TopicDto fromTopicToTopicDto(Topic updatedTopic);

     StudentOutDto fromStudentToStudentOutDto(Student student);

     TeacherOutDto fromTeacherToTeacherOutDto(Teacher teacher);

     PaginationResult<CourseFullOutDto> fromCourseToCourseFullOutPaged(PaginationResult<Course> courses);
}
