package com.project_wombat.runsmart;

/**
 * Created by anita on 2016-11-12.
 */

public class GoalModel{

    private int icon;
    private String goal;
    private String progress;

    public GoalModel(int icon, String goal, String progress) {
        super();
        this.icon = icon;
        this.goal = goal;
        this.progress = progress;
    }

    public String getGoal()
    {
        return this.goal;
    }

    public  String getProgress()
    {
        return this.progress;
    }

    public int getIcon(){return this.icon;}

    public void setProgress(String prog)
    {
        this.progress = prog;
    }
}