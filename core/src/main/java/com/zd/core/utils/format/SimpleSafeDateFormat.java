package com.zd.core.utils.format;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 线程安全SimpleDateFormat
 */
public class SimpleSafeDateFormat extends SimpleDateFormat {

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        synchronized (date) {
            return super.format(date, toAppendTo, pos);
        }
    }
}
