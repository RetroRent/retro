package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.User;
import com.retro_rent.managerApplication.modle.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserReviewRepository")
public interface UserReviewDao extends JpaRepository<UserReview, Long> {
    UserReview findById(long id);
    void delete(UserReview itemReview);
    List<UserReview> findAllByUser(User user);
    List<UserReview> findAll();
}
