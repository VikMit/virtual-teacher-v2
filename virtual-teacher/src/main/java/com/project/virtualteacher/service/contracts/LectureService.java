package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.User;

public interface LectureService {

    Lecture findById(int lectureId, User loggedUser);

    Lecture create(Lecture lectureToCreate, User loggedUser);

    void delete(int lectureId, User loggedUser);

    Lecture update(Lecture lectureUpdate, User loggedUser);
    Lecture findPublicById(int lectureId);

    String getAssignment(int lectureId, User loggedUser);
}
