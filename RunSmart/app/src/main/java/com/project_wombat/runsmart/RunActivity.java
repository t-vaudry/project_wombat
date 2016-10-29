package com.project_wombat.runsmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import static com.project_wombat.runsmart.R.id.distanceView;
import static com.project_wombat.runsmart.R.id.speedView;

public class RunActivity extends AppCompatActivity {

    GPSTracker gps;
    Chronometer chronometer;
    DBHandler dbHandler;
    MyBroadcastReceiver myBroadcastReceiver;

    //Text views
    TextView distanceView;
    TextView speedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        gps = new GPSTracker();
        chronometer = (Chronometer)findViewById(R.id.chronometer);
        dbHandler = new DBHandler(this);
        myBroadcastReceiver = new MyBroadcastReceiver();

        distanceView = (TextView) findViewById(R.id.distanceView);
        speedView = (TextView) findViewById(R.id.speedView);

        //register receiver
        IntentFilter intentFilter = new IntentFilter(GPSTracker.ACTION_UPDATE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
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

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String distance = intent.getStringExtra(GPSTracker.EXTRA_KEY_DISTANCE);
            String speed = intent.getStringExtra(GPSTracker.EXTRA_KEY_SPEED);
            distanceView.setText(distance);
            speedView.setText(speed);
        }
    }

    public void startRun(View view)
    {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        Intent gpsIntent = new Intent(this, GPSTracker.class);
        StaticData.getInstance().setCollectData(true);
        startService(gpsIntent);
    }

    public void stopRun(View view)
    {
        StaticData.getInstance().setCollectData(false);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

}
