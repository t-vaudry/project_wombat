package com.project_wombat.runsmart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Date;

public class RunDataActivity extends AppCompatActivity {
    private Bundle extras;
    private DBHandler dbHandler;
    private Date runDate;
    private TextView date;
    private TextView duration;
    private TextView distance;
    private TextView speed;
    private Chronometer timer;
    private TextView calories;
    private TextView before_hr;
    private TextView after_hr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_data);

        extras = getIntent().getExtras();

        if(extras.getBoolean("RUN_ACTIVITY", false))
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHandler = new DBHandler(this);
        runDate = new Date();

        runDate.setTime(extras.getLong("TIME_STAMP", -1));

        date = (TextView) findViewById(R.id.date);
        duration = (TextView) findViewById(R.id.duration);
        distance = (TextView) findViewById(R.id.distance);
        speed = (TextView) findViewById(R.id.speed);
        timer = (Chronometer) findViewById(R.id.durationClock);
        calories = (TextView) findViewById(R.id.calories);
        before_hr = (TextView) findViewById(R.id.before_heart_rate);
        after_hr = (TextView) findViewById(R.id.after_heart_rate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(extras.getBoolean("RUN_ACTIVITY", false))
            getMenuInflater().inflate(R.menu.runmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.done)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        Run run = dbHandler.getRun(runDate);
        String str;

        date.setText(DateUtils.display(run.getTimeStamp(), true));
        str = Long.toString(run.getDuration()/60000) + " mins";
        duration.setText(str);
        str = String.format("%.2f",run.getDistance()/1000) + " km";
        distance.setText(str);
        str = String.format("%.2f",run.getAvgSpeed()) + " km/h";
        speed.setText(str);
        timer.stop();
        timer.setBase(timer.getBase()-run.getDuration());
        str = Double.toString(((3.5*dbHandler.getProfile().getWeight()*8)/200)*(run.getDuration()/60000)) + " cal";
        calories.setText(str);
        str = Double.toString(run.getHeartRateBefore()) + " bpm";
        before_hr.setText(str);
        str = Double.toString(run.getHeartRateAfter()) + " bpm";
        after_hr.setText(str);
    }

    @Override
    public void onBackPressed()
    {
        // do nothing
    }
}
