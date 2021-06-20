package com.mebank.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class TransactionResult {

    private BigDecimal balFrPeriod;
    private long noOfTransactions;

    public BigDecimal getBalFrPeriod() {
        return balFrPeriod;
    }

    public void setBalFrPeriod(BigDecimal balFrPeriod) {
        this.balFrPeriod = balFrPeriod;
    }

    public long getNoOfTransactions() {
        return noOfTransactions;
    }

    public void setNoOfTransactions(long noOfTransactions) {
        this.noOfTransactions = noOfTransactions;
    }

    @Override
    public String toString() {
        String currency = NumberFormat.getCurrencyInstance(Locale.getDefault())
                .format(balFrPeriod);

        StringBuilder result = new StringBuilder("\nRelative balance for the period is: ")
                .append(currency).append("\nNumber of transactions included is: ")
                .append(noOfTransactions).append("\n");
        return result.toString();
    }
}
