package com.assetManager.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RandomIdCreator {

    public static final String BUSINESS_PREFIX = "BS-";
    public static final String TABLE_INFO_PREFIX = "TI-";
    public static final String MENU_PREFIX = "MN-";
    public static final String ACCOUNT_PREFIX = "AC-";

    public static String createBusinessId() {
        return BUSINESS_PREFIX + getRandomId();
    }

    public static String createTableInfoId() {
        return TABLE_INFO_PREFIX + getRandomId();
    }

    public static String createMenuId() {
        return MENU_PREFIX + getRandomId();
    }

    public static String createAccountId() { return ACCOUNT_PREFIX + getRandomId(); }

    private static String getRandomId() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSS");
        LocalDateTime localDate = LocalDateTime.now();
        return dtf.format(localDate);
    }
}
