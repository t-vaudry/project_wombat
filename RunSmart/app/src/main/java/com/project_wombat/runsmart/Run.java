package com.project_wombat.runsmart;

import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class Run {
    private long time_stamp;
    private double distance;
    private long duration;
    private double avg_speed;
    private double top_speed;
    private double heart_rate_before;
    private double heart_rate_after;

    public Run()
    {
        this.time_stamp = 0;
        this.distance = 0;
        this.duration = 0;
        this.avg_speed = 0;
        this.top_speed = 0;
        this.heart_rate_after = 0;
        this.heart_rate_before = 0;
    }

    public Run(Date time_stamp, double distance, long duration, double avg_speed, double top_speed, double heart_rate_before, double heart_rate_after)
    {
        this.time_stamp = time_stamp.getTime()/1000;
        this.distance = distance;
        this.duration = duration;
        this.avg_speed = avg_speed;
        this.top_speed = top_speed;
        this.heart_rate_before = heart_rate_before;
        this.heart_rate_after = heart_rate_after;
    }

    public void setTimeStamp(Date time_stamp) { this.time_stamp = time_stamp.getTime()/1000; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setDuration(long duration) { this.duration = duration; }
    public void setAvgSpeed(double avg_speed) { this.avg_speed = avg_speed; }
    public void setTopSpeed(double top_speed) { this.top_speed = top_speed; }
    public void setHeartRateBefore(double heart_rate_before) { this.heart_rate_before = heart_rate_before; }
    public void setHeartRateAfter(double heart_rate_after) { this.heart_rate_after = heart_rate_after; }

    public long getTimeStamp() { return time_stamp; }
    public double getDistance() { return distance; }
    public long getDuration() { return duration; }
    public double getAvgSpeed() { return avg_speed; }
    public double getTopSpeed() { return top_speed; }
    public double getHeartRateBefore() { return heart_rate_before; }
    public double getHeartRateAfter() { return heart_rate_after; }
}