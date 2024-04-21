package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.entity.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseDaoImpl implements CourseDao {
    private final EntityManager em;

    public CourseDaoImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public Course createCourse(Course course) {
        em.persist(course);
        return course;
    }

    @Override
    public Optional<Course> getCourseByTitle(String title) {
        TypedQuery<Course> query = em.createQuery("FROM Course c WHERE c.title = :title",Course.class);
        query.setParameter("title",title);
        try {
            return Optional.of(query.getSingleResult());
        }catch (NoResultException e){
            return  Optional.empty();
        }
    }

    @Override
    public Optional<Course> getCourseById(int id) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE id = :id",Course.class);
        query.setParameter("id",id);
        try {
            return Optional.of(query.getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean isCourseTitleExist(String title) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM Course WHERE title =:title", Long.class);
        query.setParameter("title",title);
        Long result = query.getSingleResult();
        return result>0;
    }
}
