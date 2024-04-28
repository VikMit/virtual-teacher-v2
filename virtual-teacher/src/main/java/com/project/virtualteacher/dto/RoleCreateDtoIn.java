package com.project.virtualteacher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateDtoIn {
    @NotBlank
    private String value;
}
