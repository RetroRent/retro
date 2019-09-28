package com.retro_rent.managerApplication.payload;

import java.util.List;

public class ApiOrderResponse {

    private boolean success;
    private String message;
    private List<String> paypalme;

    public ApiOrderResponse(boolean success, String message, List<String> paypalme) {
        this.success = success;
        this.message = message;
        this.paypalme = paypalme;
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

    public List<String> getPaypalme() {
        return paypalme;
    }

    public void setPaypalme(List<String> paypalme) {
        this.paypalme = paypalme;
    }
}
