package com.project_wombat.runsmart;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by anita on 2016-10-28.
 */

public class StaticData {
    private static StaticData mInstance = null;
    private boolean collectData;
    private boolean countSteps;
    private boolean goalChanged;
    private Lock goalLock = new ReentrantLock();

    private StaticData()
    {
        collectData = false;
        countSteps = false;
    }

    public static StaticData getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new StaticData();
        }
        return mInstance;
    }

    public boolean getCollectData()
    {
        return this.collectData;
    }
    public boolean getCountSteps() {return this.countSteps; }
    public boolean getGoalChanged()
    {
        goalLock.lock();
        boolean change = goalChanged;
        goalLock.unlock();
        return change;
    }
    public void setCollectData(boolean val)
    {
        collectData = val;
    }
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
