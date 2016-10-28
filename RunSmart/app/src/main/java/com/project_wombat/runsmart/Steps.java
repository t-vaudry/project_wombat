package com.project_wombat.runsmart;

import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class Steps {
    private long time_stamp;
    private int steps;

    public Steps(){}
    public Steps(Date time_stamp, int steps)
    {
        this.time_stamp = time_stamp.getTime()/1000;
        this.steps = steps;
    }

    public void setTimeStamp(Date time_stamp) { this.time_stamp = time_stamp.getTime()/1000; }
    public void setSteps(int steps) { this.steps = steps; }

    public long getTimeStamp() { return time_stamp; }
    public int getSteps() { return steps; }
}