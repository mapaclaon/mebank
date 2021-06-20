package com.mebank.service;

import com.mebank.constants.TransactionType;
import com.mebank.model.Transaction;
import com.mebank.model.TransactionQuery;
import com.mebank.model.TransactionResult;
import com.mebank.utils.ApplicationUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionServiceTest {

    private List<Transaction> transactions = getTransactions();
    private TransactionService service = new TransactionService();
    private TransactionQuery query;
    private TransactionResult result;

    @Before
    public void setUpTransactions() {
        this.query = new TransactionQuery();
    }

    @Test
    public void testCodeChallengeSampleDataAndInput() {
        List<Transaction> transactions = getCodeChallengeSampleData();
        result = service.getResult(transactions, getCodeChallengeSampleQuery());

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance for the period is: $-25.00", result.getBalFrPeriod(), new BigDecimal(-25));
        Assert.assertEquals("Number of transactions included is: 1", result.getNoOfTransactions(), 1);
    }

    @Test
    public void testResultWithNoMatchingAccountId() {
        query = getTransQueryWithNoTransReturn();
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance from period should be $0.00", result.getBalFrPeriod(), BigDecimal.ZERO);
    }

    @Test
    public void testResultWithOneMatchingTransaction() {
        query = getTransQueryWithOneTransReturn();
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance should be $10.00", BigDecimal.TEN, result.getBalFrPeriod());
        Assert.assertEquals("Transaction count should be 1", 1L, result.getNoOfTransactions());
    }

    @Test
    public void testResultWithAcct1() {
        query = getTransQueryWithAccTransReturn("AC0001", "15/06/2021 08:00:00", "22/06/2021 08:00:00");
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance should be $2.00", BigDecimal.valueOf(2), result.getBalFrPeriod());
        Assert.assertEquals("Transaction count should be 2", 2L, result.getNoOfTransactions());
    }

    @Test
    public void testResultWithAcct1PastTrx() {
        query = getTransQueryWithAccTransReturn("AC0001", "22/06/2020 08:00:00", "14/06/2021 09:59:59");
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance should be $0.00", BigDecimal.ZERO, result.getBalFrPeriod());
        Assert.assertEquals("Transaction count should be 0", 0, result.getNoOfTransactions());
    }

    @Test
    public void testResultWithAcct1TrxOnFrom() {
        //matched one transaction but got reversal
        query = getTransQueryWithAccTransReturn("AC0001", "22/06/2020 08:00:00", "14/06/2021 10:00:00");
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance should be $0.00", BigDecimal.ZERO, result.getBalFrPeriod());
        Assert.assertEquals("Transaction count should be 0", 0, result.getNoOfTransactions());
    }

    @Test
    public void testResultWithAcct12NegativeBalTrx() {
        query = getTransQueryWithAccTransReturn("AC0012","16/06/2021 08:00:00","16/06/2021 12:00:00");
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance should be -$12.00", BigDecimal.valueOf(-12), result.getBalFrPeriod());
        Assert.assertEquals("Transaction count should be 1", 1, result.getNoOfTransactions());
    }

    @Test
    public void testResultWithAcct12ExactDateMatchTrx() {
        query = getTransQueryWithAccTransReturn("AC0012","16/06/2021 10:00:00","16/06/2021 10:00:00");
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance should be -$12.00", BigDecimal.valueOf(-12), result.getBalFrPeriod());
        Assert.assertEquals("Transaction count should be 1", 1, result.getNoOfTransactions());
    }

    @Test
    public void testResultWithAcct12AfterDateMatchTrx() {
        query = getTransQueryWithAccTransReturn("AC0012","17/06/2021 10:00:00","25/06/2021 10:00:00");
        result = service.getResult(transactions, query);

        audit(transactions, query, result);
        Assert.assertEquals("Relative balance should be $0.00", BigDecimal.ZERO, result.getBalFrPeriod());
        Assert.assertEquals("Transaction count should be 0", 0, result.getNoOfTransactions());
    }

    private List<Transaction> getTransactions(){
        List<Transaction> transList = new ArrayList<>();

        Transaction trans = new Transaction();
        trans.setTransactionId("TX0001");
        trans.setFromAccountId("AC0010");
        trans.setToAccountId("AC0001");
        trans.setAmount(new BigDecimal(12));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("14/06/2021 10:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0002");
        trans.setFromAccountId("AC0010");
        trans.setToAccountId("AC0001");
        trans.setAmount(new BigDecimal(12));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("14/06/2021 11:00:00"));
        trans.setTransactionType(TransactionType.REVERSAL);
        trans.setRelatedTransaction("TX0001");
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0003");
        trans.setFromAccountId("AC0001");
        trans.setToAccountId("AC0011");
        trans.setAmount(BigDecimal.TEN);
        trans.setCreateAt(ApplicationUtils.convertStrToDate("15/06/2021 08:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0004");
        trans.setFromAccountId("AC0010");
        trans.setToAccountId("AC0001");
        trans.setAmount(new BigDecimal(100));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("15/06/2021 10:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0005");
        trans.setFromAccountId("AC0002");
        trans.setToAccountId("AC0012");
        trans.setAmount(new BigDecimal(48));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("15/06/2021 09:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0006");
        trans.setFromAccountId("AC0002");
        trans.setToAccountId("AC0001");
        trans.setAmount(new BigDecimal(12));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("15/06/2021 10:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0007");
        trans.setFromAccountId("AC0012");
        trans.setToAccountId("AC0002");
        trans.setAmount(new BigDecimal(12));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("16/06/2021 10:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0008");
        trans.setFromAccountId("AC0012");
        trans.setToAccountId("AC0002");
        trans.setAmount(new BigDecimal(19));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("16/06/2021 13:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX0009");
        trans.setFromAccountId("AC0010");
        trans.setToAccountId("AC0001");
        trans.setAmount(new BigDecimal(100));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("17/06/2021 08:00:00"));
        trans.setTransactionType(TransactionType.REVERSAL);
        trans.setRelatedTransaction("TX0004");
        transList.add(trans);

        return transList;
    }

    private TransactionQuery getTransQueryWithNoTransReturn() {
        query.setAccountId("ABCDEFG");
        query.setFromDate(ApplicationUtils.convertStrToDate("14/06/2021 10:00:00"));
        query.setToDate(ApplicationUtils.convertStrToDate("17/06/2021 08:00:00"));

        return query;
    }

    private TransactionQuery getTransQueryWithOneTransReturn() {
        query.setAccountId("AC0011");
        query.setFromDate(ApplicationUtils.convertStrToDate("15/06/2021 08:00:00"));
        query.setToDate(ApplicationUtils.convertStrToDate("22/06/2021 08:00:00"));

        return query;
    }

    private TransactionQuery getTransQueryWithAccTransReturn(String acctId, String fromDate, String toDate) {
        query.setAccountId(acctId);
        query.setFromDate(ApplicationUtils.convertStrToDate(fromDate));
        query.setToDate(ApplicationUtils.convertStrToDate(toDate));

        return query;
    }

    private void audit(List<Transaction> transactions, TransactionQuery query, TransactionResult result) {
        System.out.println("| Trans  | From   | To     |");
        transactions.forEach(System.out::println);
        System.out.print("\n" + query);
        System.out.println(result);
    }

    private List<Transaction> getCodeChallengeSampleData() {
        List<Transaction> transList = new ArrayList<>();

        Transaction trans = new Transaction();
        trans.setTransactionId("TX10001");
        trans.setFromAccountId("ACC334455");
        trans.setToAccountId("ACC778899");
        trans.setAmount(new BigDecimal(25));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("20/10/2018 12:47:55"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX10002");
        trans.setFromAccountId("ACC334455");
        trans.setToAccountId("ACC998877");
        trans.setAmount(new BigDecimal(10.50));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("20/10/2018 17:33:43"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX10003");
        trans.setFromAccountId("ACC998877");
        trans.setToAccountId("ACC778899");
        trans.setAmount(new BigDecimal(5));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("20/10/2018 18:00:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX10004");
        trans.setFromAccountId("ACC334455");
        trans.setToAccountId("ACC998877");
        trans.setAmount(new BigDecimal(10.50));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("20/10/2018 19:45:00"));
        trans.setTransactionType(TransactionType.REVERSAL);
        trans.setRelatedTransaction("TX10002");
        transList.add(trans);

        trans = new Transaction();
        trans.setTransactionId("TX10005");
        trans.setFromAccountId("ACC334455");
        trans.setToAccountId("ACC778899");
        trans.setAmount(new BigDecimal(7.25));
        trans.setCreateAt(ApplicationUtils.convertStrToDate("21/10/2018 09:30:00"));
        trans.setTransactionType(TransactionType.PAYMENT);
        transList.add(trans);

        return transList;
    }

    private TransactionQuery getCodeChallengeSampleQuery() {
        query.setAccountId("ACC334455");
        query.setFromDate(ApplicationUtils.convertStrToDate("20/10/2018 12:00:00"));
        query.setToDate(ApplicationUtils.convertStrToDate("20/10/2018 19:00:00"));

        return query;
    }
}
