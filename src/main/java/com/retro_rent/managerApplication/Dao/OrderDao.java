package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.Order;
import com.retro_rent.managerApplication.modle.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("OrderRepository")
public interface OrderDao extends JpaRepository<Order, Long> {
    Order findById(long id);
    void delete(Order item);
    List<Order> findAllByOrderedItemsContains(OrderedItem orderedItem);
    List<Order> findAllByTenant_TenantID(long id);
    List<Order> findAll();

    Order findByTenant_TenantIDAndOrderedItemsContains(long tenantID, OrderedItem orderedItem);
}
