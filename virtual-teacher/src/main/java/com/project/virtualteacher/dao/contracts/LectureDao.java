package com.project.virtualteacher.dao.contracts;

import com.project.virtualteacher.entity.Lecture;

import java.util.Optional;

public interface LectureDao {

    Optional<Lecture> findById(int lectureId);

    Lecture create(Lecture lectureToCreate);

    void delete(Lecture lectureToDelete);

    Lecture update(Lecture lectureUpdate);
}
