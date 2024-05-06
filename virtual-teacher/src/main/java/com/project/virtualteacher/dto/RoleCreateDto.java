package com.project.virtualteacher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateDto {

    @NotBlank
    private String value;
}
