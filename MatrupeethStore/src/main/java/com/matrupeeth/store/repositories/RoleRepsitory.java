package com.matrupeeth.store.repositories;

import com.matrupeeth.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepsitory extends JpaRepository<Role,String> {
}
