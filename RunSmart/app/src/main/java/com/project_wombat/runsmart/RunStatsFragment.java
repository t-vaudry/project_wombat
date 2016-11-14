package com.project_wombat.runsmart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RunStatsFragment extends ListFragment {
    private GraphView runGraph;
    private ListView listView;
    private ArrayList<String> listItems;
    private RunStatsAdapter adapter;
    private DBHandler dbHandler;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Spinner runSpinner;
    private Date now;
    private PointsGraphSeries<DataPoint> series;
    private int month;
    private int runType;

    public RunStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        now = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        monthSpinner = (Spinner) view.findViewById(R.id.monthSpinner);
        yearSpinner = (Spinner) view.findViewById(R.id.yearSpinner);
        runSpinner = (Spinner) view.findViewById(R.id.runSpinner);
        runGraph = (GraphView) view.findViewById(R.id.runGraph);
        listView = (ListView) view.findViewById(android.R.id.list);
        listItems = new ArrayList<String>();
        dbHandler = new DBHandler(getContext());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.month_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        monthSpinner.setAdapter(monthAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position;
                series.resetData(refreshStats());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.year_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        yearSpinner.setAdapter(yearAdapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> runAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.run_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        runAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        runSpinner.setAdapter(runAdapter);

        runSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                runType = position;
                if (runType == 0)
                    runGraph.getGridLabelRenderer().setVerticalAxisTitle("Distance(m)");
                else if (runType == 1)
                    runGraph.getGridLabelRenderer().setVerticalAxisTitle("Duration(min)");
                else
                    runGraph.getGridLabelRenderer().setVerticalAxisTitle("Speed(km/h)");
                series.resetData(refreshStats());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        monthSpinner.setSelection(now.getMonth());

        adapter = new RunStatsAdapter(getContext(), listItems);
        listView.setAdapter(adapter);

        month = now.getMonth();
        runType = 0;
        series = new PointsGraphSeries<>(refreshStats());
        //size of point
        series.setSize(10);

        runGraph.addSeries(series);

        // set manual x bounds to have nice steps
        runGraph.getViewport().setMinX(1);
        runGraph.getViewport().setMaxX(31);
        runGraph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        runGraph.getGridLabelRenderer().setHumanRounding(true);
        runGraph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        runGraph.getGridLabelRenderer().setVerticalAxisTitle("Distance(m)");
}

    public DataPoint[] refreshStats() {
        listItems.clear();
        Calendar date = new GregorianCalendar();
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, 1);

        Date start = date.getTime();

        date.set(Calendar.DATE, date.getActualMaximum(Calendar.DATE));

        Date end = date.getTime();

        List<Run> runs = dbHandler.getBoundedRuns(start, end);

        DataPoint[] points = new DataPoint[runs.size()];

        for(int i=0; i<runs.size(); i++)
        {
            listItems.add(DateUtils.format(runs.get(i).getTimeStamp(), true));
            Date runTime = new Date(runs.get(i).getTimeStamp()*1000);
            if(runType == 0)
                points[i] = new DataPoint(runTime.getDate(), runs.get(i).getDistance());
            else if(runType == 1)
                points[i] = new DataPoint(runTime.getDate(), runs.get(i).getDuration()/60000);
            else
                points[i] = new DataPoint(runTime.getDate(), runs.get(i).getAvgSpeed());
        }

        adapter.notifyDataSetChanged();
        return points;
    }
}
