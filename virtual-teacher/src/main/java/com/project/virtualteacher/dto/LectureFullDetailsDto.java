package com.project.virtualteacher.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)

public class LectureFullDetailsDto extends LectureBaseDetailsDto {

    private String videoUrl;

    private String assignmentUrl;

}
