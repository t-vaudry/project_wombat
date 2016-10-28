package com.project_wombat.runsmart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button locationButton;
    DBHandler dbHandler;
    TextView runText;
    TextView runText2;
    GPSTracker gps;

    private final String TAG = "Main Activity";
    private final int MY_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.view_profile);
        locationButton = (Button) findViewById(R.id.locationText);
        dbHandler = new DBHandler(this);
        runText = (TextView) findViewById(R.id.runText);
        runText2 = (TextView) findViewById(R.id.runText2);

        /*if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    gps.MY_PERMISSION );
        }*/

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSION);
        }

        gps = new GPSTracker(this);
        if (gps.canGetLocation())
        {
            Log.d(TAG, "Can get location.");
        }
        else
        {
            Log.d(TAG, "Can NOT get location.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (dbHandler.getProfile().getName() == null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else {
            button.setText(dbHandler.getProfile().getName());
        }
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void updateLocation(View view)
    {
        gps.getLocation();
        double lat = gps.getLatitude();
        double lon = gps.getLongitude();
        locationButton.setText("Lat: " + Double.toString(lat) + " Lon: " + Double.toString(lon));
    }
}
