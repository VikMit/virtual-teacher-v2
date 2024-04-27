package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.service.contracts.RoleService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> roleById(@PathVariable(name = "roleId") int roleId) {
        Role role = roleService.findById(roleId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
