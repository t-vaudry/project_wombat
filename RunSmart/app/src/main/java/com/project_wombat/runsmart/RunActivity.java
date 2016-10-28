package com.project_wombat.runsmart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RunActivity extends AppCompatActivity {

    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        gps = new GPSTracker(this);
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2)
    {
        float R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = (R * c)*1000; // Distance in m
        return d;
    }

    public double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}
