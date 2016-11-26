package com.project_wombat.runsmart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class HighScoreActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private TextView total_distance;
    private TextView total_steps;
    private TextView max_duration;
    private TextView max_distance;
    private TextView max_speed;
    private TextView max_steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        dbHandler = new DBHandler(this);

        total_distance = (TextView) findViewById(R.id.distance);
        total_steps = (TextView) findViewById(R.id.allSteps);
        max_duration = (TextView) findViewById(R.id.durationClock);
        max_distance = (TextView) findViewById(R.id.max_distance_value);
        max_speed = (TextView) findViewById(R.id.max_speed_value);
        max_steps = (TextView) findViewById(R.id.max_steps);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("\t\tMESSAGE","5");

        Run run;
        String str;
        int steps = 0;

        total_distance.setText(String.format("%.2f", dbHandler.getTotalDistance()/1000) + " km");
        total_steps.setText(Integer.toString(dbHandler.getTotalSteps()) + " steps" );

        run = dbHandler.getMaxDurationRun();
        str = DateUtils.formatTime(run.getDuration());
        max_duration.setText(str);

        run = dbHandler.getMaxDistanceRun();
        str = String.format("%.2f",run.getDistance()/1000) + " km";
        max_distance.setText(str);

        run = dbHandler.getMaxSpeedRun();
        str = String.format("%.2f",run.getAvgSpeed()) + " km/h";
        max_speed.setText(str);

        steps = dbHandler.getMaxSteps().getSteps();
        max_steps.setText(Integer.toString(steps) + " steps");
    }
}
