package com.retro_rent.managerApplication.Dao;

import com.retro_rent.managerApplication.modle.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleDao extends JpaRepository<Role, Long>{
    Role findByRole(String role);
}
