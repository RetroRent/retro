package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.MessageBox;
import com.retro_rent.managerApplication.modle.MessageBoxStatus;
import com.retro_rent.managerApplication.modle.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MessageBoxRepository")
public interface MessageBoxDao extends JpaRepository<MessageBox, Long> {
    MessageBox findById(long id);
    void delete(MessageBox messageBox);

    List<MessageBox> findAll();

    List<MessageBox> findAllByUser(User user);
    MessageBox findByUserAndId(User user, long id);
    List<MessageBox> findAllByUserAndStatus(User user, MessageBoxStatus messageBoxStatus);
}
