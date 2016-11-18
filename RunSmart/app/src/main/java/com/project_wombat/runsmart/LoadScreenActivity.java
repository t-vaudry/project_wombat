package com.project_wombat.runsmart;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LoadScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            //permissions not set

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Location permissions not set for RunSmart. Please allow to continue.");
            dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    final Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                    startActivity(i);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    final Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);

                }
            });
            dialog.show();
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            try{
                Thread.sleep(500);
            }
            catch(InterruptedException e) {}
            startActivity(intent);
            finish();
        }
    }
}
