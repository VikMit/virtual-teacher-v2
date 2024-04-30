package com.project.virtualteacher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDto {
    private int id;


    private String value;
}
