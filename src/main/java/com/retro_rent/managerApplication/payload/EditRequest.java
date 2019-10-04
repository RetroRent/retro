package com.retro_rent.managerApplication.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EditRequest {
    @NotNull
    private long id;

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @NotBlank
    private String paymentLink;

    @NotBlank
    private String user_name;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String home_number;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String postal_code;


    private String img_url;


    @NotBlank
    private String phone_number;

    private boolean user_type_renter;
    private boolean user_type_tenant;

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public String getFirst_name() {
        return first_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHome_number() {
        return home_number;
    }

    public void setHome_number(String home_number) {
        this.home_number = home_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isUser_type_renter() {
        return user_type_renter;
    }

    public void setUser_type_renter(boolean user_type_renter) {
        this.user_type_renter = user_type_renter;
    }

    public boolean isUser_type_tenant() {
        return user_type_tenant;
    }

    public void setUser_type_tenant(boolean user_type_tenant) {
        this.user_type_tenant = user_type_tenant;
    }
}
