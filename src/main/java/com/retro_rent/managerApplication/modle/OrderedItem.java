package com.retro_rent.managerApplication.modle;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "ordered_item")
public class OrderedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdItemId")
    private long orderdItemId;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Tenant tenant;

//    @Column(name = "freight_cost")
//    private double freightCost;

    @Column(name = "total_days_rent")
    private int totalDaysRent;

    @Column(name = "total_price_rent")
    private double totalPriceRent;

    @Column(name = "rental_start_day")
    private Date rentalStartDay;

    @Column(name = "rental_end_day")
    private Date rentalEndDay;

    @Column(name = "orderItemStatus")
    private OrderItemStatus orderItemStatus;

    public OrderItemStatus getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(OrderItemStatus orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }

    public long getOrderdItemId() {
        return orderdItemId;
    }

    public void setOrderdItemId(long orderdItemId) {
        this.orderdItemId = orderdItemId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
//
//    public double getFreightCost() {
//        return freightCost;
//    }
//
//    public void setFreightCost(double freightCost) {
//        this.freightCost = freightCost;
//    }

    public int getTotalDaysRent() {
        return totalDaysRent;
    }

    public void setTotalDaysRent(int totalDaysRent) {
        this.totalDaysRent = totalDaysRent;
    }

    public double getTotalPriceRent() {
        return totalPriceRent;
    }

    public void setTotalPriceRent(double totalPriceRent) {
        this.totalPriceRent = totalPriceRent;
    }

    public Date getRentalStartDay() {
        return rentalStartDay;
    }

    public void setRentalStartDay(Date rentalStartDay) {
        this.rentalStartDay = rentalStartDay;
    }

    public Date getRentalEndDay() {
        return rentalEndDay;
    }

    public void setRentalEndDay(Date rentalEndDay) {
        this.rentalEndDay = rentalEndDay;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof OrderedItem)) {
            return false;
        }

        OrderedItem orderedItem = (OrderedItem)o;

        if (this.getOrderdItemId() == orderedItem.getOrderdItemId()) {
            return true;
        }

        return false;
    }
}
