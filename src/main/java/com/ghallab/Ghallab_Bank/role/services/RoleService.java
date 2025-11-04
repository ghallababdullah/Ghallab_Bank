package com.ghallab.Ghallab_Bank.role.services;

import com.ghallab.Ghallab_Bank.res.Response;
import com.ghallab.Ghallab_Bank.role.entity.Role;

import java.util.List;

public interface RoleService {
    Response<Role> createRole(Role roleRequest) ;
    Response<Role> updateRole (Role roleRequest) ;
    Response<List<Role>> getAllroles () ;
    Response<?> deleteRole (Long id) ;
}
