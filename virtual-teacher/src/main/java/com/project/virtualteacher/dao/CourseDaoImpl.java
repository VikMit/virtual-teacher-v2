package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.EnrollStudent;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.List;
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
        Course course = em.find(Course.class, id);
        if (course == null) {
            return Optional.empty();
        }
        return Optional.of(course);
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
            em.remove(course);
        } catch (IllegalArgumentException e) {
            throw new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, course.getId());
        }
    }

    @Override
    public Course update(Course course) {
        try {
            return em.merge(course);
        } catch (IllegalArgumentException e) {
            throw new EntityNotExistException(ErrorMessage.COURSE_WITH_ID_NOT_FOUND, course.getId());
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
        TypedQuery<Course> published = em.createQuery("FROM Course WHERE isPublished = true", Course.class);
        return new HashSet<>(published.getResultList());
    }

    @Override
    public Set<Course> getAll(int page, int size) {
        TypedQuery<Course> query = em.createQuery("FROM Course", Course.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public void enrollUserForCourse(EnrollStudent student) {
        em.persist(student);
    }

    @Override
    public Optional<Course> getCourseByLectureId(int lectureId) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE element(lectures).id = :lectureId", Course.class);
        query.setParameter("lectureId", lectureId);
        try {
            return Optional.of(query.getSingleResult());

        } catch (NoResultException e) {

            return Optional.empty();
        }
    }

    @Override
    public List<Course> getCoursesByTopic(String topic) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE element(topics).topic LIKE :topic", Course.class);
        query.setParameter("topic", topic);
        return query.getResultList();
    }
}
