package com.retro_rent.managerApplication.modle;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an User for this web application.
 */
@Entity
@Table(name = "tenant")
public class Tenant {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenantID")
    private long tenantID;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Item> wishItems;

    @OneToMany
    private List<Meeting> meetings;

    @OneToMany
    private List<Order> orders;


    public long getTenantID() {
        return tenantID;
    }

    public void setTenantID(long tenantID) {
        this.tenantID = tenantID;
    }

    public User getUser() {
        return user;
    }

    public List<Item> getWishItems() {
        return wishItems;
    }

    public void setWishItems(List<Item> wishItems) {
        this.wishItems = wishItems;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void addToWishList(Item item)
    {
        if (this.wishItems == null) {
            this.wishItems = new ArrayList<>();
        }

        if (!this.wishItems.contains(item)) {
            this.wishItems.add(item);
        }
    }


    public void removeFromeWishList(Item item)
    {
        if (this.wishItems != null) {
            this.wishItems.remove(item);
        }
    }
} // class User