package com.project_wombat.runsmart;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoalAdapter extends ArrayAdapter<GoalModel> {

    private final Context context;
    private final ArrayList<GoalModel> goalsArrayList;


    public GoalAdapter(Context context, ArrayList<GoalModel> goalItems) {

        super(context, R.layout.goal_list, goalItems);

        this.context = context;
        this.goalsArrayList = goalItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        rowView = inflater.inflate(R.layout.goal_list, parent, false);

        // 3. Get icon,title & counter views from the rowView
        ImageView icon = (ImageView) rowView.findViewById(R.id.goalIcon);
        TextView goalView = (TextView) rowView.findViewById(R.id.goalDescription);
        TextView progressView = (TextView) rowView.findViewById(R.id.goalProgress);

        // 4. Set the text for textView
        icon.setImageResource(goalsArrayList.get(position).getIcon());
        goalView.setText(goalsArrayList.get(position).getGoal());
        progressView.setText(goalsArrayList.get(position).getProgress());

        // 5. return rowView
        return rowView;
    }
}