package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.LectureDto;
import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.service.contracts.LectureService;
import com.project.virtualteacher.utility.BindingResultCatcher;
import com.project.virtualteacher.utility.ExtractEntityHelper;
import com.project.virtualteacher.utility.Mapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecture")
public class LectureController {

    private final ExtractEntityHelper extractEntityHelper;
    private final LectureService lectureService;
    private final BindingResultCatcher catchInputErrors;
    private final Mapper mapper;


    public LectureController(ExtractEntityHelper extractEntityHelper, LectureService lectureService, BindingResultCatcher catchInputErrors, Mapper mapper) {
        this.extractEntityHelper = extractEntityHelper;
        this.lectureService = lectureService;
        this.catchInputErrors = catchInputErrors;
        this.mapper = mapper;
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<Lecture> lecture(@PathVariable(name = "lectureId") int lectureId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Lecture lecture = lectureService.findById(lectureId, loggedUser);
        return new ResponseEntity<>(lecture, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Lecture> create(@RequestBody @Valid LectureDto lectureDto, BindingResult errors, Authentication authentication) {
        Lecture lectureToCreate = mapper.fromLectureDtoToLecture(lectureDto, errors);
        catchInputErrors.proceedInputError(errors);
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Lecture createdLecture = lectureService.create(lectureToCreate, loggedUser);
        return new ResponseEntity<>(createdLecture, HttpStatus.CREATED);
    }


    @DeleteMapping("/{lectureId}")
    public ResponseEntity<String> delete(@PathVariable(name = "lectureId") int lectureId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        lectureService.delete(lectureId, loggedUser);
        return new ResponseEntity<>("Lecture with ID: "+lectureId+"was deleted",HttpStatus.OK);
    }

}
