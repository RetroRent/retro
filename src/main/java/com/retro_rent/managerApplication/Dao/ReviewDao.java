package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ReviewRepository")
public interface ReviewDao extends JpaRepository<Review, Long> {
    Review findById(long id);
    void delete(Review itemReview);

    List<Review> findAll();
}
