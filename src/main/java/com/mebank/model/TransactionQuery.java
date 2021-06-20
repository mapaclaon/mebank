package com.mebank.model;

import java.time.LocalDateTime;

public class TransactionQuery {

    private String accountId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        StringBuilder queryStr = new StringBuilder()
                .append("Account Id: ").append(accountId)
                .append(", Date Range: ").append(fromDate)
                .append(" - ").append(toDate);

        return queryStr.toString();
    }
}
