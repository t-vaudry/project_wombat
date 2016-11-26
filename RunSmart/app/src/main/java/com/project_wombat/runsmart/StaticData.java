package com.project_wombat.runsmart;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by anita on 2016-10-28.
 */

public class StaticData {
    private static StaticData mInstance = null;
    private long runTime;
    private boolean collectData;
    private boolean pauseData;
    private long pauseTime;
    private boolean countSteps;
    private boolean goalChanged;
    private Lock goalLock = new ReentrantLock();
    private int stepCount;
    private long currentRunTime;
    private double runDistance;

    private StaticData()
    {
        runTime = 0;
        collectData = false;
        pauseData = false;
        countSteps = false;
        pauseTime = 0;
        stepCount = 0;
        currentRunTime = 0;
    }

    public static StaticData getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new StaticData();
        }
        return mInstance;
    }

    public long getRunTime() { return this.runTime; }
    public boolean getCollectData()
    {
        return this.collectData;
    }
    public boolean getPauseData() { return this.pauseData; }
    public long getPauseTime() { return this.pauseTime; }
    public boolean getCountSteps() {return this.countSteps; }
    public boolean getGoalChanged()
    {
        goalLock.lock();
        boolean change = goalChanged;
        goalLock.unlock();
        return change;
    }
    public int getStepCount() { return this.stepCount; }
    public long getCurrentRunTime() { return this.currentRunTime; }
    public double getRunDistance() { return this.runDistance; }
    public void setRunTime(long val) { runTime = val; }
    public void setCollectData(boolean val)
    {
        collectData = val;
    }
    public void setPauseData(boolean val) { pauseData = val; }
    public void setPauseTime(long val) { pauseTime = val; }
    public void setCountSteps(boolean val)
    {
        countSteps = val;
    }
    public void setGoalChanged(boolean val)
    {
        goalLock.lock();
        goalChanged = val;
        goalLock.unlock();
    }
    public void setStepCount(int val) { stepCount = val; }
    public void setCurrentRunTime(long val) { currentRunTime = val; }
    public void setRunDistance(double val) { runDistance = val; }
    public boolean getAndSetGoalChanged()
    {
        goalLock.lock();
        boolean change = goalChanged;
        if (change)
            goalChanged = false;
        goalLock.unlock();
        return change;
    }
}
