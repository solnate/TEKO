package com.HttpTEKO.payload;

public class Payment {
    public int amount;
    public int currency;
    public int exponent;
    public Payment(int amount, int currency, int exponent){
        this.amount = amount;
        this.currency = currency;
        this.exponent = exponent;
    }
}
