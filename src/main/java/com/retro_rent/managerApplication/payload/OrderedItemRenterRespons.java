package com.retro_rent.managerApplication.payload;

import java.sql.Date;

public class OrderedItemRenterRespons {
    private long id;

    private long itemID;

    private int totalDaysRent;

    private String totalPriceRent;

    private Date rentalStartDay;

    private Date rentalEndDay;

    private String status;

    private String tenantName;

    private String tenantEmail;

    public OrderedItemRenterRespons(long id, long itemID, int totalDaysRent, String totalPriceRent, Date rentalStartDay, Date rentalEndDay, String status, String tenantName, String tenantEmail) {
        this.id = id;
        this.totalDaysRent = totalDaysRent;
        this.tenantEmail = tenantEmail;
        this.tenantName = tenantName;
        this.totalPriceRent = totalPriceRent;
        this.rentalStartDay = rentalStartDay;
        this.rentalEndDay = rentalEndDay;
        this.status = status;
        this.itemID = itemID;
    }

    public String getTenantEmail() {
        return tenantEmail;
    }

    public void setTenantEmail(String tenantEmail) {
        this.tenantEmail = tenantEmail;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
