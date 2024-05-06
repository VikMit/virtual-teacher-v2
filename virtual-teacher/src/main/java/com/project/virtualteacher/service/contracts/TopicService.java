package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.Topic;
import com.project.virtualteacher.entity.User;

public interface TopicService {

    Topic create(Topic topic, User user);

    Topic update(int id, Topic topicUpdate, User loggedUser);
}
