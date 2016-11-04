package com.project_wombat.runsmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e) {}
        startActivity(intent);
        finish();
    }
}
