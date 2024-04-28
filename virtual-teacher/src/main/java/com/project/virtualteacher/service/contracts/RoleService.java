package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.dto.RoleCreateDtoIn;
import com.project.virtualteacher.entity.Role;
import org.springframework.security.core.Authentication;

public interface RoleService {

    Role findById(int roleId);

    void create(Role roleToCreate);

    void update(Role roleUpdate, int roleToUpdateId);

}
