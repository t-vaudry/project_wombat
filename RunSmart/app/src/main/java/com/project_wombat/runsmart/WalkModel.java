package com.project_wombat.runsmart;

/**
 * Created by anita on 2016-11-12.
 */

public class WalkModel{

    private String date;
    private String steps;

    public WalkModel(String date, String steps) {
        super();
        this.date = date;
        this.steps = steps;
    }

    public String getDate()
    {
        return this.date;
    }

    public  String getSteps()
    {
        return this.steps;
    }

}