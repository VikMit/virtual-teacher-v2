package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.SolutionDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SolutionDaoImpl implements SolutionDao {
    private final EntityManager em;

    public SolutionDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void deleteAllForLecture(int lectureId) {
        Query query = em.createQuery("DELETE FROM Solution WHERE lecture.id =: lectureId");
        query.setParameter("lectureId",lectureId);
        query.executeUpdate();
    }
}
