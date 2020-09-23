package com.assetManager.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RandomIdCreator {

    public static String create() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSS");
        LocalDateTime localDate = LocalDateTime.now();
        return dtf.format(localDate);
    }
}
