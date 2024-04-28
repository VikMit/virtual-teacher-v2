package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.RoleException;
import com.project.virtualteacher.service.contracts.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role findById(int roleId) {
        return roleDao.findById(roleId).orElseThrow(() -> new RoleException(ErrorMessage.ROLE_ID_NOT_FOUND, roleId));
    }

    @Override
    @Transactional
    public void create(Role roleToCreate) {
        addPrefixIfNotPresent(roleToCreate);
        if (roleDao.isRoleNameExist(roleToCreate.getValue())) {
            throw new RoleException(ErrorMessage.ROLE_NAME_EXIST, roleToCreate.getValue());
        }
        roleDao.create(roleToCreate);

    }

    private void addPrefixIfNotPresent(Role role) {
        if (role.getValue().startsWith("ROLE_") || role.getValue().startsWith("role_")) {
            role.setValue( role.getValue().toUpperCase());
        } else {
            role.setValue("ROLE_" + role.getValue().toUpperCase());
        }
    }
}
