package com.retro_rent.managerApplication.payload;

import com.retro_rent.managerApplication.modle.User;

public class ApiUserResponse {
    private boolean success;
    private String message;
    private User user;

    public ApiUserResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
