package com.project_wombat.runsmart;

/**
 * Created by anita on 2016-10-31.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Date;

public class StepCounter extends IntentService implements SensorEventListener {
    private int count = 0;
    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;

    private DBHandler dbHandler;

    public StepCounter() {
        super("StepCounter");
    }

    // Values to pass distance and speed
    public static final String ACTION_UPDATE = "com.project_wombat.runsmart.UPDATE";
    public static final String EXTRA_KEY_STEPS = "steps";

    @Override
    public void onCreate()
    {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        dbHandler = new DBHandler(this);
    }

    @Override
    public void onDestroy()
    {
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mSensorManager.registerListener(this, mStepDetectorSensor,SensorManager.SENSOR_DELAY_FASTEST);

        Intent intentUpdate = new Intent();
        intentUpdate.setAction(ACTION_UPDATE);
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);

        while(StaticData.getInstance().getCountSteps()) {
            intentUpdate.putExtra(EXTRA_KEY_STEPS, Integer.toString(count));
            sendBroadcast(intentUpdate);
        }
        Steps steps = new Steps(new Date(), count);
        dbHandler.addSteps(steps);
        intentUpdate.putExtra(EXTRA_KEY_STEPS, "00000");
        sendBroadcast(intentUpdate);
    }

    @Override
    public void onAccuracyChanged(Sensor mStepCounterSensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            count++;
        }
    }

}