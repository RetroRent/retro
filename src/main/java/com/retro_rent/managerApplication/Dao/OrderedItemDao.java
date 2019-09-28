package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.OrderItemStatus;
import com.retro_rent.managerApplication.modle.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("OrderedItemRepository")
public interface OrderedItemDao extends JpaRepository<OrderedItem, Long> {
    OrderedItem findByOrderdItemId(long id);
    void delete(OrderedItem item);
    List<OrderedItem> findAllByItem_Id(long id);
    List<OrderedItem> findAll();
    List<OrderedItem> findAllByItem_Owner_IdAndOrderItemStatus(long renterID, OrderItemStatus orderItemStatus);
    List<OrderedItem> findAllByItem_Owner_IdAndOrderItemStatusIsNot(long renterID, OrderItemStatus orderItemStatus);
    OrderedItem findByItem_Owner_IdAndOrderItemStatusAndOrderdItemId(long renterID, OrderItemStatus orderItemStatus, long orderItemID);
    List<OrderedItem> findAllByTenant_TenantIDAndOrderItemStatus(long tenantID, OrderItemStatus orderItemStatus);
    List<OrderedItem> findAllByTenant_TenantIDAndOrderItemStatusIsNot(long tenantID, OrderItemStatus orderItemStatus);
    OrderedItem findByTenant_TenantIDAndOrderdItemId(long tenantID, long orderItemID);

}
