package com.retro_rent.managerApplication.Dao;

import com.retro_rent.managerApplication.modle.Tenant;
import com.retro_rent.managerApplication.modle.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("TenantRepository")
public interface TenantDao extends JpaRepository<Tenant, Long> {
    Tenant findByUser_Id(long id);
    void delete(Tenant user);
    Tenant findByUser(User user);
    void deleteByUser_Id(long user_id);
    List<Tenant> findAll();

    Tenant findByUser_Email(String email);
}