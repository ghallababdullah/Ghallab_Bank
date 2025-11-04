package com.ghallab.Ghallab_Bank.role.services.impl;

import com.ghallab.Ghallab_Bank.exceptions.BadRequestException;
import com.ghallab.Ghallab_Bank.exceptions.NotFoundException;
import com.ghallab.Ghallab_Bank.res.Response;
import com.ghallab.Ghallab_Bank.role.entity.Role;
import com.ghallab.Ghallab_Bank.role.repo.RoleRepo;
import com.ghallab.Ghallab_Bank.role.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo ;
    @Override
    public Response<Role> createRole(Role roleRequest) {
        if (roleRepo.findByName(roleRequest.getName()).isPresent()) {
            throw new BadRequestException("Role Already existed") ;
        }
        Role savedRole = roleRepo.save(roleRequest);

        return Response.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role was created")
                .data(savedRole)
                .build();


    }
    @Override
    public Response<Role> updateRole(Role roleRequest) {
        Role role = roleRepo.findById(roleRequest.getId())
                .orElseThrow(()->new NotFoundException("role is not found"));

        role.setName(roleRequest.getName());
        Role updatedRole = roleRepo.save(role) ;
        return Response.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role was created")
                .data(updatedRole)
                .build();

    }

    @Override
    public Response<List<Role>> getAllroles() {
        List<Role> roles = roleRepo.findAll();
        return Response.<List<Role>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Roles were Fetched")
                .data(roles)
                .build();


    }

    @Override
    public Response<?> deleteRole(Long id) {
        if (!roleRepo.existsById(id)){
            throw  new NotFoundException("Role not Found") ;

        }
        roleRepo.deleteById(id);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role was deleted")
                .build();

    }
}
