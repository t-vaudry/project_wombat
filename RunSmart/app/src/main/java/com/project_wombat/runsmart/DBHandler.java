package com.project_wombat.runsmart;

/**
 * Created by thoma on 10/23/16
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
	// Database version
	private static final int DATABASE_VERSION = 1;
	// Database name
	private static final String DATABASE_NAME = "runSmart";
	// Table names
	private static final String TABLE_RUNS = "runs";
	private static final String TABLE_STEPS = "steps";
	private static final String TABLE_RUNDATA = "runData";
	private static final String TABLE_PROFILE = "profile";
	private static final String TABLE_GOALS = "goals";
	private static final String TABLE_TROPHIES = "trophies";
	//Column names
	private static final String KEY_AGE = "age";
	private static final String KEY_AVERAGE_SPEED = "average_speed";
	private static final String KEY_DATE_ACHIEVED = "date_achieved";
	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_DURATION = "duration";
	private static final String KEY_END_DATE = "end_date";
	private static final String KEY_GOAL_TYPE = "goal_type";
	private static final String KEY_HEART_RATE_AFTER = "heart_rate_after";
	private static final String KEY_HEART_RATE_BEFORE = "heart_rate_before";
	private static final String KEY_HEIGHT = "height";
	private static final String KEY_ID = "id";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_NAME = "name";
	private static final String KEY_SEX = "sex";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_STEPS = "steps";
	private static final String KEY_TIME_ELAPSED = "time_elapsed";
	private static final String KEY_TIME_STAMP = "time_stamp";
	private static final String KEY_TIME_TYPE = "time_type";
	private static final String KEY_TOP_SPEED = "top_speed";
	private static final String KEY_TYPE = "type";
	private static final String KEY_VALUE = "value";
	private static final String KEY_WEIGHT = "weight";

	public DBHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Create runs table
		String CREATE_TABLE = "CREATE TABLE " + TABLE_RUNS + "("
		+ KEY_TIME_STAMP + " INTEGER PRIMARY KEY," + KEY_DISTANCE
		+ " REAL," + KEY_DURATION + " REAL," + KEY_AVERAGE_SPEED
		+ " REAL," + KEY_TOP_SPEED + " REAL," + KEY_HEART_RATE_BEFORE
		+ " REAL," + KEY_HEART_RATE_AFTER + " REAL" + ")";
		db.execSQL(CREATE_TABLE);

		// Create steps table
		CREATE_TABLE = "CREATE TABLE " + TABLE_STEPS + "("
		+ KEY_TIME_STAMP + " INTEGER PRIMARY KEY," + KEY_STEPS + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);

		// Create profile table
		CREATE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "("
		+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ KEY_NAME + " TEXT," + KEY_AGE + " INTEGER,"
		+ KEY_SEX + " TEXT," + KEY_WEIGHT + " INTEGER,"
		+ KEY_HEIGHT + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);

		// Create runData table
		CREATE_TABLE = "CREATE TABLE " + TABLE_RUNDATA + "("
		+ KEY_TIME_STAMP + " INTEGER," + KEY_SPEED
		+ " REAL," + KEY_LATITUDE + " REAL," + KEY_LONGITUDE
		+ " REAL," + KEY_TIME_ELAPSED + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);

		// Create goals table
		CREATE_TABLE = "CREATE TABLE " + TABLE_GOALS + "("
		+ KEY_GOAL_TYPE + " INTEGER PRIMARY KEY," + KEY_TIME_TYPE
		+ " INTEGER," + KEY_VALUE + " INTEGER," + KEY_END_DATE
		+ " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);

		// Create trophies table
		CREATE_TABLE = "CREATE TABLE " + TABLE_TROPHIES + "("
		+ KEY_TYPE + " INTEGER PRIMARY KEY," + KEY_VALUE
		+ " INTEGER," + KEY_DATE_ACHIEVED + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUNS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUNDATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TROPHIES);

		// Create tables again
		onCreate(db);
	}

	// Adding new run
	public void addRun(Run run)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TIME_STAMP, run.getTimeStamp());
		values.put(KEY_DISTANCE, run.getDistance());
		values.put(KEY_DURATION, run.getDuration());
		values.put(KEY_AVERAGE_SPEED, run.getAvgSpeed());
		values.put(KEY_TOP_SPEED, run.getTopSpeed());
		values.put(KEY_HEART_RATE_BEFORE, run.getHeartRateBefore());
		values.put(KEY_HEART_RATE_AFTER, run.getHeartRateAfter());

		// Inserting row
		db.insert(TABLE_RUNS, null, values);
		db.close(); // Close database connection
	}

	// Getting one run
	public Run getRun(Date time_stamp)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_RUNS + " WHERE " + KEY_TIME_STAMP + " = " + Long.toString(time_stamp.getTime()/1000);
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if(cursor != null)
			cursor.moveToFirst();

		Run run = new Run(
		new Date(Long.parseLong(cursor.getString(0))*1000),
		Double.parseDouble(cursor.getString(1)),
		Long.parseLong(cursor.getString(2)),
		Double.parseDouble(cursor.getString(3)),
		Double.parseDouble(cursor.getString(4)),
		Double.parseDouble(cursor.getString(5)),
		Double.parseDouble(cursor.getString(6))
		);

        cursor.close();
		return run;
	}

	public List<Run> getAllRuns()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<Run> runList = new ArrayList<Run>();

		String selectQuery = "SELECT * FROM " + TABLE_RUNS + " ORDER BY " + KEY_TIME_STAMP;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if(cursor != null)
			cursor.moveToFirst();

		do
		{
			Run run = new Run();
			run.setTimeStamp(new Date(Long.parseLong(cursor.getString(0))*1000));
			run.setDistance(Double.parseDouble(cursor.getString(1)));
			run.setDuration(Long.parseLong(cursor.getString(2)));
			run.setAvgSpeed(Double.parseDouble(cursor.getString(3)));
			run.setTopSpeed(Double.parseDouble(cursor.getString(4)));
			run.setHeartRateBefore(Double.parseDouble(cursor.getString(5)));
			run.setHeartRateAfter(Double.parseDouble(cursor.getString(6)));

			// Add run to list
			runList.add(run);
		} while (cursor.moveToNext());

        cursor.close();
		// return run list
		return runList;
	}

	public List<Run> getBoundedRuns(Date start, Date end)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<Run> runList = new ArrayList<Run>();

		String selectQuery = "SELECT * FROM " + TABLE_RUNS + " WHERE " + KEY_TIME_STAMP + " BETWEEN " + start.getTime()/1000 + " AND " + end.getTime()/1000;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do 
			{
				Run run = new Run();
				run.setTimeStamp(new Date(Long.parseLong(cursor.getString(0))*1000));
				run.setDistance(Double.parseDouble(cursor.getString(1)));
				run.setDuration(Long.parseLong(cursor.getString(2)));
				run.setAvgSpeed(Double.parseDouble(cursor.getString(3)));
				run.setTopSpeed(Double.parseDouble(cursor.getString(4)));
				run.setHeartRateBefore(Double.parseDouble(cursor.getString(5)));
				run.setHeartRateAfter(Double.parseDouble(cursor.getString(6)));
				
				// Add run to list
				runList.add(run);
			} while (cursor.moveToNext());
		}

        cursor.close();
		// return run list
		return runList;
	}

	// Deleting a run
	public void deleteRun(Date time_stamp)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RUNS, KEY_TIME_STAMP + " = ?",
		new String[] { Long.toString(time_stamp.getTime()/1000) });
		db.close();
	}

	// Adding new steps
	public void addSteps(Steps step)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TIME_STAMP, step.getTimeStamp());
		values.put(KEY_STEPS, step.getSteps());

		// Inserting row
		db.insert(TABLE_STEPS, null, values);
		db.close(); // Close database connection
	}

	// Getting one steps
	public Steps getSteps(Date time_stamp)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STEPS, new String[]{KEY_TIME_STAMP,
		KEY_STEPS}, KEY_TIME_STAMP + "?=", new String[]{Long.toString(time_stamp.getTime()/1000)}, null, null, null);

        Steps step = new Steps();

		if(cursor.moveToFirst())
        {
            step = new Steps(
            new Date(Long.parseLong(cursor.getString(0))*1000),
            Integer.parseInt(cursor.getString(1))
            );
        }

        cursor.close();
		return step;
	}

	public List<Steps> getAllSteps()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<Steps> stepsList = new ArrayList<Steps>();

		String selectQuery = "SELECT * FROM " + TABLE_STEPS + " ORDER BY " + KEY_TIME_STAMP;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do 
			{
				Steps step = new Steps();
				step.setTimeStamp(new Date(Long.parseLong(cursor.getString(0))*1000));
				step.setSteps(Integer.parseInt(cursor.getString(1)));
				
				// Add steps to list
				stepsList.add(step);
			} while (cursor.moveToNext());
		}

        cursor.close();
		// return steps list
		return stepsList;
	}

	public List<Steps> getBoundedSteps(Date start, Date end)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<Steps> stepsList = new ArrayList<Steps>();

		String selectQuery = "SELECT * FROM " + TABLE_STEPS + " WHERE " + KEY_TIME_STAMP + " BETWEEN " + start.getTime()/1000 + " AND " + end.getTime()/1000;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do 
			{
				Steps step = new Steps();
				step.setTimeStamp(new Date(Long.parseLong(cursor.getString(0))*1000));
				step.setSteps(Integer.parseInt(cursor.getString(1)));
				
				// Add steps to list
				stepsList.add(step);
			} while (cursor.moveToNext());
		}

        cursor.close();
		// return steps list
		return stepsList;
	}

	// Deleting a step
	public void deleteSteps(Date time_stamp)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_STEPS, KEY_TIME_STAMP + " = ?",
		new String[] { Long.toString(time_stamp.getTime()/1000) });
		db.close();
	}

	public void addProfile(Profile profile)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, profile.getName());
		values.put(KEY_AGE, profile.getAge());
		values.put(KEY_SEX, profile.getSex());
		values.put(KEY_WEIGHT, profile.getWeight());
		values.put(KEY_HEIGHT, profile.getHeight());

		// Inserting row
		db.insert(TABLE_PROFILE, null, values);
		db.close(); // Close database connection
	}

	public Profile getProfile()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_PROFILE;

		Cursor cursor = db.rawQuery(selectQuery, null);
        Profile profile = new Profile();

		if(cursor.moveToFirst())
        {
            profile = new Profile(
            cursor.getString(1),
            Integer.parseInt(cursor.getString(2)),
            Boolean.parseBoolean(cursor.getString(3)),
            Integer.parseInt(cursor.getString(4)),
            Integer.parseInt(cursor.getString(5))
            );
        }

        cursor.close();
		return profile;
	}

	// Updating profile
	public void updateProfile(Profile new_profile)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, new_profile.getName());
		values.put(KEY_AGE, new_profile.getAge());
		values.put(KEY_SEX, new_profile.getSex());
		values.put(KEY_WEIGHT, new_profile.getWeight());
		values.put(KEY_HEIGHT, new_profile.getHeight());

		// updating row
		db.update(TABLE_PROFILE, values, KEY_ID + " = ?",
				new String[]{String.valueOf(1)});
	}

	// Adding run data
	public void addRunData(RunData runData)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TIME_STAMP, runData.getTimeStamp());
		values.put(KEY_SPEED, runData.getSpeed());
		values.put(KEY_LATITUDE, runData.getLatitude());
		values.put(KEY_LONGITUDE, runData.getLongitude());
		values.put(KEY_TIME_ELAPSED, runData.getTimeElapsed());

		// Inserting row
		db.insert(TABLE_RUNDATA, null, values);
		db.close();
	}

	// Get runData for specific day
	public List<RunData> getRunData(Date time_stamp)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<RunData> runDataList = new ArrayList<RunData>();

		String selectQuery = "SELECT * FROM " + TABLE_RUNDATA + " WHERE "
			+ KEY_TIME_STAMP + " = " + Long.toString(time_stamp.getTime()/1000);

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do 
			{
				RunData runData = new RunData();
				runData.setTimeStamp(new Date(Long.parseLong(cursor.getString(0))*1000));
				runData.setSpeed(Double.parseDouble(cursor.getString(1)));
				runData.setLatitude(Double.parseDouble(cursor.getString(2)));
				runData.setLongitude(Double.parseDouble(cursor.getString(3)));
				runData.setTimeElapsed(Long.parseLong(cursor.getString(4)));
				
				// Add runData to list
				runDataList.add(runData);
			} while (cursor.moveToNext());
		}

        cursor.close();
		// return runData list
		return runDataList;
	}

	public List<RunData> getAllRunData()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<RunData> runDataList = new ArrayList<RunData>();

		String selectQuery = "SELECT * FROM " + TABLE_RUNDATA;

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				RunData runData = new RunData();
				runData.setTimeStamp(new Date(Long.parseLong(cursor.getString(0))*1000));
				runData.setSpeed(Double.parseDouble(cursor.getString(1)));
				runData.setLatitude(Double.parseDouble(cursor.getString(2)));
				runData.setLongitude(Double.parseDouble(cursor.getString(3)));
				runData.setTimeElapsed(Long.parseLong(cursor.getString(4)));

				// Add runData to list
				runDataList.add(runData);
			} while (cursor.moveToNext());
		}

		cursor.close();
		// return runData list
		return runDataList;
	}

	// Add goal
	public void addGoal(Goal goal)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_GOAL_TYPE, goal.getGoalType());
		values.put(KEY_TIME_TYPE, goal.getTimeType());
		values.put(KEY_VALUE, goal.getValue());
		values.put(KEY_END_DATE, goal.getEndDate());

		db.insert(TABLE_GOALS, null, values);
		db.close();
	}

	public List<Goal> getAllGoals()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<Goal> goalList = new ArrayList<Goal>();

		String selectQuery = "SELECT * FROM " + TABLE_GOALS;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do 
			{
				Goal goal = new Goal();
				goal.setGoalType(Integer.parseInt(cursor.getString(0)));
				goal.setTimeType(Integer.parseInt(cursor.getString(1)));
				goal.setValue(Integer.parseInt(cursor.getString(2)));
				goal.setEndDate(new Date(Long.parseLong(cursor.getString(3))*1000));
				
				// Add goal to list
				goalList.add(goal);
			} while (cursor.moveToNext());
		}

        cursor.close();
		// return goal list
		return goalList;
	}

	public void addTrophy(Trophy trophy)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, trophy.getType());
		values.put(KEY_VALUE, trophy.getValue());
		values.put(KEY_DATE_ACHIEVED, trophy.getDateAchieved());

		db.insert(TABLE_TROPHIES, null, values);
		db.close();
	}

	public List<Trophy> getAllTrophies()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		List<Trophy> trophyList =  new ArrayList<Trophy>();

		String selectQuery = "SELECT * FROM " + TABLE_TROPHIES;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do 
			{
				Trophy trophy = new Trophy();
				trophy.setType(Integer.parseInt(cursor.getString(0)));
				trophy.setValue(Integer.parseInt(cursor.getString(1)));
				trophy.setDateAchieved(new Date(Long.parseLong(cursor.getString(2))*1000));
				
				// Add trophy to list
				trophyList.add(trophy);
			} while (cursor.moveToNext());
		}

        cursor.close();
		// return trophy list
		return trophyList;
	}
}