package com.project.virtualteacher.utility;

import com.project.virtualteacher.dao.contracts.*;
import com.project.virtualteacher.dto.*;
import com.project.virtualteacher.entity.*;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class Mapper {

    private final RoleDao roleDao;
    private final CourseDao courseDao;
    private final TopicDao topicDao;


    public Mapper(RoleDao roleDao, UserDao userDao, CourseDao courseDao, TopicDao topicDao) {
        this.roleDao = roleDao;
        this.courseDao = courseDao;
        this.topicDao = topicDao;
    }

    public User fromUserCreateDtoToUser(UserCreateDto userCreateDto) {
        User user = fromUserUpdateDtoToUser(userCreateDto);
        Role role = roleDao.findById(userCreateDto.getRoleId()).orElseThrow(() -> new EntityNotExistException(ErrorMessage.ROLE_ID_NOT_FOUND, userCreateDto.getRoleId()));
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setEmail(userCreateDto.getEmail());
        user.setRole(role);
        if (userCreateDto.getPictureUrl() == null) {
            user.setPictureUrl("Default picture URL");
        } else {
            user.setPictureUrl(userCreateDto.getPictureUrl());
        }
        return user;
    }

    public UserOutDto fromUserToUserOutDto(User user) {
        UserOutDto userOutDto = new UserOutDto();
        userOutDto.setUserId(user.getId());
        userOutDto.setUsername(user.getUsername());
        userOutDto.setEmail(user.getEmail());
        userOutDto.setFirstName(user.getFirstName());
        userOutDto.setLastName(user.getLastName());
        userOutDto.setPictureUrl(user.getPictureUrl());
        userOutDto.setRole(user.getRole());
        return userOutDto;
    }

    public User fromUserUpdateDtoToUser(UserUpdateDto userUpdateDto) {
        User userToUpdate = new User();
        userToUpdate.setFirstName(userUpdateDto.getFirstName());
        userToUpdate.setLastName(userUpdateDto.getLastName());
        userToUpdate.setDob(userUpdateDto.getDob());
        return userToUpdate;
    }

    public Course fromCourseCreateDtoToCourse(CourseCreateDto courseCreateDto) {
        Course course = new Course();
        course.setDescription(courseCreateDto.getDescription());
        course.setPublished(courseCreateDto.getIsPublished());
        course.setPassingGrade(courseCreateDto.getPassingGrade());
        course.setTitle(courseCreateDto.getTitle());
        course.setStartDate(courseCreateDto.getStartDate());
        Set<Lecture> lectures = new HashSet<>();
        course.setLectures(lectures);
        Set<Topic> topics = new HashSet<>();
        courseCreateDto.getTopics().forEach(topic -> topics.add(topicDao.getById(topic)
                .orElseThrow(() -> new EntityNotExistException(ErrorMessage.TOPIC_ID_NOT_EXIST, topic))));
        course.setTopics(topics);
        return course;
    }

    public Course fromCourseBaseOutDtoToCourse(CourseBaseOutDto courseBaseOutDto) {
        Course course = new Course();
        course.setTitle(courseBaseOutDto.getTitle());
        course.setStartDate(courseBaseOutDto.getStartDate());
        course.setPublished(courseBaseOutDto.getIsPublished());
        course.setPassingGrade(courseBaseOutDto.getPassingGrade());
        course.setDescription(courseBaseOutDto.getDescription());
        return course;
    }

    public CourseBaseOutDto fromCourseToCourseBaseOutDto(Course updatedCourse) {
        CourseBaseOutDto result = new CourseBaseOutDto();
        result.setCreator(fromUserToUserOutDto(updatedCourse.getTeacher()));
        result.setIsPublished(updatedCourse.isPublished());
        result.setDescription(updatedCourse.getDescription());
        result.setTitle(updatedCourse.getTitle());
        result.setStartDate(updatedCourse.getStartDate());
        result.setPassingGrade(updatedCourse.getPassingGrade());
        return result;
    }

    public CourseFullOutDto fromCourseToCourseFullOutDto(Course course) {
        CourseFullOutDto result = new CourseFullOutDto();
        result.setId(course.getId());
        result.setCreator(fromUserToUserOutDto(course.getTeacher()));
        result.setIsPublished(course.isPublished());
        result.setDescription(course.getDescription());
        result.setPassingGrade(course.getPassingGrade());
        result.setTitle(course.getTitle());
        result.setStartDate(course.getStartDate());
        result.setTopics(course.getTopics());
        result.setLectures(course.getLectures());
        result.setEnrolledStudents(course.getEnrolledStudents().stream().map(this::fromUserToUserOutDto).collect(Collectors.toSet()));
        return result;
    }

    public Role fromRoleCreateDtoToRole(RoleCreateDto roleCreateDto) {
        Role role = new Role();
        role.setValue(roleCreateDto.getValue());
        return role;
    }

    public Lecture fromLectureFullDtoToLecture(LectureFullDto lectureFullDetailsDto) {
        Lecture lecture = fromLectureBaseDtoToLecture(lectureFullDetailsDto);
        lecture.setId(lecture.getId());
        lecture.setVideoUrl(lectureFullDetailsDto.getVideoUrl());
        lecture.setAssignmentUrl(lectureFullDetailsDto.getAssignmentUrl());
        return lecture;
    }

    public Lecture fromLectureBaseDtoToLecture(LectureBaseDto lectureBaseDto) {
        Lecture lecture = new Lecture();
        lecture.setId(lectureBaseDto.getId());
        lecture.setTitle(lectureBaseDto.getTitle());
        lecture.setDescription(lectureBaseDto.getDescription());
        if (lectureBaseDto.getId() != null) {
            Course course = courseDao.getCourseById(lectureBaseDto.getCourseId()).orElseThrow(() -> new EntityNotExistException(ErrorMessage.LECTURE_NOT_FOUND_IN_COURSE, lectureBaseDto.getId()));
            lecture.setCourse(course);
        }
        return lecture;
    }

    public LectureFullDto fromLectureToLectureFullDto(Lecture lecture) {
        LectureFullDto result = new LectureFullDto();
        result.setId(lecture.getId());
        result.setCourseId(lecture.getCourse().getId());
        result.setVideoUrl(lecture.getVideoUrl());
        result.setAssignmentUrl(lecture.getAssignmentUrl());
        return result;
    }

    public LectureBaseDto fromLectureToLectureBaseDto(Lecture lecture) {
        LectureBaseDto result = new LectureBaseDto();
        result.setId(lecture.getId());
        result.setCourseId(lecture.getId());
        result.setDescription(lecture.getDescription());
        result.setTitle(lecture.getTitle());
        return result;
    }

    public Topic fromTopicDtoToTopic(TopicDto topicDto) {
        Topic topic = new Topic();
        topic.setTopic(topicDto.getTopic());
        return topic;
    }

    public TopicDto fromTopicToTopicDto(Topic updatedTopic) {
        TopicDto result = new TopicDto();
        result.setId(updatedTopic.getId());
        result.setTopic(updatedTopic.getTopic());
        return result;
    }

    public StudentOutDto fromStudentToStudentOutDto(Student student) {
        return new StudentOutDto(student);
    }

    public TeacherOutDto fromTeacherToTeacherOutDto(Teacher teacher) {
        return new TeacherOutDto(teacher);
    }
}
