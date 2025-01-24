package com.mealpicker.project.repositories;

import com.mealpicker.project.model.AppRole;
import com.mealpicker.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
