package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MeetingRepository")
public interface MeetingDao extends JpaRepository<Meeting, Long> {
    Meeting findById(long id);
    void delete(Meeting item);

    List<Meeting> findAll();
}
