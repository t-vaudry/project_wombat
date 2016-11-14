package com.project_wombat.runsmart;

import android.Manifest;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.Date;
/**
 * Created by anita on 2016-10-27.
 */

public class GPSTracker extends IntentService implements LocationListener {

    private final Context mContext = this;
    DBHandler dbHandler = new DBHandler(mContext);

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    public int MY_PERMISSION = 1;
    boolean canGetLocation = false;

    Date beginningOfDataCollection;
    Date now;
    Date prev;

    double prev_latitude;
    double prev_longitude;

    Location location; // location
    double curr_latitude; // latitude
    double curr_longitude; // longitude

    double curr_distance = 0;
    double curr_speed = 0;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2; // 2 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 seconds

    // Values to pass distance and speed
    public static final String ACTION_UPDATE = "com.project_wombat.runsmart.UPDATE";
    public static final String EXTRA_KEY_DISTANCE = "distance";
    public static final String EXTRA_KEY_SPEED = "speed";

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker() {
        super("Service");
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            curr_latitude = location.getLatitude();
                            curr_longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                curr_latitude = location.getLatitude();
                                curr_longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            curr_latitude = location.getLatitude();
        }

        // return latitude
        return curr_latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            curr_longitude = location.getLongitude();
        }

        // return longitude
        return curr_longitude;
    }

    /**
     * Function to check if best network provider
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
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

    public double getSpeed()
    {
        return curr_speed;
    }

    public double getDistance()
    {
        return curr_distance;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        double distanceDifferential = 0;

        curr_distance = 0;
        beginningOfDataCollection = new Date();
        now = new Date();

        RunData rd;
        while(StaticData.getInstance().getCollectData())
        {
            getLocation();

            //Set now to current time
            now = new Date();

            curr_latitude = getLatitude();
            curr_longitude = getLongitude();

            if (prev_longitude == 0.0 || prev_latitude == 0.0)
            {
                distanceDifferential = 0.0;
                curr_speed = 0.0;
            }
            else
            {
                distanceDifferential = getDistance(curr_latitude, curr_longitude, prev_latitude, prev_longitude);
                curr_speed = distanceDifferential/((now.getTime()-prev.getTime())/1000);
            }
            curr_distance += distanceDifferential;

            Intent intentUpdate = new Intent();
            intentUpdate.setAction(ACTION_UPDATE);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
            intentUpdate.putExtra(EXTRA_KEY_DISTANCE, Double.toString(curr_distance));
            intentUpdate.putExtra(EXTRA_KEY_SPEED, Double.toString(curr_speed));
            sendBroadcast(intentUpdate);

            rd = new RunData(beginningOfDataCollection, curr_speed, curr_latitude, curr_longitude, now.getTime()-beginningOfDataCollection.getTime());
            dbHandler.addRunData(rd);

            prev_latitude = curr_latitude;
            prev_longitude = curr_longitude;
            prev = now;
            try {
                synchronized (this){
                    wait(1000);
                }
            }
            catch(InterruptedException ex){}
        }

        //Log entire run to database
        Run run = new Run(beginningOfDataCollection, curr_distance, now.getTime()-beginningOfDataCollection.getTime(), (curr_distance/1000.0)/((now.getTime()-beginningOfDataCollection.getTime())/3600000.0), 0.0, 0.0, 0.0);
        dbHandler.addRun(run);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
