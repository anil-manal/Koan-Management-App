package com.brandedpatil.saavkar;

public class InterestPaidDetails {
    private String date;
    private double amountCollected;
    private double remainingInterest;

    public InterestPaidDetails(String date, double amountCollected, double remainingInterest) {
        this.date = date;
        this.amountCollected = amountCollected;
        this.remainingInterest = remainingInterest;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(double amountCollected) {
        this.amountCollected = amountCollected;
    }

    public double getRemainingInterest() {
        return remainingInterest;
    }

    public void setRemainingInterest(double remainingInterest) {
        this.remainingInterest = remainingInterest;
    }
}
