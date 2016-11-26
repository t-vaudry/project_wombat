package com.project_wombat.runsmart;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RunStatsAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> runArrayList;

    public RunStatsAdapter(Context context, ArrayList<String> listItems) {

        super(context, R.layout.custom_list_item, listItems);

        this.context = context;
        this.runArrayList = listItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        rowView = inflater.inflate(R.layout.custom_list_item, parent, false);

        // 3. Get icon,title & counter views from the rowView
        TextView timestampView = (TextView) rowView.findViewById(R.id.timestamp);
        ImageView statsView = (ImageView) rowView.findViewById(R.id.statsIcon);
        ImageView mapView = (ImageView) rowView.findViewById(R.id.mapIcon);

        // 4. Set the text for textView
        timestampView.setText(runArrayList.get(position));

        // 5. Set listeners
        statsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RunDataActivity.class);
                intent.putExtra("TIME_STAMP", DateUtils.parse(runArrayList.get(position)).getTime());
                context.startActivity(intent);
            }
        });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission( context, Manifest.permission.MAPS_RECEIVE ) != PackageManager.PERMISSION_GRANTED )
                {
                    //permissions not set

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage("Google Maps permissions not set for RunSmart. Please allow to continue.");
                    dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + context.getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                            context.startActivity(i);
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.show();
                }
                else
                {
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("TIME_STAMP", DateUtils.parse(runArrayList.get(position)).getTime());
                    context.startActivity(intent);
                }
            }
        });

        // 6. return rowView
        return rowView;
    }
}
