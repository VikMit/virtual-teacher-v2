package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.CourseNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        TypedQuery<Course> query = em.createQuery("FROM Course c WHERE c.title = :title", Course.class);
        query.setParameter("title", title);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Course> getCourseById(int id) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE id = :id", Course.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isCourseTitleExist(String title) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM Course WHERE title =:title", Long.class);
        query.setParameter("title", title);
        Long result = query.getSingleResult();
        return result > 0;
    }

    @Override
    public void delete(Course course) {
        try {
            em.detach(course);
        } catch (IllegalArgumentException e) {
            throw new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, course.getId());
        }
    }

    @Override
    public Course update(Course course) {
        try {
            return em.merge(course);
        } catch (IllegalArgumentException e) {
            throw new CourseNotFoundException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, course.getId());
        }
    }

    @Override
    public Optional<Course> getPublicCourseById(int courseId) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE id = :courseId AND isPublished = true", Course.class);
        query.setParameter("courseId", courseId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Course> getPublicCourseByTitle(String title) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE title = :title AND isPublished = true", Course.class);
        query.setParameter("title", title);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<Course> getAllPublic() {
        TypedQuery<Course> published = em.createQuery("FROM Course WHERE isPublished = true",Course.class);
        return new HashSet<>(published.getResultList());
    }
}
