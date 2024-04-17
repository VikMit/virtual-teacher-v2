package com.project.virtualteacher.dao;

import com.project.virtualteacher.entity.Role;

import java.util.Optional;

public interface RoleDao {
    Optional<Role> getRoleById(int id);
    Role getRoleByName(String roleName);
}
