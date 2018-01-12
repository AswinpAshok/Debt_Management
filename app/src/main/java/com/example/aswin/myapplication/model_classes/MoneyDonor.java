package com.example.aswin.myapplication.model_classes;

/**
 * Created by ASWIN on 1/10/2018.
 */

public class MoneyDonor {

    private String name,amount,createdDate;
    private int id;

    public MoneyDonor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "{NAME : "+name+",AMOUNT : "+amount+"}";
    }
}
