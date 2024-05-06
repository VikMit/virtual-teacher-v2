package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.TopicDao;
import com.project.virtualteacher.entity.Topic;
import com.project.virtualteacher.entity.User;
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
}
