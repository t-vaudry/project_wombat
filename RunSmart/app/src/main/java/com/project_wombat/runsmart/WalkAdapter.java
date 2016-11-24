package com.project_wombat.runsmart;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WalkAdapter extends ArrayAdapter<WalkModel> {

    private final Context context;
    private final ArrayList<WalkModel> walkList;


    public WalkAdapter(Context context, ArrayList<WalkModel> walkList) {

        super(context, R.layout.walk_list, walkList);

        this.context = context;
        this.walkList = walkList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        rowView = inflater.inflate(R.layout.walk_list, parent, false);

        // 3. Get icon,title & counter views from the rowView
        TextView dateView = (TextView) rowView.findViewById(R.id.walkDate);
        TextView stepView = (TextView) rowView.findViewById(R.id.walkCount);

        // 4. Set the text for textView
        dateView.setText(walkList.get(position).getDate());
        stepView.setText("Steps: " + walkList.get(position).getSteps());

        // 5. return rowView
        return rowView;
    }
}