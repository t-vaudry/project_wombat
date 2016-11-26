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
    private static final DateFormat dtf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    private static final DateFormat displayDate = new SimpleDateFormat("EEE, MMM d, yyyy");
    private static final DateFormat displayTime = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss");
    private static final DateFormat timeFormat = new SimpleDateFormat("mm:ss");

    public static Date parse(String toParse)
    {
        Date date = new Date();
        try {
            date = dtf.parse(toParse);
        }
        catch (ParseException e) {
        	Log.i("Date error", "Date error");
        }
        return date;
    }

    public static String format(long toFormat, boolean datetime)
    {
        if(datetime)
            return dtf.format(new Date(toFormat*1000));
        else
            return df.format(new Date(toFormat*1000));
    }

    public static String display(long toFormat, boolean datetime)
    {
        if(datetime)
            return displayTime.format(new Date(toFormat*1000));
        else
            return displayDate.format(new Date(toFormat*1000));
    }

    public static String formatTime(long toFormat)
    {
        return timeFormat.format(new Date(toFormat));
    }
}