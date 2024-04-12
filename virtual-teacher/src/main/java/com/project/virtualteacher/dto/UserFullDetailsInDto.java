package com.project.virtualteacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserFullDetailsInDto extends UserBaseDetailsInDto {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&* ()-+=^])(?=\\S+$).{8,20}$";
    public static final String PASS_REGEX_ERROR_MESSAGE = "Password must be at least 8 symbols long and contain upper,lower,special characters";

    @Size(min = 8, max = 30)
    private String username;

    @Pattern(regexp = PASSWORD_PATTERN, message = PASS_REGEX_ERROR_MESSAGE)
    private String password;

    @Pattern(regexp = PASSWORD_PATTERN, message = PASS_REGEX_ERROR_MESSAGE)
    private String confirmPassword;

    @Email(message = "Email should be valid")
    private String email;

}
