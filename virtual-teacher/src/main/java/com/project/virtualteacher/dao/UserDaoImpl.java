package com.project.virtualteacher.dao;


import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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
    public Optional<User> findByUsename(String username) {
        TypedQuery<User> query = em.createQuery("FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

  /*  @Override
    public Optional<Student> findStudentByUsername(String username){
        TypedQuery<Student> query = em.createQuery("FROM Student WHERE username = :username",Student.class);
        query.setParameter("username",username);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }*/

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void delete(User user) {
        em.remove(user);
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public boolean isEmailExist(String email) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM User WHERE email=:email", Long.class);
        query.setParameter("email", email);
        Long result = query.getSingleResult();
        return result > 0;
    }

    @Override
    public boolean isUsernameExist(String username) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM User WHERE username=:username", Long.class);
        query.setParameter("username", username);
        Long result = query.getSingleResult();
        return result > 0;
    }

    @Override
    public void block(int userId) {
        Query query = em.createQuery("UPDATE  User u SET u.isBlocked = true WHERE id=:userId");
        query.setParameter("userId", userId);
        int result = query.executeUpdate();
        if (result < 1) {
            throw new EntityNotExistException(ErrorMessage.USER_ID_NOT_FOUND, userId);
        }
    }

    @Override
    public void unblock(int userId) {
        Query query = em.createQuery("UPDATE  User u SET u.isBlocked = false WHERE id=:userId");
        query.setParameter("userId", userId);
        int result = query.executeUpdate();
        if (result < 1) {
            throw new EntityNotExistException(ErrorMessage.USER_ID_NOT_FOUND, userId);
        }
    }

    @Override
    public void verifyEmail(String code) {
        Query query = em.createQuery("UPDATE User u SET u.isEmailVerified = true WHERE emailCode LIKE :code");
        query.setParameter("code",code);
        int result = query.executeUpdate();
        if (result<1){
            throw new EntityNotExistException("Verification declined.");
        }
    }

    @Override
    public Optional<User> findById(int userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
