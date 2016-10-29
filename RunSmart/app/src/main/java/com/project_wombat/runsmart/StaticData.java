package com.project_wombat.runsmart;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by anita on 2016-10-28.
 */

public class StaticData {
    private static StaticData mInstance = null;
    private boolean collectData;

    private StaticData()
    {
        collectData = false;
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
    public void setCollectData(boolean val)
    {
        collectData = val;
    }
}
