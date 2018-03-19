package com.example.manet.its_partner_test.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringViewFormat {
    private static final int HOUR = 3600*1000;
    private static final int MINUTE = 60*1000;
    private static final int SEC = 1000;
    private static final String STRING_HOUR = "hours: ";
    private static final String STRING_MINUTE = " minute: ";
    private static final String STRING_SEC = " sec: ";
    private static final String STRING_NUMBER = "train number: ";
    private static final String STRING_START_TIME = " start train time: ";
    private static final String STRING_TIME = " train time: ";
    private static final String STRING_COUNTER = " number of turning: ";

    public String stringGenerate(int number, long startTime, long time, int counter){
        StringBuilder format = new StringBuilder();
        format.append(STRING_NUMBER);
        format.append(number);
        format.append('\n');
        format.append(STRING_START_TIME);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("'data 'E yyyy.MM.dd 'time' hh:mm:ss");
        format.append(formatForDateNow.format(startTime));
        format.append('\n');
        format.append(STRING_TIME);
        formatForDateNow = new SimpleDateFormat("'minutes:sec:milisec ' mm:ss:SS");
        format.append(formatForDateNow.format(time));
        format.append('\n');
        format.append(STRING_COUNTER);
        format.append(counter);
        format.append('\n');
        return format.toString();
    }
}
