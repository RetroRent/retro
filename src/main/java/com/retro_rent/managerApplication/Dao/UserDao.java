package com.retro_rent.managerApplication.Dao;

import com.retro_rent.managerApplication.modle.User;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("UserRepository")
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    void delete(User user);
    Boolean existsByEmail(String email);
    List<User> findAll();
}