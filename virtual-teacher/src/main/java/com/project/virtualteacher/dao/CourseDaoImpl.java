package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.dto.CourseSearchCriteria;
import com.project.virtualteacher.dto.PaginationResult;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.EnrollStudent;
import com.project.virtualteacher.entity.Student;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;

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
    public PaginationResult<Course> getCoursesByTitle(String title, int page, int size) {
        Long totalRecords;
        TypedQuery<Long> countRecords = em.createQuery("SELECT COUNT(*) FROM Course c WHERE c.title LIKE :title", Long.class);
        countRecords.setParameter("title", "%" + title + "%");
        totalRecords = countRecords.getSingleResult();
        TypedQuery<Course> query = em.createQuery("FROM Course c WHERE c.title LIKE :title ORDER BY c.id", Course.class);
        query.setParameter("title", "%" + title + "%");
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        Set<Course> coursesSet = new TreeSet<>(query.getResultList());
        PaginationResult<Course> result = new PaginationResult<>();
        result.setCurrentPage(page);
        result.setSize(size);
        result.setTotalRecords(totalRecords);
        result.setData(coursesSet);
        result.setLastPage(getLastPage(totalRecords, size));
        return result;
    }

    @Override
    public PaginationResult<Course> getEnrolledCoursesByTitle(String title, int page, int size, Student student) {
        Long totalRecords;
        TypedQuery<Long> countRecords = em.createQuery("SELECT COUNT(*) FROM Course c WHERE c.title LIKE :title AND element(enrolledStudents)=:student", Long.class);
        countRecords.setParameter("title", "%" + title + "%");
        countRecords.setParameter("student", student);
        totalRecords = countRecords.getSingleResult();
        TypedQuery<Course> query = em.createQuery("FROM Course c WHERE c.title LIKE :title AND element(enrolledStudents)=:student ORDER BY c.id", Course.class);
        query.setParameter("title", "%" + title + "%");
        query.setParameter("student", student);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        Set<Course> coursesSet = new TreeSet<>(query.getResultList());
        PaginationResult<Course> result = new PaginationResult<>();
        result.setCurrentPage(page);
        result.setSize(size);
        result.setTotalRecords(totalRecords);
        result.setData(coursesSet);
        result.setLastPage(getLastPage(totalRecords, size));
        return result;
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

 /*   @Override
    public List<Course> getPublicCourseByTitle(String title) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE title LIKE :title AND isPublished = true", Course.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }*/

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
    public Set<Course> getCoursesByTopic(String topic) {
        TypedQuery<Course> query = em.createQuery("FROM Course WHERE element(topics).topic LIKE :topic", Course.class);
        query.setParameter("topic", topic);
        return new HashSet<>(query.getResultList());
    }

   /* @Override
    public PaginationResult<Course> getAllBySearchCriteria(CourseSearchCriteria searchCriteria, int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> courseQuery = cb.createQuery(Course.class);
        Root<Course> courseRoot = courseQuery.from(Course.class);
        if (searchCriteria.getTitle().isPresent()) {
            String title = searchCriteria.getTitle().get().toLowerCase();
            Predicate titlePredicate = cb.like(cb.lower(courseRoot.get("title")), "%" + title + "%");
            courseQuery.where(titlePredicate);
            Set<Course> coursesSet = new TreeSet<>(em.createQuery(courseQuery).setFirstResult((page - 1) * size).setMaxResults(size).getResultList());
            PaginationResult<Course> result = new PaginationResult<>();
            result.setCurrentPage(page);
            result.setSize(size);
            result.setData(coursesSet);
            long totalRecords = getTotalRecords(searchCriteria);
            result.setTotalRecords(totalRecords);
            result.setLastPage(getLastPage(totalRecords, size));
            return result;
        }
        return null;
    }*/

    @Override
    public PaginationResult<Course> getAllBySearchCriteria(CourseSearchCriteria searchCriteria, int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> courseQuery = cb.createQuery(Course.class);
        Root<Course> courseRoot = courseQuery.from(Course.class);
        Predicate titleLike;
        Predicate teacherLike;
        Predicate descriptionLike;
        Predicate startDatePredicate;
        Predicate combinedPredicates;
        if (searchCriteria.getTitle().isPresent()) {
            String title = searchCriteria.getTitle().get().toLowerCase();
            titleLike = cb.like(cb.lower(courseRoot.get("title")), "%" + title + "%");
        } else {
            titleLike = cb.like(cb.lower(courseRoot.get("title")), "%%");
        }
        if (searchCriteria.getTeacher().isPresent()) {
            String teacher = searchCriteria.getTeacher().get().toLowerCase();
            teacherLike = cb.like(cb.lower(courseRoot.get("teacher").get("username")), "%" + teacher + "%");
        } else {
            teacherLike = cb.like(courseRoot.get("teacher").get("username"), "%%");
        }
        if (searchCriteria.getDescription().isPresent()) {
            String description = searchCriteria.getDescription().get().toLowerCase();
            descriptionLike = cb.like(cb.lower(courseRoot.get("description")), "%" + description + "%");
        } else {
            descriptionLike = cb.like(cb.lower(courseRoot.get("description")), "%%");
        }

        if (searchCriteria.getStartDate().isPresent()) {
            LocalDate startDate = searchCriteria.getStartDate().get();
            startDatePredicate = cb.greaterThan(courseRoot.get("startDate"),startDate);
        } else {

            startDatePredicate = cb.greaterThan(courseRoot.get("startDate"),LocalDate.MIN);
        }

        combinedPredicates = cb.and(titleLike,teacherLike, descriptionLike,startDatePredicate);
        courseQuery.where(combinedPredicates);
        Set<Course> coursesSet = new TreeSet<>(em.createQuery(courseQuery).setFirstResult((page - 1) * size).setMaxResults(size).getResultList());
        PaginationResult<Course> result = new PaginationResult<>();
        result.setCurrentPage(page);
        result.setSize(size);
        result.setData(coursesSet);
        long totalRecords = getTotalRecords(searchCriteria);
        result.setTotalRecords(totalRecords);
        result.setLastPage(getLastPage(totalRecords, size));
        return result;
    }

    private int getLastPage(long totalRecords, int pageSize) {
        if (totalRecords == 0) {
            return 1;
        }
        int lastPage = (int) totalRecords / pageSize;
        if (totalRecords % pageSize != 0) {
            lastPage++;
        }
        return lastPage;

    }

    private long getTotalRecords(CourseSearchCriteria searchCriteria) {
        if (searchCriteria.getTitle().isPresent()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> totalRecordsQuery = cb.createQuery(Long.class);
            Root<Course> courseCountRoot = totalRecordsQuery.from(Course.class);
            String title = searchCriteria.getTitle().get().toLowerCase();

            Predicate titlePredicate = cb.like(cb.lower(courseCountRoot.get("title")), "%" + title + "%");
            totalRecordsQuery.select(cb.count(courseCountRoot)).where(titlePredicate);
            return em.createQuery(totalRecordsQuery).getSingleResult();
        }
        return 1111111;
    }

}
