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
    private final LectureDao lectureDao;
    private final TopicDao topicDao;


    public Mapper(RoleDao roleDao, UserDao userDao, CourseDao courseDao, LectureDao lectureDao, TopicDao topicDao, UserDao userDao1) {
        this.roleDao = roleDao;
        this.courseDao = courseDao;
        this.lectureDao = lectureDao;
        this.topicDao = topicDao;
    }

    public User fromUserFullDetailsInDtoToUser(UserFullDetailsInDto detailedUserInDto) {
        User user = fromUserBaseDetailsInDtoToUser(detailedUserInDto);
        Role role = roleDao.findById(detailedUserInDto.getRoleId()).orElseThrow();
        user.setUsername(detailedUserInDto.getUsername());
        user.setPassword(detailedUserInDto.getPassword());
        user.setEmail(detailedUserInDto.getEmail());
        user.setRole(role);
        if (user.getPictureUrl() == null) {
            user.setPictureUrl("Default picture URL");
        } else {
            user.setPictureUrl(detailedUserInDto.getPictureUrl());
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

    public User fromUserBaseDetailsInDtoToUser(UserBaseDetailsInDto userBaseDetailsInDto) {
        User userToUpdate = new User();
        userToUpdate.setFirstName(userBaseDetailsInDto.getFirstName());
        userToUpdate.setLastName(userBaseDetailsInDto.getLastName());
        userToUpdate.setDob(userBaseDetailsInDto.getDob());
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

    public Course fromCourseBaseDetailsOutDtoToCourse(CourseBaseDetailsOutDto courseBaseDetailsOutDto) {
        Course course = new Course();
        course.setTitle(courseBaseDetailsOutDto.getTitle());
        course.setStartDate(courseBaseDetailsOutDto.getStartDate());
        course.setPublished(courseBaseDetailsOutDto.getIsPublished());
        course.setPassingGrade(courseBaseDetailsOutDto.getPassingGrade());
        course.setDescription(courseBaseDetailsOutDto.getDescription());
        return course;
    }

    public CourseBaseDetailsOutDto fromCourseToCourseBaseDetailsOutDto(Course updatedCourse) {
        CourseBaseDetailsOutDto result = new CourseBaseDetailsOutDto();
        result.setCreator(fromUserToUserOutDto(updatedCourse.getTeacher()));
        result.setIsPublished(updatedCourse.isPublished());
        result.setDescription(updatedCourse.getDescription());
        result.setTitle(updatedCourse.getTitle());
        result.setStartDate(updatedCourse.getStartDate());
        result.setPassingGrade(updatedCourse.getPassingGrade());
        return result;
    }

    public CourseFullDetailsOutDto fromCourseToCourseFullDetailsOutDto(Course course) {
        CourseFullDetailsOutDto result = new CourseFullDetailsOutDto();
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

    public Role fromRoleCreateDtoInToRole(RoleCreateDto roleCreateDto) {
        Role role = new Role();
        role.setValue(roleCreateDto.getValue());
        return role;
    }

    public Lecture fromLectureFullDetailsDtoToLecture(LectureFullDetailsDto lectureFullDetailsDto) {
        Lecture lecture = fromLectureBaseDetailsDtoToLecture(lectureFullDetailsDto);
        lecture.setId(lecture.getId());
        lecture.setVideoUrl(lectureFullDetailsDto.getVideoUrl());
        lecture.setAssignmentUrl(lectureFullDetailsDto.getAssignmentUrl());
        return lecture;
    }

    public Lecture fromLectureBaseDetailsDtoToLecture(LectureBaseDetailsDto lectureBaseDetailsDto) {
        Lecture lecture = new Lecture();
        lecture.setId(lectureBaseDetailsDto.getId());
        lecture.setTitle(lectureBaseDetailsDto.getTitle());
        lecture.setDescription(lectureBaseDetailsDto.getDescription());
        if (lectureBaseDetailsDto.getId() != null) {
            Course course = courseDao.getCourseById(lectureBaseDetailsDto.getCourseId()).orElseThrow(() -> new EntityNotExistException(ErrorMessage.LECTURE_NOT_FOUND_IN_COURSE, lectureBaseDetailsDto.getId()));
            lecture.setCourse(course);
        }
        return lecture;
    }

    public LectureFullDetailsDto fromLectureToLectureFullDetailsDto(Lecture lecture) {
        LectureFullDetailsDto result = new LectureFullDetailsDto();
        result.setId(lecture.getId());
        result.setCourseId(lecture.getCourse().getId());
        result.setVideoUrl(lecture.getVideoUrl());
        result.setAssignmentUrl(lecture.getAssignmentUrl());
        return result;
    }

    public LectureBaseDetailsDto fromLectureToLectureBaseDetailsDto(Lecture lecture) {
        LectureBaseDetailsDto result = new LectureBaseDetailsDto();
        result.setId(lecture.getId());
        result.setCourseId(lecture.getId());
        result.setDescription(lecture.getDescription());
        result.setTitle(lecture.getTitle());
        return result;
    }
    public Topic fromTopicDtoToTopic(TopicDto topicDto){
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
}
