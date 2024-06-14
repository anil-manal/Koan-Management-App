package com.brandedpatil.saavkar;

import java.util.Date;

public class CollectionBorrower {
    private String borrowerName;
    private double amountToCollect;
    private Date collectionDate;
    private Date loanStartDate; // Adding loan start date property
    private String monthlyInterest; // Adding monthly interest property

    public CollectionBorrower() {
        // Default constructor required for Firebase deserialization
    }

    public CollectionBorrower(String borrowerName, double amountToCollect, Date collectionDate, Date loanStartDate, String monthlyInterest) {
        this.borrowerName = borrowerName;
        this.amountToCollect = amountToCollect;
        this.collectionDate = collectionDate;
        this.loanStartDate = loanStartDate; // Initialize loan start date
        this.monthlyInterest = monthlyInterest;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public double getAmountToCollect() {
        return amountToCollect;
    }

    public void setAmountToCollect(double amountToCollect) {
        this.amountToCollect = amountToCollect;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Date getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(Date loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public String getMonthlyInterest() {
        return monthlyInterest;
    }

    public void setMonthlyInterest(String monthlyInterest) {
        this.monthlyInterest = monthlyInterest;
    }
}
