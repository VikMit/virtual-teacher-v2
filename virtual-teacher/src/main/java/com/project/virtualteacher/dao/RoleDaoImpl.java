package com.project.virtualteacher.dao;

import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.RoleNotFoundException;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final EntityManager em;

    public RoleDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Role getRoleById(int id) {
        Role role = em.find(Role.class, id);
        if (role == null) {
            throw new RoleNotFoundException(ErrorMessage.ROLE_ID_NOT_FOUND,id);
        }
        return role;
    }

    @Override
    public Role getRoleByName(String roleName) {
        try {
            Query query = em.createQuery("FROM Role WHERE value=:value");
            query.setParameter("value",roleName);
            return (Role) query.getSingleResult();
        } catch (NoResultException e) {
            throw new RoleNotFoundException(ErrorMessage.ROLE_NAME_NOT_FOUND,roleName);
        }

    }
}
