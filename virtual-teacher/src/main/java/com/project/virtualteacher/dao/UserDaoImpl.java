package com.project.virtualteacher.dao;


import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.UserNotFoundException;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private final EntityManager em;

    @Autowired
    public UserDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        TypedQuery<User> query = em.createQuery("FROM User where username=:username", User.class);
        query.setParameter("username", username);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void create(User user) {
        em.persist(user);
    }

 /*   @Override
    public Optional<Student> findById(int userId) {
        Student user = entityManager.find(Student.class, userId);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }*/

    @Override
    public void delete(User user) {
        em.remove(user);
    }

    @Override
    public User update(User user) {
        return em.merge(user);
    }

    @Override
    public boolean isEmailExist(String email) {
        TypedQuery<User> query = em.createQuery("FROM User WHERE email=:email", User.class);
        query.setParameter("email", email);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public boolean isUsernameExist(String username) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM User WHERE username=:username", Long.class);
        query.setParameter("username", username);
        Long i = query.getSingleResult();
        return i > 0;
    }

    @Override
    public void blockUser(int userId) {
        Query query = em.createQuery("UPDATE  User u SET u.isBlocked = true WHERE id=:userId");
        query.setParameter("userId", userId);
        int result = query.executeUpdate();
        if (result < 1) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND, userId);
        }
    }

    @Override
    public void unBlockUser(int userId) {
        Query query = em.createQuery("UPDATE  User u SET u.isBlocked = false WHERE id=:userId");
        query.setParameter("userId", userId);
        int result = query.executeUpdate();
        if (result < 1) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND, userId);
        }
    }

    @Override
    public Optional<User> getById(int userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
