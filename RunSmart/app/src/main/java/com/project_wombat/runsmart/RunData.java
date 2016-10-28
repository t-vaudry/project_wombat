package com.project_wombat.runsmart;

import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class RunData {
	private long time_stamp;
	private double speed;
	private double longitude;
	private double latitude;
	private long time_elapsed;

	public RunData(){}
	public RunData(Date time_stamp, double speed, double longitude, double latitude, long time_elapsed)
	{
		this.time_stamp = time_stamp.getTime()/1000;
		this.speed = speed;
		this.longitude = longitude;
		this.latitude = latitude;
		this.time_elapsed = time_elapsed;
	}

	public void setTimeStamp(Date time_stamp) { this.time_stamp = time_stamp.getTime()/1000; }
	public void setSpeed(double speed) { this.speed = speed; }
	public void setLongitude(double longitude) { this.longitude = longitude; }
	public void setLatitude(double latitude) { this.latitude = latitude; }
	public void setTimeElapsed(long time_elapsed) { this.time_elapsed = time_elapsed; }

	public long getTimeStamp() { return time_stamp; }
	public double getSpeed() { return speed; }
	public double getLongitude() { return longitude; }
	public double getLatitude() { return latitude; }
	public long getTimeElapsed() { return time_elapsed; }
}