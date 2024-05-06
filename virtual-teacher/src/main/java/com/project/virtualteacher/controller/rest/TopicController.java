package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.TopicDto;
import com.project.virtualteacher.entity.Topic;
import com.project.virtualteacher.entity.User;
import com.project.virtualteacher.service.contracts.TopicService;
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
@RequestMapping("/api/v1/topic")
public class TopicController {
    private final TopicService topicService;
    private final ExtractEntityHelper extractEntityHelper;
    private final Mapper mapper;
    private final BindingResultCatcher bindingErrorCatcher;

    public TopicController(TopicService topicService, ExtractEntityHelper extractEntityHelper, Mapper mapper, BindingResultCatcher bindingErrorCatcher) {
        this.topicService = topicService;
        this.extractEntityHelper = extractEntityHelper;
        this.mapper = mapper;
        this.bindingErrorCatcher = bindingErrorCatcher;
    }

    @PostMapping()
    public ResponseEntity<Topic> create(@RequestBody @Valid TopicDto topicDto, BindingResult errors, Authentication authentication) {
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Topic topicToCreate = mapper.fromTopicDtoToTopic(topicDto);
        topicToCreate = topicService.create(topicToCreate, loggedUser);
        bindingErrorCatcher.proceedInputError(errors);
        return new ResponseEntity<>(topicToCreate, HttpStatus.CREATED);
    }
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicDto> update(@RequestBody @Valid TopicDto topicUpdate,@PathVariable(name = "topicId") int id,BindingResult errors ,Authentication authentication){
        User loggedUser = extractEntityHelper.extractUserFromAuthentication(authentication);
        Topic topicToUpdate = mapper.fromTopicDtoToTopic(topicUpdate);
        Topic updatedTopic = topicService.update(id,topicToUpdate,loggedUser);
        TopicDto result = mapper.fromTopicToTopicDto(updatedTopic);
        bindingErrorCatcher.proceedInputError(errors);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
