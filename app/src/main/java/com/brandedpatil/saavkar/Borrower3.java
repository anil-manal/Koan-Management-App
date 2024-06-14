package com.brandedpatil.saavkar;

import java.util.Map;

public class Borrower3 {
    private String borrowerId;
    private int collectionDay;
    private Map<String, Map<String, Map<String, Object>>> interestPaid;

    public Borrower3() {}

    public Borrower3(String borrowerId, int collectionDay, Map<String, Map<String, Map<String, Object>>> interestPaid) {
        this.borrowerId = borrowerId;
        this.collectionDay = collectionDay;
        this.interestPaid = interestPaid;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
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
}
