package com.manager.myapplication.model;

import java.util.List;

public class DonHangModel {
    boolean success;
    String message;
    List<DonHang> result;

    public DonHangModel(boolean success, String message, List<DonHang> result) {
        this.success = success;
        this.message = message;
        this.result = result;
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

    public List<DonHang> getResult() {
        return result;
    }

    public void setResult(List<DonHang> result) {
        this.result = result;
    }
}
