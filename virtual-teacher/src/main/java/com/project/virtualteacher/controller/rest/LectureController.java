package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.entity.Lecture;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.service.contracts.LectureService;
import com.project.virtualteacher.utility.ExtractEntityHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lecture/")
public class LectureController {

    private final ExtractEntityHelper extractEntityHelper;
    private final LectureService lectureService;

    public LectureController(ExtractEntityHelper extractEntityHelper, LectureService lectureService) {
        this.extractEntityHelper = extractEntityHelper;
        this.lectureService = lectureService;
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<Lecture> lecture(@PathVariable(name = "lectureId") int lectureId, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Lecture result = lectureService.findById(lectureId, loggedUser);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
