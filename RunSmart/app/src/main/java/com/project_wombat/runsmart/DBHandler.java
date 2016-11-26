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
	private static final String KEY_FACEBOOK = "facebook";
	private static final String KEY_GOAL_TYPE = "goal_type";
	private static final String KEY_GOOGLE_MAPS = "google_maps";
	private static final String KEY_HEART_RATE_AFTER = "heart_rate_after";
	private static final String KEY_HEART_RATE_BEFORE = "heart_rate_before";
	private static final String KEY_HEIGHT = "height";
	private static final String KEY_ID = "id";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_NAME = "name";
	private static final String KEY_SEX = "sex";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_START_DATE = "start_date";
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
		+ KEY_HEIGHT + " INTEGER," + KEY_FACEBOOK + " TEXT,"
		+ KEY_GOOGLE_MAPS + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);

		// Create runData table
		CREATE_TABLE = "CREATE TABLE " + TABLE_RUNDATA + "("
		+ KEY_TIME_STAMP + " INTEGER," + KEY_SPEED
		+ " REAL," + KEY_LATITUDE + " REAL," + KEY_LONGITUDE
		+ " REAL," + KEY_TIME_ELAPSED + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);

		// Create goals table
		CREATE_TABLE = "CREATE TABLE " + TABLE_GOALS + "("
		+ KEY_GOAL_TYPE + " INTEGER," + KEY_TIME_TYPE
		+ " INTEGER," + KEY_VALUE + " INTEGER," + KEY_START_DATE + " INTEGER," + KEY_END_DATE
		+ " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);

		// Create trophies table
		CREATE_TABLE = "CREATE TABLE " + TABLE_TROPHIES + "("
		+ KEY_TYPE + " INTEGER PRIMARY KEY," + KEY_VALUE
		+ " INTEGER," + KEY_DATE_ACHIEVED + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);

		//db.close(); // Close database connection
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
		Run run = null;

		// looping through all rows and adding to list
		if(cursor.getCount() != 0) {
			cursor.moveToFirst();

			run = new Run(
					new Date(Long.parseLong(cursor.getString(0)) * 1000),
					cursor.getDouble(1),
					cursor.getLong(2),
					cursor.getDouble(3),
					cursor.getDouble(4),
					cursor.getDouble(5),
					cursor.getDouble(6)
			);
		}

        cursor.close();
		db.close(); // Close database connection
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
		db.close(); // Close database connection
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
				run.setDistance(cursor.getDouble(1));
				run.setDuration(cursor.getLong(2));
				run.setAvgSpeed(cursor.getDouble(3));
				run.setTopSpeed(cursor.getDouble(4));
				run.setHeartRateBefore(cursor.getDouble(5));;
				run.setHeartRateAfter(cursor.getDouble(6));;
				
				// Add run to list
				runList.add(run);
			} while (cursor.moveToNext());
		}

        cursor.close();
		db.close(); // Close database connection
		// return run list
		return runList;
	}


	public long getRunTimeBounded(long start, long end)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		long count = 0;

		String selectQuery = "SELECT SUM(" + KEY_DURATION + ") FROM " + TABLE_RUNS + " WHERE " + KEY_TIME_STAMP + " BETWEEN " + start + " AND " + end;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// set count if there is run data
		if (cursor != null)
		{
			cursor.moveToFirst();
			count = cursor.getLong(0);
			//count = Long.parseLong(cursor.getString(0));
		}

		cursor.close();
		db.close(); // Close database connection
		// return time count
		return count;
	}


	public double getRunDistanceBounded(long start, long end)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		double count = 0;

		String selectQuery = "SELECT SUM(" + KEY_DISTANCE + ") FROM " + TABLE_RUNS + " WHERE " + KEY_TIME_STAMP + " BETWEEN " + start + " AND " + end;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// set count if there is run data
		if (cursor != null)
		{
			cursor.moveToFirst();
			count = cursor.getDouble(0);
			//count = Double.parseDouble(cursor.getString(0));
		}

		cursor.close();
		db.close(); // Close database connection
		// return distance count
		return count;
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

		//Check if exists
		Cursor cursor = db.query(TABLE_STEPS, new String[]{KEY_TIME_STAMP, KEY_STEPS},
				KEY_TIME_STAMP + " = " + Long.toString(step.getTimeStamp()), null, null, null, null);

		if(cursor.moveToFirst())
		{
			step.setSteps(step.getSteps()+Integer.parseInt(cursor.getString(1)));
			values = new ContentValues();
			values.put(KEY_TIME_STAMP, step.getTimeStamp());
			values.put(KEY_STEPS, step.getSteps());
			db.update(TABLE_STEPS, values, KEY_TIME_STAMP + " = ?", new String[]{String.valueOf(step.getTimeStamp())});
		}
		else
		{
			db.insert(TABLE_STEPS, null, values);
		}

		db.close(); // Close database connection
	}

	// Getting one steps
	public Steps getSteps(Date time_stamp)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STEPS, new String[]{KEY_TIME_STAMP,
		KEY_STEPS}, KEY_TIME_STAMP + " = " + Long.toString(time_stamp.getTime()/1000), null, null, null, null);

        Steps step = new Steps();

		if(cursor.moveToFirst())
        {
            step = new Steps(
            new Date(Long.parseLong(cursor.getString(0))*1000),
            Integer.parseInt(cursor.getString(1))
            );
        }

        cursor.close();
		db.close(); // Close database connection
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
		db.close(); // Close database connection
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
				step.setSteps(cursor.getInt(1));
				
				// Add steps to list
				stepsList.add(step);
			} while (cursor.moveToNext());
		}

        cursor.close();
		db.close(); // Close database connection
		// return steps list
		return stepsList;
	}


	public int getStepCountBounded(long start, long end)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		int count = 0;

		String selectQuery = "SELECT SUM(" + KEY_STEPS + ") FROM " + TABLE_STEPS + " WHERE " + KEY_TIME_STAMP + " BETWEEN " + start + " AND " + end;
		Cursor cursor = db.rawQuery(selectQuery, null);

		//return count if there are steps in this range
		if(cursor != null)
		{
			cursor.moveToFirst();
			count = cursor.getInt(0);
			//count = Integer.parseInt(cursor.getString(0));
		}

		cursor.close();
		db.close(); // Close database connection
		// return step count
		return count;
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
		values.put(KEY_FACEBOOK, profile.getUseFacebook());
		values.put(KEY_GOOGLE_MAPS, profile.getUseGoogleMaps());

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
            cursor.getString(3).matches("1"),
            Integer.parseInt(cursor.getString(4)),
            Integer.parseInt(cursor.getString(5)),
			cursor.getString(6).matches("1"),
			cursor.getString(7).matches("1")
            );
        }

        cursor.close();
		db.close(); // Close database connection
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
		values.put(KEY_FACEBOOK, new_profile.getUseFacebook());
		values.put(KEY_GOOGLE_MAPS, new_profile.getUseGoogleMaps());

		// updating row
		db.update(TABLE_PROFILE, values, KEY_ID + " = ?",
				new String[]{String.valueOf(1)});

		db.close(); // Close database connection
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
				runData.setSpeed(cursor.getDouble(1));
				runData.setLatitude(cursor.getDouble(2));;
				runData.setLongitude(cursor.getDouble(3));;
				runData.setTimeElapsed(cursor.getLong(4));;
				
				// Add runData to list
				runDataList.add(runData);
			} while (cursor.moveToNext());
		}

        cursor.close();
		db.close(); // Close database connection
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
				runData.setSpeed(cursor.getDouble(1));
				runData.setLatitude(cursor.getDouble(2));
				runData.setLongitude(cursor.getDouble(3));
				runData.setTimeElapsed(cursor.getLong(4));

				// Add runData to list
				runDataList.add(runData);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close(); // Close database connection
		// return runData list
		return runDataList;
	}

	// Deleting a goal
	public void deleteGoal(Goal goal)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GOALS, KEY_GOAL_TYPE + " =? AND " + KEY_TIME_TYPE + " =? AND " + KEY_VALUE + " =? AND " + KEY_END_DATE + " =? ",
				new String [] { Integer.toString(goal.getGoalType()), Integer.toString(goal.getTimeType()), Integer.toString(goal.getValue()), Long.toString(goal.getEndDate()) });
		db.close();
	}

	// Add goal
	public void addGoal(Goal goal)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_GOAL_TYPE, goal.getGoalType());
		values.put(KEY_TIME_TYPE, goal.getTimeType());
		values.put(KEY_VALUE, goal.getValue());
		values.put(KEY_START_DATE, goal.getStart_date());
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
				goal.setGoalType(cursor.getInt(0));
				goal.setTimeType(cursor.getInt(1));
				goal.setValue(cursor.getInt(2));
				goal.setStart_date(new Date(Long.parseLong(cursor.getString(3))*1000));
				goal.setEndDate(new Date(Long.parseLong(cursor.getString(4))*1000));
				
				// Add goal to list
				goalList.add(goal);
			} while (cursor.moveToNext());
		}

        cursor.close();
		db.close(); // Close database connection
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
		db.close(); // Close database connection
		// return trophy list
		return trophyList;
	}

	public Run getMaxDistanceRun()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_RUNS + " ORDER BY " + KEY_DISTANCE + " DESC LIMIT 1";
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor != null)
			cursor.moveToFirst();

		Run run = new Run(
				new Date(Long.parseLong(cursor.getString(0))*1000),
				cursor.getDouble(1),
				cursor.getLong(2),
				cursor.getDouble(3),
				cursor.getDouble(4),
				cursor.getDouble(5),
				cursor.getDouble(6)
		);

		cursor.close();
		db.close();
		return run;
	}

	public Run getMaxDurationRun()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_RUNS + " ORDER BY " + KEY_DURATION + " DESC LIMIT 1";
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor != null)
			cursor.moveToFirst();

		Run run = new Run(
				new Date(Long.parseLong(cursor.getString(0))*1000),
				cursor.getDouble(1),
				cursor.getLong(2),
				cursor.getDouble(3),
				cursor.getDouble(4),
				cursor.getDouble(5),
				cursor.getDouble(6)
		);

		cursor.close();
		db.close();
		return run;
	}

	public Run getMaxSpeedRun()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_RUNS + " ORDER BY " + KEY_AVERAGE_SPEED + " DESC LIMIT 1";
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor != null)
			cursor.moveToFirst();

		Run run = new Run(
				new Date(Long.parseLong(cursor.getString(0))*1000),
				cursor.getDouble(1),
				cursor.getLong(2),
				cursor.getDouble(3),
				cursor.getDouble(4),
				cursor.getDouble(5),
				cursor.getDouble(6)
		);

		cursor.close();
		db.close();
		return run;
	}

	public Steps getMaxSteps()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_STEPS + " ORDER BY " + KEY_STEPS + " DESC LIMIT 1";
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor != null)
			cursor.moveToFirst();

		Steps steps = new Steps(
				new Date(Long.parseLong(cursor.getString(0))*1000),
				cursor.getInt(1)
		);

		cursor.close();
		db.close();
		return steps;
	}

	public Double getTotalDistance()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT SUM(" + KEY_DISTANCE +") FROM " + TABLE_RUNS;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor != null)
			cursor.moveToFirst();

		Double totalDistance = cursor.getDouble(0);

		cursor.close();
		db.close();
		return totalDistance;
	}

	public int getTotalSteps()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT SUM(" + KEY_STEPS +") FROM " + TABLE_STEPS;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor != null)
			cursor.moveToFirst();

		int totalSteps = cursor.getInt(0);

		cursor.close();
		db.close();
		return totalSteps;
	}
}