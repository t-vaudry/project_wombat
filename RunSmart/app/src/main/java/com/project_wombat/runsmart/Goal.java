package com.project_wombat.runsmart;

import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class Goal {
	private int goal_type;
	private int time_type;
	private int value;
	private long start_date;
	private long end_date;

	public Goal(){}
	public Goal(int goal_type, int time_type, int value, Date start_date, Date end_date)
	{
		this.goal_type = goal_type;
		this.time_type = time_type;
		this.value = value;
		this.start_date = start_date.getTime()/1000;
		this.end_date = end_date.getTime()/1000;
	}

	public void setGoalType(int goal_type) { this.goal_type = goal_type; }
	public void setTimeType(int time_type) { this.time_type = time_type; }
	public void setValue(int value) { this.value = value; }
	public void setStart_date(Date start_date) {this.start_date = start_date.getTime()/1000;}
	public void setEndDate(Date end_date) { this.end_date = end_date.getTime()/1000; }

	public int getGoalType() { return goal_type; }
	public int getTimeType() { return time_type; }
	public int getValue() { return value; }
	public long getStart_date(){return start_date;}
	public long getEndDate() { return end_date; }
}