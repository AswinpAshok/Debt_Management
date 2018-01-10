package com.example.aswin.myapplication.model_classes;

/**
 * Created by ASWIN on 1/10/2018.
 */

public class MoneyDonor {

    private String name,amount;

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

    @Override
    public String toString() {
        return "{NAME : "+name+",AMOUNT : "+amount+"}";
    }
}
