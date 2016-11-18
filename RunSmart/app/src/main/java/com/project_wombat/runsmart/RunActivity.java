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
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class RunActivity extends AppCompatActivity {

    GPSTracker gps;
    Chronometer chronometer;
    DBHandler dbHandler;
    MyBroadcastReceiver myBroadcastReceiver;
    Toast back_toast;

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

        distanceView = (TextView) findViewById(R.id.distanceView);

        //register receiver
        IntentFilter intentFilter = new IntentFilter(GPSTracker.ACTION_UPDATE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        startRun();
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
        back_toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            back_toast.show();
            return true;
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
        Intent gpsIntent = new Intent(this, GPSTracker.class);
        StaticData.getInstance().setCollectData(true);
        startService(gpsIntent);
    }

    public void stopRun(View view)
    {
        StaticData.getInstance().setCollectData(false);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());

        finish();
    }

}
