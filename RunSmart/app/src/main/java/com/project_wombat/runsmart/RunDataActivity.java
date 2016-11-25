package com.project_wombat.runsmart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import java.util.Date;

public class RunDataActivity extends AppCompatActivity {
    private Bundle extras;
    private DBHandler dbHandler;
    private Date runDate;
    private TextView date;
    private TextView duration;
    private TextView distance;
    private TextView speed;
    private Chronometer timer;
    private TextView calories;
    private Button mapButton;
    private ShareButton shareButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_run_data);
        dbHandler = new DBHandler(this);

        mapButton = (Button) findViewById(R.id.mapButton);
        extras = getIntent().getExtras();

        if(extras.getBoolean("RUN_ACTIVITY", false))
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if(!dbHandler.getProfile().getUseGoogleMaps())
                mapButton.setVisibility(View.GONE);
        }
        else
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mapButton.setVisibility(View.GONE);
        }

        runDate = new Date();

        runDate.setTime(extras.getLong("TIME_STAMP", -1));

        date = (TextView) findViewById(R.id.date);
        duration = (TextView) findViewById(R.id.duration);
        distance = (TextView) findViewById(R.id.distance);
        speed = (TextView) findViewById(R.id.speed);
        timer = (Chronometer) findViewById(R.id.durationClock);
        calories = (TextView) findViewById(R.id.calories);

        shareButton = (ShareButton) findViewById(R.id.shareButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(extras.getBoolean("RUN_ACTIVITY", false))
            getMenuInflater().inflate(R.menu.runmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.done)
        {
            finish();
        }
        else if (id == android.R.id.home)
        {
            extras = getIntent().getExtras();

            if(extras.getBoolean("RUN_ACTIVITY", false))
            {
                return true;
            }
            else
            {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        Run run = dbHandler.getRun(runDate);
        String str;

        date.setText(DateUtils.display(run.getTimeStamp(), true));
        str = Long.toString(run.getDuration()/60000) + " mins";
        duration.setText(str);
        str = String.format("%.2f",run.getDistance()/1000) + " km";
        distance.setText(str);
        str = String.format("%.2f",run.getAvgSpeed()) + " km/h";
        speed.setText(str);
        timer.stop();
        timer.setBase(timer.getBase()-run.getDuration());
        str = Double.toString(((3.5*dbHandler.getProfile().getWeight()*8)/200)*(run.getDuration()/60000)) + " cal";
        calories.setText(str);

        if(dbHandler.getProfile().getUseFacebook()) {
            View rootView = getWindow().getDecorView().getRootView();
            Bitmap b = getScreenShot(rootView);

            //screenShot(this);
            SharePhoto photo = new SharePhoto.Builder().setBitmap(b).build();
            SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
            shareButton.setShareContent(content);
        }
        else
            shareButton.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onBackPressed()
    {
        extras = getIntent().getExtras();

        if(extras.getBoolean("RUN_ACTIVITY", false))
        {
            //do nothing
        }
        else
        {
            finish();
        }
    }

    public void viewMap(View view)
    {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("TIME_STAMP", StaticData.getInstance().getRunTime());
        startActivity(intent);
    }

    public static Bitmap getScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false); // clear drawing cache
        return bitmap;
    }
}
