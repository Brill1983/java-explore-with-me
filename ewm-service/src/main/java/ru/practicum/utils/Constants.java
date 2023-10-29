package ru.practicum.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String APP_NAME = "ewm-main";

    public static final String DATE_TIME_PATTERN = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01]) (([0,1][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]$";
}
