package com.retro_rent.managerApplication.payload;

import javax.validation.constraints.NotNull;

public class EditItemRequest {

    @NotNull
    private long userID;

    @NotNull
    private long itemID;

    @NotNull
    private String pricePerDay;

    @NotNull
    private String currency;

    @NotNull
    private String description;

    @NotNull
    private String labels;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(String pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }
}
