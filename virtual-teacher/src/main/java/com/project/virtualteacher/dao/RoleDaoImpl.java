package com.project.virtualteacher.dao;

import com.project.virtualteacher.dao.contracts.RoleDao;
import com.project.virtualteacher.entity.Role;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final EntityManager em;

    public RoleDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Role> findById(int id) {
        Role role = em.find(Role.class, id);
        if (role == null) {
            return Optional.empty();
            // throw new RoleNotFoundException(ErrorMessage.ROLE_ID_NOT_FOUND,id);
        }
        return Optional.of(role);
    }

    @Override
    public Optional<Role> findByName(String roleName) {

        TypedQuery<Role> query = em.createQuery("FROM Role WHERE value=:value", Role.class);
        query.setParameter("value", roleName);

        try {
            Role role = query.getSingleResult();
            return Optional.of(role);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isRoleNameExist(String roleName) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM Role WHERE value = :roleName",Long.class);
        query.setParameter("roleName",roleName);
        long result = query.getSingleResult();
        return result>0;
    }

    @Override
    public void create(Role roleToCreate) {
        em.persist(roleToCreate);
    }
}
