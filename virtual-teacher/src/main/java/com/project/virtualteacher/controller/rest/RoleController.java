package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.RoleCreateDtoIn;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.service.contracts.RoleService;
import com.project.virtualteacher.utility.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;
    private final Mapper mapper;

    public RoleController(RoleService roleService, Mapper mapper) {
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> roleById(@PathVariable(name = "roleId") int roleId) {
        Role role = roleService.findById(roleId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> create(@RequestBody() RoleCreateDtoIn roleCreateDtoIn){
        Role roleToCreate = mapper.fromRoleCreateDtoInToRole(roleCreateDtoIn);
        roleService.create(roleToCreate);
        return new ResponseEntity<>("Role '"+ roleCreateDtoIn.getValue().toUpperCase()+"' was created.",HttpStatus.CREATED);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<String> update(@PathVariable(name = "roleId") int roleId,@RequestBody() RoleCreateDtoIn updateRole){
        Role roleUpdate = mapper.fromRoleCreateDtoInToRole(updateRole);
        roleService.update(roleUpdate, roleId);
        return new ResponseEntity<>("Role with ID: "+roleId+" was updated",HttpStatus.OK);
    }

}
