package com.project.virtualteacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserUpdateDto {

    @NotBlank(message = "First name can not be empty")
    @Size(min = 1, max = 32, message = "First name must be in range min = 1 and max = 32 characters.")
    private String firstName;

    @NotBlank(message = "Last name can not be empty")
    @Size(min = 1, max = 32, message = "Last name must be in range min = 1 and max = 32 characters.")
    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }
}
