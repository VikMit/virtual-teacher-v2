package com.project.virtualteacher.controller.rest;

import com.project.virtualteacher.dto.RoleCreateDto;
import com.project.virtualteacher.entity.Role;
import com.project.virtualteacher.service.contracts.RoleService;
import com.project.virtualteacher.utility.MapperImpl;
import com.project.virtualteacher.utility.contracts.Mapper;
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
    public ResponseEntity<String> create(@RequestBody() RoleCreateDto roleCreateDto) {
        Role roleToCreate = mapper.fromRoleCreateDtoToRole(roleCreateDto);
        roleService.create(roleToCreate);
        return new ResponseEntity<>("Role '" + roleCreateDto.getValue().toUpperCase() + "' was created.", HttpStatus.CREATED);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Role> update(@PathVariable(name = "roleId") int roleId, @RequestBody() RoleCreateDto updateRole) {
        Role roleUpdate = mapper.fromRoleCreateDtoToRole(updateRole);
        roleUpdate = roleService.update(roleUpdate, roleId);
        return new ResponseEntity<>(roleUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> delete(@PathVariable(name = "roleId") int roleId) {
        roleService.delete(roleId);
        return new ResponseEntity<>("Role with ID: " + roleId + " was deleted", HttpStatus.OK);
    }

}
