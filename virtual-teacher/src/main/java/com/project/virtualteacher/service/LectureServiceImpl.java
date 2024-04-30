package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.LectureDao;
import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.service.contracts.LectureService;
import com.project.virtualteacher.utility.ValidatorHelper;
import org.springframework.stereotype.Service;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureDao lectureDao;
    private final ValidatorHelper validator;

    public LectureServiceImpl(LectureDao lectureDao, ValidatorHelper validator) {
        this.lectureDao = lectureDao;
        this.validator = validator;
    }

    @Override
    public Lecture findById(int lectureId, User loggedUser) {
        Lecture lecture = lectureDao.findById(lectureId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.LECTURE_ID_NOT_FOUND, lectureId));
        if (validator.isTeacherOrAdmin(loggedUser) || validator.isUserEnrolledForCourse(loggedUser, lecture.getCourse())) {
            return lecture;
        } else {
            throw new UnAuthorizeException(ErrorMessage.USER_NOT_ENROLLED_LECTURE_ACCESS_DENIED, loggedUser.getUsername());
        }
    }

}
