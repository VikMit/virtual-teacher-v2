package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.LectureBaseDetailsDto;
import com.project.virtualteacher.dto.LectureFullDetailsDto;
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
    public ResponseEntity<LectureFullDetailsDto> lecture(@PathVariable(name = "lectureId") int lectureId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Lecture lecture = lectureService.findById(lectureId, loggedUser);
        LectureFullDetailsDto lectureToReturn = mapper.fromLectureToLectureFullDetailsDto(lecture);
        return new ResponseEntity<>(lectureToReturn, HttpStatus.OK);
    }

    @GetMapping("/{lectureId}/public/basic")
    public ResponseEntity<LectureBaseDetailsDto> lectureBasic(@PathVariable(name = "lectureId") int lectureId){
        Lecture lecture = lectureService.findPublicById(lectureId);
        LectureBaseDetailsDto lectureToReturn = mapper.fromLectureToLectureBaseDetailsDto(lecture);
        return new ResponseEntity<>(lectureToReturn,HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Lecture> create(@RequestBody @Valid LectureBaseDetailsDto lectureBaseDetailsDto, BindingResult errors, Authentication authentication) {
        Lecture lectureToCreate = mapper.fromLectureBaseDetailsDtoToLecture(lectureBaseDetailsDto);
        catchInputErrors.proceedInputError(errors);
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Lecture createdLecture = lectureService.create(lectureToCreate, loggedUser);
        return new ResponseEntity<>(createdLecture, HttpStatus.CREATED);
    }


    @DeleteMapping("/{lectureId}")
    public ResponseEntity<String> delete(@PathVariable(name = "lectureId") int lectureId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        lectureService.delete(lectureId, loggedUser);
        return new ResponseEntity<>("Lecture with ID: " + lectureId + " was deleted", HttpStatus.OK);
    }

    @PutMapping("/{lectureId}")
    public ResponseEntity<Lecture> update(@PathVariable(name = "lectureId") int lectureId, @RequestBody @Valid LectureBaseDetailsDto lectureBaseDetailsDto, BindingResult errors, Authentication authentication){
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Lecture lectureUpdate = mapper.fromLectureBaseDetailsDtoToLecture(lectureBaseDetailsDto);
        lectureUpdate.setId(lectureId);
        Lecture updatedLecture = lectureService.update(lectureUpdate,loggedUser);
        return new ResponseEntity<>(updatedLecture,HttpStatus.OK);
    }

    @GetMapping("/{lectureId}/assignment")
    public ResponseEntity<String> downloadAssignment(@PathVariable(name = "lectureId")int lectureId,Authentication authentication){
        User user = extractEntityHelper.extractUserFromAuthentication(authentication);
        String assignmentUrl = lectureService.getAssignment(lectureId,user);
        return new ResponseEntity<>(assignmentUrl,HttpStatus.OK);
    }

}
