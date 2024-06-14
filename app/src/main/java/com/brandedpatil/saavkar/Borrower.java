package com.brandedpatil.saavkar;

import java.util.Date;
import java.util.Map;

public class Borrower {
    private String borrowerId;
    private String borrowerName;
    private String mobileNumber;
    private String amountToBorrow;
    private String interestRate;
    private String monthlyInterest;
    private String loanDuration;
    private Date loanStartDate;
    private String loanStatus;
    private int collectionDay;
    private Map<String, Map<String, Map<String, Object>>> interestPaid;
    private boolean interestCollectedForCurrentMonth;

    public Borrower() {
        // Default constructor required for Firebase
    }

    public Borrower(String borrowerId, String borrowerName, String mobileNumber, String amountToBorrow,
                    String interestRate, String monthlyInterest, String loanDuration, Date loanStartDate,
                    String loanStatus, int collectionDay, Map<String, Map<String, Map<String, Object>>> interestPaid) {
        this.borrowerId = borrowerId;
        this.borrowerName = borrowerName;
        this.mobileNumber = mobileNumber;
        this.amountToBorrow = amountToBorrow;
        this.interestRate = interestRate;
        this.monthlyInterest = monthlyInterest;
        this.loanDuration = loanDuration;
        this.loanStartDate = loanStartDate;
        this.loanStatus = loanStatus;
        this.collectionDay = collectionDay;
        this.interestPaid = interestPaid;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAmountToBorrow() {
        return amountToBorrow;
    }

    public void setAmountToBorrow(String amountToBorrow) {
        this.amountToBorrow = amountToBorrow;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getMonthlyInterest() {
        return monthlyInterest;
    }

    public void setMonthlyInterest(String monthlyInterest) {
        this.monthlyInterest = monthlyInterest;
    }

    public String getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(String loanDuration) {
        this.loanDuration = loanDuration;
    }

    public Date getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(Date loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getCollectionDay() {
        return collectionDay;
    }

    public void setCollectionDay(int collectionDay) {
        this.collectionDay = collectionDay;
    }

    public Map<String, Map<String, Map<String, Object>>> getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(Map<String, Map<String, Map<String, Object>>> interestPaid) {
        this.interestPaid = interestPaid;
    }

    public boolean isInterestCollectedForCurrentMonth() {
        return interestCollectedForCurrentMonth;
    }

    public void setInterestCollectedForCurrentMonth(boolean interestCollectedForCurrentMonth) {
        this.interestCollectedForCurrentMonth = interestCollectedForCurrentMonth;
    }
}
