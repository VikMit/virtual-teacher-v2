package com.project.virtualteacher.dao.contracts;

import com.project.virtualteacher.entity.Role;

import java.util.Optional;

public interface RoleDao {
    Optional<Role> findById(int id);
    Optional<Role> findByName(String roleName);
}
