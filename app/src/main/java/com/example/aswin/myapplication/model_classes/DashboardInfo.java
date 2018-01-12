package com.example.aswin.myapplication.model_classes;

/**
 * Created by ASWIN on 1/12/2018.
 */

public class DashboardInfo {

    String totalRecords,totalAmount;

    public DashboardInfo() {
    }

    public DashboardInfo(String totalRecords, String totalAmount) {
        this.totalRecords = totalRecords;
        this.totalAmount = totalAmount;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
