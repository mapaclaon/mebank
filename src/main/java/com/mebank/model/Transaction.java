package com.mebank.model;

import com.mebank.constants.TransactionType;
import com.mebank.utils.ApplicationUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mebank.utils.ApplicationUtils.DATETIME_PATTERN;

public class Transaction {

    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private LocalDateTime createAt;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String relatedTransaction;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public BigDecimal getAmount(String accountId) {
        if (accountId.equals(this.fromAccountId)) {
            return amount.negate();
        } else if (accountId.equals(this.toAccountId)) {
            return amount;
        }
        return BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getRelatedTransaction() {
        return relatedTransaction;
    }

    public void setRelatedTransaction(String relatedTransaction) {
        this.relatedTransaction = relatedTransaction;
    }

    public Boolean isPayment() {
        return this.transactionType.equals(TransactionType.PAYMENT);
    }

    public Boolean isTransRelatedWithAccount(String accountId) {
        return accountId.equals(fromAccountId) || accountId.equals(toAccountId);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("| ").append(transactionId)
                .append(" | ").append(fromAccountId)
                .append(" | ").append(toAccountId)
                .append(" | ").append(createAt)
                .append(" | ").append(amount)
                .append(" | ").append(transactionType)
                .append(" | ").append(relatedTransaction).append(" |");
        return stringBuilder.toString();
    }
}
