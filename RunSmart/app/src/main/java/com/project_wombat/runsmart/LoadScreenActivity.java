package com.project_wombat.runsmart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadScreenActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private final int MY_PERMISSIONS_REQUEST_MAPS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        int count = 0;
        String[] permissions = new String[2];
        int[] grantResults = new int[2];

        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            //permissions not set
            permissions[count] = android.Manifest.permission.ACCESS_COARSE_LOCATION;
            grantResults[count] = MY_PERMISSIONS_REQUEST_LOCATION;
            count++;
        }

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.MAPS_RECEIVE ) != PackageManager.PERMISSION_GRANTED )
        {
            //permissions not set
            permissions[count] = Manifest.permission.MAPS_RECEIVE;
            grantResults[count] = MY_PERMISSIONS_REQUEST_MAPS;
            count++;
        }

        if(count != 0)
        {
            if(count == 1)
            {
                ActivityCompat.requestPermissions(this,
                        new String[] { permissions[0] },
                        grantResults[0]);
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        grantResults[0]);
            }
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                Intent intent = new Intent(this, MainActivity.class);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                startActivity(intent);
                finish();
            }
            case MY_PERMISSIONS_REQUEST_MAPS: {
                Intent intent = new Intent(this, MainActivity.class);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                startActivity(intent);
                finish();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
