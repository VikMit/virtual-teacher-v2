package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.RoleNotFoundException;
import com.project.virtualteacher.service.contracts.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role findById(int roleId) {
        return roleDao.findById(roleId).orElseThrow(() -> new RoleNotFoundException(ErrorMessage.ROLE_ID_NOT_FOUND, roleId));
    }
}
