package com.project.virtualteacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserBaseDetailsInDto {
    private static final String DATE_FORMAT = "";

    @NotBlank(message = "First name can not be empty")
    private String firstName;

    @NotBlank(message = "Last name can not be empty")
    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    @Min(value = 1, message = "Role ID must be positive")
    private int roleId;

    private String pictureUrl;
}
