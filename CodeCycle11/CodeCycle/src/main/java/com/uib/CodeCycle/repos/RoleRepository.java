package com.uib.CodeCycle.repos;

import com.uib.CodeCycle.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "role")
@CrossOrigin("http://localhost:4200/")
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(String roleName);
}
