package com.project_wombat.runsmart;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunActivity extends AppCompatActivity {

    GPSTracker gps;
    Chronometer chronometer;
    DBHandler dbHandler;

    //Text views
    TextView distanceView;
    TextView speedView;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run(){
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            speedView.setText(df.format(new Date()));
            distanceView.setText(Double.toString(gps.getDistance()));
        }
    };

    public class distance
    {
        private double mDistance;
        public void setDistance(double d)
        {
            mDistance = d;
            distanceView.setText(Double.toString(mDistance));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        gps = new GPSTracker();
        chronometer = (Chronometer)findViewById(R.id.chronometer);
        dbHandler = new DBHandler(this);

        distanceView = (TextView) findViewById(R.id.distanceView);
        speedView = (TextView) findViewById(R.id.speedView);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void startRun(View view)
    {
        chronometer.start();
        Intent gpsIntent = new Intent(this, GPSTracker.class);
        StaticData.getInstance().setCollectData(true);
        startService(gpsIntent);
        runOnUiThread(mRunnable);
        try {
            wait(5000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void stopRun(View view)
    {
        StaticData.getInstance().setCollectData(false);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        distanceView.setText(Double.toString(dbHandler.getAllRunData().get(2).getLatitude()));
        speedView.setText(Integer.toString(dbHandler.getAllRunData().size()));
    }

}
