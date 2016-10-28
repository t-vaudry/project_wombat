package com.project_wombat.runsmart;

import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class Run {
    private long time_stamp;
    private double distance;
    private int duration;
    private double avg_speed;
    private double top_speed;
    private double heart_rate_before;
    private double heart_rate_after;

    public Run(){}
    public Run(Date time_stamp, double distance, int duration, double avg_speed, double top_speed, double heart_rate_before, double heart_rate_after)
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
    public void setDuration(int duration) { this.duration = duration; }
    public void setAvgSpeed(double avg_speed) { this.avg_speed = avg_speed; }
    public void setTopSpeed(double top_speed) { this.top_speed = top_speed; }
    public void setHeartRateBefore(double heart_rate_before) { this.heart_rate_before = heart_rate_before; }
    public void setHeartRateAfter(double heart_rate_after) { this.heart_rate_after = heart_rate_after; }

    public long getTimeStamp() { return time_stamp; }
    public double getDistance() { return distance; }
    public int getDuration() { return duration; }
    public double getAvgSpeed() { return avg_speed; }
    public double getTopSpeed() { return top_speed; }
    public double getHeartRateBefore() { return heart_rate_before; }
    public double getHeartRateAfter() { return heart_rate_after; }
}