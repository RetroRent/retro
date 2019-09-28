package com.retro_rent.managerApplication.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class SignUpRequest {
    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @NotBlank
    private String user_name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String name) {
        this.first_name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String name) {
        this.last_name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String name) {
        this.user_name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
