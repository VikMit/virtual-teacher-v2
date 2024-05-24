package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.TopicDto;
import com.project.virtualteacher.entity.Topic;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.service.contracts.TopicService;
import com.project.virtualteacher.utility.UserValidatorHelperImpl;
import com.project.virtualteacher.utility.contracts.Mapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/topic")
public class TopicController {
    private final TopicService topicService;
    private final UserValidatorHelperImpl userValidatorHelperImpl;
    private final Mapper mapper;

    public TopicController(TopicService topicService, UserValidatorHelperImpl userValidatorHelperImpl, Mapper mapper) {
        this.topicService = topicService;
        this.userValidatorHelperImpl = userValidatorHelperImpl;
        this.mapper = mapper;
    }

    @PostMapping()
    public ResponseEntity<Topic> create(@RequestBody @Valid TopicDto topicDto, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        Topic topicToCreate = mapper.fromTopicDtoToTopic(topicDto);
        topicToCreate = topicService.create(topicToCreate, loggedUser);
        return new ResponseEntity<>(topicToCreate, HttpStatus.CREATED);
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<TopicDto> update(@RequestBody @Valid TopicDto topicUpdate, @PathVariable(name = "topicId") int id, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        Topic topicToUpdate = mapper.fromTopicDtoToTopic(topicUpdate);
        Topic updatedTopic = topicService.update(id, topicToUpdate, loggedUser);
        TopicDto result = mapper.fromTopicToTopicDto(updatedTopic);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDto> getById(@PathVariable(name = "topicId") int topicId) {
        Topic topicDb = topicService.getById(topicId);
        TopicDto result = mapper.fromTopicToTopicDto(topicDb);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<String> delete(@PathVariable(name = "topicId") int id, Authentication authentication) {
        User loggedUser = userValidatorHelperImpl.extractUserFromAuthentication(authentication);
        topicService.delete(id, loggedUser);
        return new ResponseEntity<>("Topic with ID: " + id + " was deleted.", HttpStatus.OK);
    }
}
