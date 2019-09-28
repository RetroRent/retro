package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.ItemReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ItemReviewRepository")
public interface ItemReviewDao extends JpaRepository<ItemReview, Long> {
    ItemReview findById(long id);
    void delete(ItemReview itemReview);
    List<ItemReview> findAllByItem_Id(long itemID);
    List<ItemReview> findAll();
}
