package com.retro_rent.managerApplication.modle;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents an User for this web application.
 */
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")})
public class User {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @JsonIgnore
    private String password;

    @Column(name = "paymentLink")
    private String paymentLink;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Column(name = "user_name")
    @NotEmpty(message = "*Please provide your user name")
    private String user_name;

    @Column(name = "name")
    @NotEmpty(message = "*Please provide your name")
    private String name;

    @Column(name = "last_name")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "address_number")
    private String address_number;

    @Column(name = "postal_code")
    private String postal_code;

    private String imageUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    private Role role;

    @OneToMany
    private List<UserReview> userReviews;

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<UserReview> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<UserReview> userReviews) {
        this.userReviews = userReviews;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress_number() {
        return address_number;
    }

    public String getCity() {
        return city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getStreet() {
        return street;
    }


    public void setAddress_number(String address_number) {
        this.address_number = address_number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
} // class User