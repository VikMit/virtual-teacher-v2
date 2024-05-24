package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.dao.contracts.TopicDao;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Topic;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.service.contracts.TopicService;
import com.project.virtualteacher.utility.contracts.UserValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicDao topicDao;
    private final UserValidatorHelper userValidator;
    private final CourseDao courseDao;

    public TopicServiceImpl(TopicDao topicDao, UserValidatorHelper userValidator, CourseDao courseDao) {
        this.topicDao = topicDao;
        this.userValidator = userValidator;
        this.courseDao = courseDao;
    }

    @Override
    @Transactional
    public Topic create(Topic topic, User user) {
        userValidator.throwIfNotTeacherOrAdmin(user);
        return topicDao.create(topic);
    }

    @Override
    @Transactional
    public Topic update(int id, Topic topicUpdate, User loggedUser) {
        userValidator.throwIfNotTeacherOrAdmin(loggedUser);
        Topic topicToUpdate = topicDao.getById(id).orElseThrow(() -> new EntityNotExistException(ErrorMessage.TOPIC_ID_NOT_EXIST, id));
        topicToUpdate.setTopic(topicUpdate.getTopic());
        return topicDao.update(topicToUpdate);
    }

    @Override
    public Topic getById(int topicId) {
        return topicDao.getById(topicId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.TOPIC_ID_NOT_EXIST, topicId));
    }

    @Override
    @Transactional
    public void delete(int id,User loggedUser) {
        userValidator.throwIfNotTeacherOrAdmin(loggedUser);
        Topic topicToDelete = topicDao.getById(id).orElseThrow(() -> new EntityNotExistException(ErrorMessage.TOPIC_ID_NOT_EXIST, id));
        Set<Course> courses = courseDao.getCoursesByTopic(topicToDelete.getTopic());
        removeTopicFromCourses(topicToDelete, courses);
        topicDao.delete(topicToDelete);
    }

    private void removeTopicFromCourses(Topic topic, Set<Course> courses) {
        courses.forEach(course -> {
            course.getTopics().remove(topic);
            courseDao.update(course);
        });
    }
}
