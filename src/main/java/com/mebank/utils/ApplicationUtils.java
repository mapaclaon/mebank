package com.mebank.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationUtils {

    public static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    public static LocalDateTime convertStrToDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        LocalDateTime dateTime = LocalDateTime.parse(trim(strDate), formatter);

        return dateTime;
    }

    public static String trim(String strToTrim) {
        return strToTrim.trim();
    }
}
