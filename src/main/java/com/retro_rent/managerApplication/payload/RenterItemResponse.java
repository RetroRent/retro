package com.retro_rent.managerApplication.payload;

import com.retro_rent.managerApplication.modle.Review;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class RenterItemResponse {
    private long id;

    private long userID;

    private String labels;

    private boolean star;

    private String description;

    private String year_of_production;

    private List<String> imageItemNames;

    private List<Integer> daysAvailable;

    private List<Date> start;

    private List<Date> end;

    private String pricePerDay;

    private String currency;

    private String itemFCategory;

    private String itemSCategory;

    private String itemTCategory;

    private String ownerName;

    private String ownerEmail;

    private List<Review> itemReviews;

    public RenterItemResponse(long id, long userID, String labels, String description, String year_of_production, double pricePerDay,String currency, String itemFCategory, String itemSCategory, String itemTCategory, String ownerName, String ownerEmail, List<String> imageItemNames, List<Integer> daysAvailable, List<Date> start, List<Date> end, boolean star, List<Review> itemsReview) {
        this.id = id;
        this.userID = userID;
        this.labels = labels;
        this.description = description;
        this.year_of_production = year_of_production;
        this.pricePerDay = String.valueOf(pricePerDay).concat(currency);
        this.itemFCategory = itemFCategory;
        this.itemSCategory = itemSCategory;
        this.itemTCategory = itemTCategory;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.imageItemNames = imageItemNames;
        this.daysAvailable = daysAvailable;
        this.start = start;
        this.end = end;
        this.currency = currency;
        this.star = star;
        this.itemReviews = itemsReview;
    }

    public List<Review> getItemReviews() {
        return itemReviews;
    }

    public void setItemReviews(List<Review> itemReviews) {
        this.itemReviews = itemReviews;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public long getUserID() {
        return userID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear_of_production() {
        return year_of_production;
    }

    public void setYear_of_production(String year_of_production) {
        this.year_of_production = year_of_production;
    }

    public String getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(String pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getItemFCategory() {
        return itemFCategory;
    }

    public void setItemFCategory(String itemFCategory) {
        this.itemFCategory = itemFCategory;
    }

    public String getItemSCategory() {
        return itemSCategory;
    }

    public void setItemSCategory(String itemSCategory) {
        this.itemSCategory = itemSCategory;
    }

    public String getItemTCategory() {
        return itemTCategory;
    }

    public void setItemTCategory(String itemTCategory) {
        this.itemTCategory = itemTCategory;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public List<String> getImageItemNames() {
        return imageItemNames;
    }

    public void setImageItemNames(List<String> imageItemNames) {
        this.imageItemNames = imageItemNames;
    }

    public List<Integer> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(List<Integer> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    public List<Date> getStart() {
        return start;
    }

    public void setStart(List<Date> start) {
        this.start = start;
    }

    public List<Date> getEnd() {
        return end;
    }

    public void setEnd(List<Date> end) {
        this.end = end;
    }

    public void addDates(Date start, Date end)
    {
        if (this.start == null) {
            this.start = new LinkedList<>();
        }

        if (this.end == null) {
            this.end = new LinkedList<>();
        }

        this.start.add(start);
        this.end.add(end);
    }
}
