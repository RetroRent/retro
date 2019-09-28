package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.Item;
import com.retro_rent.managerApplication.modle.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ItemRepository")
public interface ItemDao extends JpaRepository<Item, Long> {
    Item findById(long id);
    void delete(Item item);
    List<Item> findAllByOwner(Renter renter);
    Item findByOwnerAndId(Renter renter, long id);
    List<Item> findAll();
}
