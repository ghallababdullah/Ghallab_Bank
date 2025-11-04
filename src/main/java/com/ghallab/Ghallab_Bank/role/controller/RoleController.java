package com.ghallab.Ghallab_Bank.role.controller;

import com.ghallab.Ghallab_Bank.res.Response;
import com.ghallab.Ghallab_Bank.role.entity.Role;
import com.ghallab.Ghallab_Bank.role.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('Admin')")

public class RoleController {
    private final RoleService roleService ;

    @PostMapping
    public ResponseEntity<Response<Role>> createRole (@RequestBody Role roleRequest){
        return ResponseEntity.ok(roleService.createRole(roleRequest)) ;
    }
    @PutMapping
    public ResponseEntity<Response<Role>> updateRole (@RequestBody Role roleRequest){
        return ResponseEntity.ok(roleService.updateRole(roleRequest)) ;
    }

    @GetMapping
    public ResponseEntity<Response<List<Role>>> updateRole (){
        return ResponseEntity.ok(roleService.getAllroles()) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteRole (@PathVariable Long id){
        return ResponseEntity.ok(roleService.deleteRole(id)) ;
    }

}
