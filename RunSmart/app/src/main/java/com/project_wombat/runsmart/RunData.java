package com.project_wombat.runsmart;

import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class RunData {
	private long time_stamp;
	private double speed;
	private int time_elapsed;

	public RunData(){}
	public RunData(Date time_stamp, double speed, int time_elapsed)
	{
		this.time_stamp = time_stamp.getTime()/1000;
		this.speed = speed;
		this.time_elapsed = time_elapsed;
	}

	public void setTimeStamp(Date time_stamp) { this.time_stamp = time_stamp.getTime()/1000; }
	public void setSpeed(double speed) { this.speed = speed; }
	public void setTimeElapsed(int time_elapsed) { this.time_elapsed = time_elapsed; }

	public long getTimeStamp() { return time_stamp; }
	public double getSpeed() { return speed; }
	public int getTimeElapsed() { return time_elapsed; }
}