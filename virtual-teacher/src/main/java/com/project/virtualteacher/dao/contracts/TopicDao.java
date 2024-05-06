package com.project.virtualteacher.dao.contracts;

import com.project.virtualteacher.entity.Topic;

import java.util.Optional;

public interface TopicDao {
    Topic create(Topic topic);

    void delete(Topic topic);

    Optional<Topic> getByValue(String value);

    Optional<Topic> getById(int id);
}
