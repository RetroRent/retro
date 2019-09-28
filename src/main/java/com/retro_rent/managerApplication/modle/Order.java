package com.retro_rent.managerApplication.modle;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "retro_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retro_order_id")
    private long id;

    @OneToMany
    private List<OrderedItem> orderedItems;

    @ManyToOne
    private Tenant tenant;

    @Column(name = "creation_date")
    private Date creationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void removeOrderItem(OrderedItem orderedItem)
    {
        if (this.orderedItems != null)  {
            this.orderedItems.remove(orderedItem);
        }
    }
}
