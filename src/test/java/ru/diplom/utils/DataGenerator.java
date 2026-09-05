package ru.diplom.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataGenerator {
    
    public static String getApprovedCardNumber() {
        return "4444444444444441";
    }


    public static String getDeclinedCardNumber() {
        return "4444444444444442";
    }


    public static String getValidMonth() {
        LocalDate futureDate = LocalDate.now().plusMonths(1);
        return futureDate.format(DateTimeFormatter.ofPattern("MM"));
    }


    public static String getValidYear() {
        LocalDate futureDate = LocalDate.now().plusYears(1);
        return futureDate.format(DateTimeFormatter.ofPattern("yy"));
    }


    public static String getCardHolder() {
        return "John Doe";
    }


    public static String getValidCvc() {
        return "123";
    }
}
