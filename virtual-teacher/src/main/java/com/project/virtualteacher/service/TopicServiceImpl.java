package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.TopicDao;
import com.project.virtualteacher.entity.Topic;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.service.contracts.TopicService;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicDao topicDao;
    private final ValidatorHelper validator;

    public TopicServiceImpl(TopicDao topicDao, ValidatorHelper validator) {
        this.topicDao = topicDao;
        this.validator = validator;
    }

    @Override
    @Transactional
    public Topic create(Topic topic, User user) {
        validator.throwIfNotTeacherOrAdmin(user);
        return topicDao.create(topic);
    }

    @Override
    @Transactional
    public Topic update(int id, Topic topicUpdate, User loggedUser) {
        validator.throwIfNotTeacherOrAdmin(loggedUser);
        Topic topicToUpdate = topicDao.getById(id).orElseThrow(()-> new EntityNotExistException(ErrorMessage.TOPIC_ID_NOT_EXIST,id));
        topicToUpdate.setTopic(topicUpdate.getTopic());
        return topicDao.update(topicToUpdate);
    }

    @Override
    public Topic getById(int topicId) {
        return topicDao.getById(topicId).orElseThrow(()->new EntityNotExistException(ErrorMessage.TOPIC_ID_NOT_EXIST,topicId));
    }
}
