package com.ghallab.Ghallab_Bank.role.repo;

import com.ghallab.Ghallab_Bank.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name )
;}
