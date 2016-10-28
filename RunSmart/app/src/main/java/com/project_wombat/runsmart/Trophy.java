package com.project_wombat.runsmart;

import java.util.Date;

/**
 * Created by thoma on 10/23/16
 */

public class Trophy {
	private int type;
	private int value;
	private long date_achieved;

	public Trophy() {}
	public Trophy(int type, int value, Date date_achieved)
	{
		this.type = type;
		this.value = value;
		this.date_achieved = date_achieved.getTime()/1000;
	}

	public void setType(int type) { this.type = type; }
	public void setValue(int value) { this.value = value; }
	public void setDateAchieved(Date date_achieved) { this.date_achieved = date_achieved.getTime()/1000; }

	public int getType() { return type; }
	public int getValue() { return value; }
	public long getDateAchieved() { return date_achieved; }
}