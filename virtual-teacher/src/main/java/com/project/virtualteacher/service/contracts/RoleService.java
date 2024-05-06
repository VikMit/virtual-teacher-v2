package com.project.virtualteacher.service.contracts;

import com.project.virtualteacher.entity.Role;

public interface RoleService {

    Role findById(int roleId);

    void create(Role roleToCreate);

    Role update(Role roleUpdate, int roleToUpdateId);

    void delete(int roleId);

}
