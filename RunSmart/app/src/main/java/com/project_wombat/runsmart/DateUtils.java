package com.project_wombat.runsmart;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thoma on 10/24/16
 */

public class DateUtils {

    private DateUtils() { throw new AssertionError(); }
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static Date parse(String toParse)
    {
        Date date = new Date();
        try {
            date = df.parse(toParse);
        }
        catch (ParseException e) {
        	Log.i("Date error", "Date error");
        }
        return date;
    }

    public static String format(long toFormat)
    {
        return df.format(new Date(toFormat*1000));
    }
}