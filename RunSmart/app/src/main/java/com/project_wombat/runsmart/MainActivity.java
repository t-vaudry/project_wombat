package com.project_wombat.runsmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button;
    DBHandler dbHandler;
    TextView runText;
    TextView runText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.view_profile);
        dbHandler = new DBHandler(this);
        runText = (TextView) findViewById(R.id.runText);
        runText2 = (TextView) findViewById(R.id.runText2);

        //SUP BITCHES
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

}
