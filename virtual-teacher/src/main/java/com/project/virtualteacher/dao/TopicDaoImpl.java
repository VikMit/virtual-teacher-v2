package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.TopicDao;
import com.project.virtualteacher.entity.Topic;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityExistException;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TopicDaoImpl implements TopicDao {
    private final EntityManager em;

    public TopicDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Topic create(Topic topic) {
        try {
            em.persist(topic);
            return topic;
        }
        catch (EntityExistsException e){
            throw new EntityExistException(ErrorMessage.TOPIC_NAME_EXIST,topic.getTopic());
        }
    }

    @Override
    public void delete(Topic topic) {
        em.remove(topic);
    }

    @Override
    public Optional<Topic> getByValue(String value) {
        TypedQuery<Topic> query = em.createQuery("FROM Topic t WHERE t.topic = :topic",Topic.class);
        query.setParameter("topic",value);
        try{

            Topic result =  query.getSingleResult();
            return Optional.ofNullable(result);
        }
        catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Topic> getById(int id) {
        Topic topic = em.find(Topic.class,id);
        if (topic == null){
            return Optional.empty();
        }
        return Optional.of(topic);
    }
}
