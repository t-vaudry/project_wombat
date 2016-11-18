package com.project_wombat.runsmart;

/**
 * Created by anita on 2016-10-28.
 */

public class StaticData {
    private static StaticData mInstance = null;
    private boolean collectData;
    private boolean countSteps;

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
    public void setCollectData(boolean val)
    {
        collectData = val;
    }
    public void setCountSteps(boolean val)
    {
        countSteps = val;
    }
}
