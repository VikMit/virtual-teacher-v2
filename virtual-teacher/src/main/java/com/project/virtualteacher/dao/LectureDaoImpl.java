package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.LectureDao;
import com.project.virtualteacher.entity.Lecture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LectureDaoImpl implements LectureDao {
    private final EntityManager em;

    public LectureDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Lecture> findById(int lectureId) {

        Lecture lecture = em.find(Lecture.class,lectureId);
        if (lecture==null){
            return Optional.empty();
        }

        return Optional.of(lecture);

    }

    @Override
    public Lecture create(Lecture lectureToCreate) {
        em.persist(lectureToCreate);
        return lectureToCreate;
    }

    @Override
    public void delete(Lecture lectureToDelete) {
        em.remove(lectureToDelete);
    }

    @Override
    public Lecture update(Lecture lectureUpdate) {
        return em.merge(lectureUpdate);
    }
}
