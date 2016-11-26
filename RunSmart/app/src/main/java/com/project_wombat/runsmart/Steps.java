package com.project_wombat.runsmart;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class Steps {
    private long time_stamp;
    private int steps;

    public Steps()
    {
        this.time_stamp = 0;
        this.steps = 0;
    }

    public Steps(Date time_stamp, int steps)
    {
        Calendar now = Calendar.getInstance();
        now.setTime(time_stamp);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        time_stamp = now.getTime();
        this.time_stamp = time_stamp.getTime()/1000;
        this.steps = steps;
    }

    public void setTimeStamp(Date time_stamp) { this.time_stamp = time_stamp.getTime()/1000; }
    public void setSteps(int steps) { this.steps = steps; }

    public long getTimeStamp() { return time_stamp; }
    public int getSteps() { return steps; }
}