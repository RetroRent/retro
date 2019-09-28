package com.retro_rent.managerApplication.payload;

import java.sql.Date;

public class OrderItemResponse {
    private long id;

    private RenterItemResponse item;

    private int totalDaysRent;

    private String totalPriceRent;

    private Date rentalStartDay;

    private Date rentalEndDay;

    private String status;

    public OrderItemResponse(long id, RenterItemResponse item, int totalDaysRent, String totalPriceRent, Date rentalStartDay, Date rentalEndDay, String status) {
        this.id = id;
        this.item = item;
        this.totalDaysRent = totalDaysRent;
        this.totalPriceRent = totalPriceRent;
        this.rentalStartDay = rentalStartDay;
        this.rentalEndDay = rentalEndDay;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RenterItemResponse getItem() {
        return item;
    }

    public void setItem(RenterItemResponse item) {
        this.item = item;
    }

    public int getTotalDaysRent() {
        return totalDaysRent;
    }

    public void setTotalDaysRent(int totalDaysRent) {
        this.totalDaysRent = totalDaysRent;
    }

    public String getTotalPriceRent() {
        return totalPriceRent;
    }

    public void setTotalPriceRent(String totalPriceRent) {
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
}
