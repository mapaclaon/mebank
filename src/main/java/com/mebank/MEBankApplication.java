package com.mebank;

import com.mebank.constants.TransactionType;
import com.mebank.model.Transaction;
import com.mebank.service.TransactionAnalyser;
import com.mebank.utils.ApplicationUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mebank.constants.ApplicationConstants.AMOUNT_INDEX;
import static com.mebank.constants.ApplicationConstants.COMMA_DELIMITER;
import static com.mebank.constants.ApplicationConstants.CREATE_AT_INDEX;
import static com.mebank.constants.ApplicationConstants.FROM_ACCOUNT_ID_INDEX;
import static com.mebank.constants.ApplicationConstants.RELATED_TRANSACTION_INDEX;
import static com.mebank.constants.ApplicationConstants.TO_ACCOUNT_ID_INDEX;
import static com.mebank.constants.ApplicationConstants.TRANSACTION_ID_INDEX;
import static com.mebank.constants.ApplicationConstants.TRANSACTION_TYPE_INDEX;
import static com.mebank.utils.ApplicationUtils.trim;

/**
 * This application analyses financial transaction records.
 *
 * @author mary paclaon
 */
public class MEBankApplication {

    public static void main(String[] args) throws Exception {
        TransactionAnalyser transactionAnalyser = new TransactionAnalyser(initialise(args[0]));
        transactionAnalyser.analyse();
    }

    /**
     * Initialises a list of transaction records from a comma delimited input file.
     *
     * @param inputFilePath file path to csv
     * @return a list of transaction records
     */
    private static List<Transaction> initialise(String inputFilePath) throws IOException {

        List<Transaction> inputList;

        File csvFile = new File(inputFilePath);
        try(InputStream inputCsvTransaction = new FileInputStream(csvFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputCsvTransaction))) {
            // skip the header of the csv
            inputList = br.lines().skip(1).map(mapToTransaction).collect(Collectors.toList());
        } catch (IOException ex) {
            System.out.println(String.format("An exception occurred while initialising the csv file: %s", ex.getMessage()));
            throw ex;
        }

        return inputList;
    }

    /**
     * Maps the csv row to Transaction object
     */
    private static Function<String, Transaction> mapToTransaction = (trxRow) -> {

        String[] trxCol = trxRow.split(COMMA_DELIMITER);
        Transaction item = new Transaction();

        item.setTransactionId(trim(trxCol[TRANSACTION_ID_INDEX]));
        item.setFromAccountId(trim(trxCol[FROM_ACCOUNT_ID_INDEX]));
        item.setToAccountId(trim(trxCol[TO_ACCOUNT_ID_INDEX]));
        item.setCreateAt(ApplicationUtils.convertStrToDate(trxCol[CREATE_AT_INDEX]));
        item.setAmount(new BigDecimal(trim(trxCol[AMOUNT_INDEX])));
        item.setTransactionType(TransactionType.valueOf(trim(trxCol[TRANSACTION_TYPE_INDEX])));
        if (trxCol.length > 6) {
            item.setRelatedTransaction(trim(trxCol[RELATED_TRANSACTION_INDEX]));
        }

        return item;
    };

}
