package com.mebank.service;

import com.mebank.constants.TransactionType;
import com.mebank.model.Transaction;
import com.mebank.model.TransactionQuery;
import com.mebank.model.TransactionResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class computes for the transactions' relative account balance (positive or negative)
 * based on a time frame into a resulting <code>TransactionResult</code>.
 *
 * @author mary paclaon
 */
public class TransactionService {

    /**
     * Returns a <code>TransactionResult</code> that stores the computed account
     * balance, as well as the number of transactions, from the given time frame.
     *
     * The balance is the sum of funds that were transferred to/ from an account in a given
     * time frame; it does not account for funds that were in that account prior to the time frame.
     *
     * @param transactionList the transactions
     * @param query the filtering criteria
     */
    public TransactionResult getResult(List<Transaction> transactionList, TransactionQuery query) {

        TransactionResult transactionResult = new TransactionResult();
        //for filtering
        Set<String> reversedTransactions = getReversedTransactions(transactionList);

        List<Transaction> results = transactionList.stream()
                .filter(trx -> trx.isTransRelatedWithAccount(query.getAccountId()))
                .filter(trx -> !trx.getCreateAt().isBefore(query.getFromDate())
                        && !trx.getCreateAt().isAfter(query.getToDate()))
                .filter(Transaction::isPayment)
                .filter(trx -> !(reversedTransactions.contains(trx.getTransactionId())))
                .collect(Collectors.toList());

        BigDecimal amounts = BigDecimal.ZERO;
        if (results.size() > 0) {
            amounts = results.stream().map(t -> t.getAmount(query.getAccountId()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        transactionResult.setNoOfTransactions(results.size());
        transactionResult.setBalFrPeriod(amounts);

        return transactionResult;
    }

    /**
     * The transaction ids to be omitted from the calculation.
     *
     * @param transactions
     * @return reversed transaction Ids
     */
    private Set<String> getReversedTransactions(List<Transaction> transactions){
        return  transactions.stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.REVERSAL))
                .map(Transaction::getRelatedTransaction)
                .collect(Collectors.toSet());
    }

}
