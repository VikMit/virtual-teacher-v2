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

    @Override
    @Transactional
    public Role update(Role roleUpdate, int roleToUpdateId) {
        Role roleToUpdate = findById(roleToUpdateId);
        roleToUpdate.setValue(roleUpdate.getValue());
        addPrefixIfNotPresent(roleToUpdate);
        return roleDao.update(roleToUpdate);

    }

    @Override
    @Transactional
    public void delete(int roleId) {
        Role roleToDelete = roleDao.findById(roleId).orElseThrow(()->new RoleException(ErrorMessage.ROLE_ID_NOT_FOUND,roleId));
        throwIfRoleAssignedToUser(roleId);
        roleDao.delete(roleToDelete);
    }

    private void addPrefixIfNotPresent(Role role) {
        if (role.getValue().startsWith("ROLE_") || role.getValue().startsWith("role_")) {
            role.setValue(role.getValue().toUpperCase());
        } else {
            role.setValue("ROLE_" + role.getValue().toUpperCase());
        }
    }

    private void throwIfRoleAssignedToUser(int roleId) {
        if (roleDao.isAssignedToUser(roleId)) {
            throw new RoleException("Can not delete role when it's assigned to user");
        }
    }
}
