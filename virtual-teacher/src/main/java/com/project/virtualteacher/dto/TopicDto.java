package com.project.virtualteacher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicDto {
    private int id;

    @Size(min = 3,max = 32,message = "Topic must be with length between 3 and 32 characters, leading and trailing whitespaces are trimmed.")
    private String topic;

    public void setTopic(@NotBlank String topic) {
        this.topic = topic.trim();
    }
}
