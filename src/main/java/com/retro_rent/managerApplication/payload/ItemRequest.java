package com.retro_rent.managerApplication.payload;

import javax.validation.constraints.NotNull;

public class ItemRequest {

    @NotNull
    private long renter_user_id;

    @NotNull
    private String pricePerDay;

    @NotNull
    private String year_of_production;

    @NotNull
    private String description;

    @NotNull
    private String currency;

    @NotNull
    private boolean sun;
    @NotNull
    private boolean mon;
    @NotNull
    private boolean tue;
    @NotNull
    private boolean wed;
    @NotNull
    private boolean thu;
    @NotNull
    private boolean fri;
    @NotNull
    private boolean sat;

    @NotNull
    private String main_category;
    @NotNull
    private String secondary_category;
    @NotNull
    private String third_category;
    @NotNull
    private String labels;

    public ItemRequest()
    {

    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getRenter_user_id() {
        return renter_user_id;
    }

    public void setRenter_user_id(long renter_user_id) {
        this.renter_user_id = renter_user_id;
    }

    public String getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(String pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getYear_of_production() {
        return year_of_production;
    }

    public void setYear_of_production(String year_of_production) {
        this.year_of_production = year_of_production;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getSecondary_category() {
        return secondary_category;
    }

    public void setSecondary_category(String secondary_category) {
        this.secondary_category = secondary_category;
    }

    public String getThird_category() {
        return third_category;
    }

    public void setThird_category(String third_category) {
        this.third_category = third_category;
    }

    public String getLabels() {
        return this.labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }
}
