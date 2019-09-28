package com.retro_rent.managerApplication.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class RegistrationEndRequest {
    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotNull
    private String paymentLink;

    @NotBlank
    private String home_number;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String postal_code;

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

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
