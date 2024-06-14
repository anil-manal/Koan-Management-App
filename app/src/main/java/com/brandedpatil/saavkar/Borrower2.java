package com.brandedpatil.saavkar;

import java.util.Map;

public class Borrower2 {
    private String amountToBorrow;
    private String borrowerId;
    private String borrowerName;
    private int collectionDay;
    private String interestRate;
    private String loanDuration;
    private Map<String, Map<String, Map<String, Object>>> interestPaid; // Assuming you have this structure
    private String loanStatus;
    private String mobileNumber;
    private String monthlyInterest;
    private String remainingInterest;
    private InterestPaidDetails interestPaidDetails; // New field for interest paid details

    // Constructor
    public Borrower2() {
        // Default constructor required for Firebase
    }

    // Getters and setters
    public String getAmountToBorrow() {
        return amountToBorrow;
    }

    public void setAmountToBorrow(String amountToBorrow) {
        this.amountToBorrow = amountToBorrow;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public int getCollectionDay() {
        return collectionDay;
    }

    public void setCollectionDay(int collectionDay) {
        this.collectionDay = collectionDay;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(String loanDuration) {
        this.loanDuration = loanDuration;
    }

    public Map<String, Map<String, Map<String, Object>>> getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(Map<String, Map<String, Map<String, Object>>> interestPaid) {
        this.interestPaid = interestPaid;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMonthlyInterest() {
        return monthlyInterest;
    }

    public void setMonthlyInterest(String monthlyInterest) {
        this.monthlyInterest = monthlyInterest;
    }

    public String getRemainingInterest() {
        return remainingInterest;
    }

    public void setRemainingInterest(String remainingInterest) {
        this.remainingInterest = remainingInterest;
    }

    public InterestPaidDetails getInterestPaidDetails() {
        return interestPaidDetails;
    }

    public void setInterestPaidDetails(InterestPaidDetails interestPaidDetails) {
        this.interestPaidDetails = interestPaidDetails;
    }
}
