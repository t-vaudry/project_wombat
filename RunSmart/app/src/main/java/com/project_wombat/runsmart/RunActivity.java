package com.project_wombat.runsmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;

public class RunActivity extends AppCompatActivity {

    GPSTracker gps;
    Chronometer chronometer;
    DBHandler dbHandler;
    MyBroadcastReceiver myBroadcastReceiver;
    Toast back_toast;
    Button pauseButton;
    Button resumeButton;
    long timeWhenPaused;

    //Text views
    TextView distanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        //Create toast
        back_toast = Toast.makeText(this, R.string.back_run, Toast.LENGTH_SHORT);

        //Initialize GPS tracker
        gps = new GPSTracker();
        chronometer = (Chronometer)findViewById(R.id.chronometer);
        dbHandler = new DBHandler(this);
        myBroadcastReceiver = new MyBroadcastReceiver();

        pauseButton = (Button) findViewById(R.id.buttonPause);
        resumeButton = (Button) findViewById(R.id.buttonResume);
        timeWhenPaused = 0;

        distanceView = (TextView) findViewById(R.id.distanceView);

        //register receiver
        IntentFilter intentFilter = new IntentFilter(GPSTracker.ACTION_UPDATE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        if(!StaticData.getInstance().getCollectData())
            startRun();
        else
            returnToRun();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void onBackPressed()
    {
        if(!StaticData.getInstance().getPauseData())
            back_toast.show();
        else
            finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            if(!StaticData.getInstance().getPauseData()) {
                back_toast.show();
                return true;
            }
            else
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String distance = intent.getStringExtra(GPSTracker.EXTRA_KEY_DISTANCE);

            if (distance != null)
            {
                DecimalFormat numbers = new DecimalFormat("0.000");


                double distanceDouble = Double.parseDouble(distance);

                distanceDouble = distanceDouble/1000f; //to km

                distance = numbers.format(distanceDouble);
                distanceView.setText(distance + "km");
            }
        }
    }

    public void startRun()
    {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        resumeButton.setVisibility(View.GONE);
        Intent gpsIntent = new Intent(this, GPSTracker.class);
        StaticData.getInstance().setCollectData(true);
        StaticData.getInstance().setPauseData(false);
        startService(gpsIntent);
    }

    public void returnToRun()
    {
        timeWhenPaused = StaticData.getInstance().getPauseTime();
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
        resumeButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    public void pauseRun(View view)
    {
        StaticData.getInstance().setPauseData(true);
        timeWhenPaused = chronometer.getBase() - SystemClock.elapsedRealtime();
        StaticData.getInstance().setPauseTime(timeWhenPaused);

        chronometer.stop();
        resumeButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    public void resumeRun(View view)
    {
        StaticData.getInstance().setPauseData(false);
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
        chronometer.start();
        pauseButton.setVisibility(View.VISIBLE);
        resumeButton.setVisibility(View.GONE);
    }

    public void stopRun(View view)
    {
        StaticData.getInstance().setCollectData(false);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenPaused = 0;

        Intent intent = new Intent(this, RunDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("TIME_STAMP", StaticData.getInstance().getRunTime());
        intent.putExtra("RUN_ACTIVITY", true);
        startActivity(intent);
        finish();
    }

}
