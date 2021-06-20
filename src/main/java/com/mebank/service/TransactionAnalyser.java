package com.mebank.service;

import com.mebank.model.Transaction;
import com.mebank.model.TransactionQuery;
import com.mebank.model.TransactionResult;
import com.mebank.utils.ApplicationUtils;

import java.util.List;
import java.util.Scanner;

/**
 * This class returns analyses of transactions based on input queries.
 *
 * @author mary paclaon
 */
public class TransactionAnalyser {

    private final List<Transaction> transactions;

    public TransactionAnalyser(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void analyse() {
        TransactionQuery query = getTransactionQuery();
        TransactionService service = new TransactionService();
        TransactionResult result = service.getResult(transactions, query);
        System.out.printf("%s", result);
    }

    private TransactionQuery getTransactionQuery() {
        TransactionQuery query = new TransactionQuery();
        try (Scanner scanner = new Scanner(System.in).useDelimiter("\n")) {
            System.out.print("accountId: ");
            query.setAccountId(scanner.next());

            //prompt for the date range e.g 20/10/2018 12:00:00
            //assumption: date range is entered correctly
            System.out.print("from: ");
            query.setFromDate(ApplicationUtils.convertStrToDate(scanner.next()));
            System.out.print("to: ");
            query.setToDate(ApplicationUtils.convertStrToDate(scanner.next()));
        }

        return query;
    }

}
