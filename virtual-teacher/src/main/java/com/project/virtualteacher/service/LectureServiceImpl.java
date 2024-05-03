package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.CourseDao;
import com.project.virtualteacher.dao.contracts.LectureDao;
import com.project.virtualteacher.dao.contracts.SolutionDao;
import com.project.virtualteacher.entity.Course;
import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.EntityExistException;
import com.project.virtualteacher.exception_handling.exceptions.EntityNotExistException;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import com.project.virtualteacher.service.contracts.LectureService;
import com.project.virtualteacher.utility.ValidatorHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureDao lectureDao;
    private final ValidatorHelper validator;
    private final CourseDao courseDao;
    private final SolutionDao solutionDao;

    public LectureServiceImpl(LectureDao lectureDao, ValidatorHelper validator, CourseDao courseDao, SolutionDao solutionDao) {
        this.lectureDao = lectureDao;
        this.validator = validator;
        this.courseDao = courseDao;
        this.solutionDao = solutionDao;
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

    @Override
    @Transactional
    public Lecture create(Lecture lectureToCreate, User loggedUser) {
        validator.isCreatorOfCourse(lectureToCreate.getCourse(), loggedUser);
        validator.isLectureTitleExistInCourse(lectureToCreate.getCourse(), lectureToCreate.getTitle());
        return lectureDao.create(lectureToCreate);
    }

    @Override
    @Transactional
    public void delete(int lectureId, User loggedUser) {
        Lecture lectureToDelete = lectureDao.findById(lectureId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.LECTURE_ID_NOT_FOUND, lectureId));
        Course course = courseDao.getCourseByLectureId(lectureId).orElseThrow(() -> new EntityNotExistException(ErrorMessage.COURSE_WITH_LECTURE_NOT_FOUND, lectureId));
        validator.isCreatorOfCourse(course, loggedUser);
        solutionDao.delete(lectureId);
        lectureDao.delete(lectureToDelete);
    }

    @Override
    @Transactional
    public Lecture update(Lecture lectureUpdate, User loggedUser) {
        Course course = courseDao.getCourseByLectureId(lectureUpdate.getId()).orElseThrow(() -> new EntityNotExistException(ErrorMessage.LECTURE_ID_NOT_FOUND, lectureUpdate.getId()));
        validator.isCreatorOfCourse(course, loggedUser);
        throwIfTitleExistInCourse(lectureUpdate,course);
        lectureUpdate.setCourse(course);
        return lectureDao.update(lectureUpdate);
    }

    @Override
    public Lecture findPublicById(int lectureId) {
        return lectureDao.findById(lectureId).orElseThrow(()->new EntityNotExistException(ErrorMessage.PUBLIC_LECTURE_ID_NOT_FOUND,lectureId));
    }

    private void throwIfTitleExistInCourse(Lecture lecture, Course course) {
        boolean isTitleExist = course.getLectures().stream().anyMatch(l -> l.getTitle().equalsIgnoreCase(lecture.getTitle()));
        if (isTitleExist) {
            throw new EntityExistException(ErrorMessage.LECTURE_TITLE_EXIST, lecture.getTitle());
        }
    }

}
