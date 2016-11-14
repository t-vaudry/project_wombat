package com.project_wombat.runsmart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    private LinearLayout mEditLayout;
    private EditText mEditValue;
    private Button mSaveButton;
    private Spinner mTypeSpinner;
    private Spinner mRunSpinner;
    private Spinner mDurationSpinner;
    private TextView mStepsText;
    private TextView mTitleText;
    private DBHandler dbHandler;

    private boolean mDeleteMode;
    private boolean mAddMode;

    private MenuItem mAddItem;

    private GoalAdapter mGoalAdapter;
    private ListView mGoalListView;

    private ArrayList<GoalModel> mGoalListItems;
    //private ArrayList<String> mProgressListItems;

    private List<Goal> mGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        dbHandler = new DBHandler(this);

        mEditLayout = (LinearLayout) findViewById(R.id.editLayout);

        mGoalListView = (ListView) findViewById(R.id.goalList);
        mGoalListItems = new ArrayList<GoalModel>();
        mGoals = new ArrayList<Goal>();

        //Hide goal input box
        mEditLayout.setVisibility(View.GONE);

        //Find all views
        mEditValue = (EditText) findViewById(R.id.editAmount);

        mSaveButton = (Button) findViewById(R.id.SaveButton);
        mSaveButton.setVisibility(View.GONE);

        mTypeSpinner = (Spinner) findViewById(R.id.spinnerGoalType);
        mRunSpinner = (Spinner) findViewById(R.id.spinnerRunType);
        mDurationSpinner = (Spinner) findViewById(R.id.spinnerDuration);

        mStepsText = (TextView) findViewById(R.id.stepText);
        mTitleText = (TextView) findViewById(R.id.goalText);

        //Get goals from db
        mGoals = dbHandler.getAllGoals();

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0)//Walk
                {
                    mRunSpinner.setVisibility(View.GONE);
                    mStepsText.setVisibility(View.VISIBLE);
                }

                else
                {
                    mRunSpinner.setVisibility(View.VISIBLE);
                    mStepsText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        mGoalAdapter = new GoalAdapter(this, mGoalListItems);
        mGoalListView.setAdapter(mGoalAdapter);
        mGoalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDeleteMode)
                {
                    dbHandler.deleteGoal(mGoals.get(position));
                    mGoals.remove(position);
                    mGoalListItems.remove(position);
                    mGoalAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.goalmenu, menu);
        mAddItem = menu.findItem(R.id.action_add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_add)
        {
            mAddMode = !mAddMode;

            if (mAddMode) //Now adding items
            {
                item.setIcon(R.mipmap.ic_clear_white_24dp);
                mEditLayout.setVisibility(View.VISIBLE);
                mSaveButton.setVisibility(View.VISIBLE);
                mTitleText.setText(R.string.add_goal);
            }
            else
            {
                item.setIcon(R.mipmap.ic_add_white_24dp);
                mEditLayout.setVisibility(View.GONE);
                mSaveButton.setVisibility(View.GONE);
                mTitleText.setText(R.string.goal_title);
            }

        }
        else if (id == R.id.action_delete)
        {
            mDeleteMode = !mDeleteMode;

            if (mDeleteMode) //Now want to be able to delete things
                item.setIcon(R.mipmap.ic_check_white_24dp);
            else
                item.setIcon(R.mipmap.ic_delete_white_24dp);

        }
        else if (id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGoalListItems.clear();
        mDeleteMode = false;
        mAddMode = false;
        mTitleText.setText(R.string.goal_title);
        loadCurrentGoals();
        getProgress();
    }

    public void saveClicked(View view)
    {
        if(mEditValue.getText().toString().matches(""))
        {
            mEditValue.setError(getText(R.string.no_goal));
        }
        else
        {
            mTitleText.setText(R.string.goal_title);

            mAddItem.setIcon(R.mipmap.ic_add_white_24dp);
            mAddMode = !mAddMode;

            mEditLayout.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.GONE);

            //Display the newly added goal
            TextView text = new TextView(this);
            String type = mTypeSpinner.getSelectedItem().toString();
            String ending;

            int goalType;
            int durationType;
            int icon = 0;

            if (type.equals("Walk"))
            {
                ending = "steps";
                goalType = 0;
                icon = R.mipmap.ic_directions_walk_black_24dp;
            }
            else
            {
                ending = mRunSpinner.getSelectedItem().toString();
                icon = R.mipmap.ic_directions_run_black_24dp;
                if (ending.equals("minutes"))
                    goalType = 1;
                else
                    goalType = 2;
            }

            String g = type + " " + mEditValue.getText() + " " + ending + " " + mDurationSpinner.getSelectedItem().toString();
            GoalModel gm1 = new GoalModel(icon,g,"0");
            mGoalListItems.add(gm1);
            mGoalAdapter.notifyDataSetChanged();

            //Add the new goal to the database
            durationType = mDurationSpinner.getSelectedItemPosition();
            Date endDate = getEndDate(durationType, new Date());
            Date startDate = getStartDate(durationType);
            Goal goal = new Goal(goalType, durationType, Integer.parseInt(mEditValue.getText().toString()), startDate, endDate);
            dbHandler.addGoal(goal);
            mGoals.add(goal);

            //Update progress for newly added goal
            getProgress(goal);
            mGoalAdapter.notifyDataSetChanged();
        }
    }

    public Date getEndDate(int durationType, Date startDate)
    {
        // today
        Calendar date = new GregorianCalendar();

        //set to 11:59pm
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 99);

        switch(durationType)
        {
            //toda
            case 0:
                break;
            //this week
            case 1:
                date.set(Calendar.DAY_OF_WEEK, date.getActualMaximum(Calendar.DAY_OF_WEEK));
                break;

            //this month
            case 2:
                date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
        }

        return date.getTime();
    }

    public Date getStartDate(int durationType)
    {
        //today
        Calendar date = new GregorianCalendar();

        //set to midnight
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        switch(durationType)
        {
            //today
            case 0:
                break;
            //this week
            case 1:
                date.set(Calendar.DAY_OF_WEEK, date.getActualMinimum(Calendar.DAY_OF_WEEK));
                break;
            //this month
            case 2:
                date.set(Calendar.DAY_OF_MONTH, date.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
        }
        return date.getTime();
    }

    public void getProgress()
    {
        int count = 0;
        int percentage = 0;
        for (int i = 0; i < mGoals.size(); i++)
        {
            switch(mGoals.get(i).getGoalType())
            {
                //Walk
                case 0:
                    count = dbHandler.getStepCountBounded(mGoals.get(i).getStart_date(), mGoals.get(i).getEndDate());
                    percentage = (int)((float)count/(float)mGoals.get(i).getValue() * 100);
                    break;
                case 1:
                    count = (int)(dbHandler.getRunTimeBounded(mGoals.get(i).getStart_date(), mGoals.get(i).getEndDate()));
                    percentage = (int)((float)count/((float)mGoals.get(i).getValue()*60000) * 100);
                    break;
                case 2:
                    count = (int)dbHandler.getRunDistanceBounded(mGoals.get(i).getStart_date(), mGoals.get(i).getEndDate());
                    percentage = (int)((float)count/(float)mGoals.get(i).getValue() * 100);
                    break;
            }


            if (percentage > 100)
                percentage = 100;
            mGoalListItems.get(i).setProgress(String.valueOf(percentage) + "%");
        }

        mGoalAdapter.notifyDataSetChanged();
    }

    public void getProgress(Goal goal)
    {
        int count = 0;
        int percentage = 0;
        switch(goal.getGoalType())
        {
            //Walk
            case 0:
                count = dbHandler.getStepCountBounded(mGoals.get(mGoals.size()-1).getStart_date(), mGoals.get(mGoals.size()-1).getEndDate());
                percentage = (int)((float)count/(float)mGoals.get(mGoals.size()-1).getValue() * 100);
                break;
            case 1:
                count = (int)dbHandler.getRunTimeBounded(mGoals.get(mGoals.size()-1).getStart_date(), mGoals.get(mGoals.size()-1).getEndDate());
                percentage = (int)((float)count/((float)mGoals.get(mGoals.size()-1).getValue()*60000) * 100);
                break;
            case 2:
                count = (int)dbHandler.getRunDistanceBounded(mGoals.get(mGoals.size()-1).getStart_date(), mGoals.get(mGoals.size()-1).getEndDate());
                percentage = (int)((float)count/(float)mGoals.get(mGoals.size()-1).getValue() * 100);
                break;
        }

        if (percentage > 100)
            percentage = 100;
        int size = mGoals.size()-1;

        mGoalListItems.get(size).setProgress(String.valueOf(percentage) + "%");
    }

    public void loadCurrentGoals()
    {
        List<Goal> goals = dbHandler.getAllGoals();
        int typeInt;
        String type;
        String value;
        String ending;
        int durationInt;
        String duration;
        int icon = 0;
        

        for(Goal goal : goals)
        {
            TextView textView = new TextView(this);
            typeInt = goal.getGoalType();
            switch (typeInt)
            {
                case 0:
                    type = "Walk ";
                    ending = " steps";
                    icon = R.mipmap.ic_directions_walk_black_24dp;
                    break;
                case 1:
                    type = "Run ";
                    ending = " minutes";
                    icon = R.mipmap.ic_directions_run_black_24dp;
                    break;
                case 2:
                    type = "Run ";
                    ending = " kilometres";
                    icon = R.mipmap.ic_directions_run_black_24dp;
                    break;
                default:
                    type = "ERROR ";
                    ending = "ERROR";
                    break;
            }
            value = Integer.toString(goal.getValue());

            durationInt = goal.getTimeType();
            switch (durationInt)
            {
                case 0:
                    duration = " today";
                    break;
                case 1:
                    duration = " this week";
                    break;
                case 2:
                    duration = " this month";
                    break;
                default:
                    duration = "ERROR";
                    break;
            }

            String g = type + value + ending + duration;
            mGoalListItems.add(new GoalModel(icon,g,"0"));
        }
        mGoalAdapter.notifyDataSetChanged();
    }
}
