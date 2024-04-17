package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Role;
import lombok.Data;

@Data
public class UserOutDto {

    private int userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String pictureUrl;
    private Role role;
}
