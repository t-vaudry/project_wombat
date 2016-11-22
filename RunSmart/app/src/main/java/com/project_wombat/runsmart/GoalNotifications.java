package com.project_wombat.runsmart;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Date;
import java.util.List;

/**
 * Created by anita on 2016-11-22.
 */

public class GoalNotifications extends IntentService
{
    public static final String ACTION_UPDATE = "com.project_wombat.runsmart.UPDATE";
    public static final String EXTRA_KEY_DELETED = "deleted";

    private List<Goal> mGoals;
    private int mId = 0;
    private boolean mChanged = false;

    private long today;
    private final Context mContext = this;
    DBHandler dbHandler = new DBHandler(mContext);

    public GoalNotifications(){super(GoalNotifications.class.getName());}

    @Override
    public void onCreate()
    {
        super.onCreate();
        dbHandler = new DBHandler(this);

        //get all currently saved goals
        mGoals = dbHandler.getAllGoals();

    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        while (true)
        {
            mChanged = false;
            //if goals have been changed (added or removed), get again
            if(StaticData.getInstance().getAndSetGoalChanged())
            {
                mGoals.clear();
                mGoals = dbHandler.getAllGoals();
            }

            Date todayDate = new Date();
            today = todayDate.getTime()/1000;

            int count = 0;
            int percentage = 0;
            for (int i = 0; i < mGoals.size(); i++)
            {
                boolean failed = false;
                String notificationTitle = "";
                String notificationText = "";

                //if goal has expired
                if (mGoals.get(i).getEndDate() < today)
                {
                    notificationTitle = getText(R.string.app_name)+ ": Goal Failed";
                    failed = true;
                }
                else
                {
                    notificationTitle = getText(R.string.app_name)+ ": Goal Completed!";
                }
                switch(mGoals.get(i).getGoalType())
                {
                    //Walk
                    case 0:
                        count = dbHandler.getStepCountBounded(mGoals.get(i).getStart_date(), mGoals.get(i).getEndDate());
                        percentage = (int)((float)count/(float)mGoals.get(i).getValue() * 100);
                        notificationText += "Walk ";
                        notificationText += Integer.toString(mGoals.get(i).getValue());
                        notificationText += " steps ";
                        break;
                    //Run time
                    case 1:
                        count = (int)(dbHandler.getRunTimeBounded(mGoals.get(i).getStart_date(), mGoals.get(i).getEndDate()));
                        percentage = (int)((float)count/((float)mGoals.get(i).getValue()*60000) * 100);
                        notificationText += "Run ";
                        notificationText += Integer.toString(mGoals.get(i).getValue());
                        notificationText += " minutes ";
                        break;
                    //Run distance
                    case 2:
                        count = (int)dbHandler.getRunDistanceBounded(mGoals.get(i).getStart_date(), mGoals.get(i).getEndDate());
                        percentage = (int)((float)count/((float)mGoals.get(i).getValue()*1000) * 100);
                        notificationText += "Run ";
                        notificationText += Integer.toString(mGoals.get(i).getValue());
                        notificationText += " kilometres ";
                        break;
                }
                //if percentage >= 100: push notification
                if (percentage >= 100 || failed)
                {
                    mId++;

                    //notify GoalsActivity needs to reload current goals
                    mChanged = true;

                    //complete notification text
                    switch(mGoals.get(i).getTimeType())
                    {
                        //today
                        case 0:
                            notificationText += "today.";
                            break;
                        case 1:
                            notificationText += "this week.";
                        case 2:
                            notificationText += "this month.";
                            break;
                        default:
                            notificationText += "ERROR";
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.mipmap.ic_stars_white_24dp)
                                    .setContentTitle(notificationTitle)
                                    .setContentText(notificationText);
// Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(this, GoalsActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(GoalsActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =

                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.

                    mNotificationManager.notify(mId, mBuilder.build());

                    //delete goal from database
                    dbHandler.deleteGoal(mGoals.get(i));
                    StaticData.getInstance().setGoalChanged(true);
                }


            }

            if (mChanged)
            {
                Intent intentUpdate = new Intent();
                intentUpdate.setAction(ACTION_UPDATE);
                intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
                intentUpdate.putExtra(EXTRA_KEY_DELETED, "true");
                sendBroadcast(intentUpdate);
            }

            //wait for X seconds
            try{
                Thread.sleep(5000);
            }
            catch(InterruptedException e) {}
        }
    }
}
