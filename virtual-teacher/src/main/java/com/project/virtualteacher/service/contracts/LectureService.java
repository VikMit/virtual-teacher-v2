package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.User;

public interface LectureService {

    Lecture findById(int lectureId, User loggedUser);

}
