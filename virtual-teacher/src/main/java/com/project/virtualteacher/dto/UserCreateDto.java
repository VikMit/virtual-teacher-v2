package com.project.virtualteacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserCreateDto extends UserUpdateDto {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&* ()-+=^])(?=\\S+$).{8,20}$";
    public static final String PASS_REGEX_ERROR_MESSAGE = "Password must be at least 8 symbols long and contain upper,lower,special characters";

    @NotNull(message = "Username can not be NULL.")
    @Size(min = 8, max = 30,message = "Username must be between 8 and 30 characters. Leading or trailing whitespaces are trimmed.")
    private String username;

    @Pattern(regexp = PASSWORD_PATTERN, message = PASS_REGEX_ERROR_MESSAGE)
    private String password;

    @Pattern(regexp = PASSWORD_PATTERN, message = PASS_REGEX_ERROR_MESSAGE)
    private String confirmPassword;

    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 1, message = "Role ID must be positive")
    private int roleId;

    private String pictureUrl;

    public void setUsername(String username) {
        this.username = username.trim();
    }
}
