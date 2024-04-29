package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.CourseStatusDao;
import com.project.virtualteacher.entity.CourseStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseStatusDaoImpl implements CourseStatusDao {

    private final EntityManager em;

    public CourseStatusDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<CourseStatus> findCourseStatusByValue(String value) {
        TypedQuery<CourseStatus> query = em.createQuery("FROM CourseStatus WHERE value = :value", CourseStatus.class);
        query.setParameter("value",value);
        try{
            return Optional.of(query.getSingleResult());
        }
        catch (NoResultException ex){
            return Optional.empty();
        }
    }
}
