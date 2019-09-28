package com.retro_rent.managerApplication.Dao;

import com.retro_rent.managerApplication.modle.Renter;
import com.retro_rent.managerApplication.modle.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RenterRepository")
public interface RenterDao extends JpaRepository<Renter, Long> {
    Renter findByUser_Id(long id);
    Renter findByUser(User user);
    void delete(Renter user);

    void deleteByUser_Id(long user_id);
    List<Renter> findAll();

    Renter findByUser_Email(String email);
}