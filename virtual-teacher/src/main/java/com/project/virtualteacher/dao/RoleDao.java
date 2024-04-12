package com.project.virtualteacher.dao;

import com.project.virtualteacher.entity.Role;

public interface RoleDao {
    Role getRoleById(int id);
    Role getRoleByName(String roleName);
}
