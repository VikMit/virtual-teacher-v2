package com.project.virtualteacher.dto;

import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.utility.Mapper;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutDto {

    private int userId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String pictureUrl;

    private Role role;

}
