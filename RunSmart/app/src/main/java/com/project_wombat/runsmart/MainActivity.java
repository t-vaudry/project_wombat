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

    public void startRun(View view)
    {
        Intent intent = new Intent(this, RunActivity.class);
        startActivity(intent);
    }
}
