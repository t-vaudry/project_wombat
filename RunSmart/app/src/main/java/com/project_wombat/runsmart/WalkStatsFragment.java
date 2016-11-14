package com.project_wombat.runsmart;

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

public class WalkStatsFragment extends ListFragment {
    private Date now;
    private GraphView walkGraph;
    private ListView listView;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    private DBHandler dbHandler;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private PointsGraphSeries<DataPoint> series;

    public WalkStatsFragment() {
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
        return inflater.inflate(R.layout.fragment_walk_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        monthSpinner = (Spinner) view.findViewById(R.id.monthSpinner);
        yearSpinner = (Spinner) view.findViewById(R.id.yearSpinner);
        walkGraph = (GraphView) view.findViewById(R.id.walkGraph);
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
                series.resetData(refreshStats(position));
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

        monthSpinner.setSelection(now.getMonth());

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        series = new PointsGraphSeries<>(refreshStats(now.getMonth()));
        series.setSize(10);

        walkGraph.addSeries(series);

        // set manual x bounds to have nice steps
        walkGraph.getViewport().setMinX(1);
        walkGraph.getViewport().setMaxX(31);
        walkGraph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        walkGraph.getGridLabelRenderer().setHumanRounding(true);
        walkGraph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        walkGraph.getGridLabelRenderer().setVerticalAxisTitle("Steps");
    }

    public DataPoint[] refreshStats(int month) {
        listItems.clear();
        Calendar date = new GregorianCalendar();
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, 1);

        Date start = date.getTime();

        date.set(Calendar.DATE, date.getActualMaximum(Calendar.DATE));

        Date end = date.getTime();

        List<Steps> steps = dbHandler.getBoundedSteps(start, end);

        DataPoint[] points = new DataPoint[steps.size()];

        for(int i=0; i<steps.size(); i++)
        {
            Date stepTime = new Date(steps.get(i).getTimeStamp()*1000);
            points[i] = new DataPoint(stepTime.getDate(), steps.get(i).getSteps());
            listItems.add(DateUtils.format(steps.get(i).getTimeStamp(), false));
        }

        adapter.notifyDataSetChanged();
        return points;
    }
}
