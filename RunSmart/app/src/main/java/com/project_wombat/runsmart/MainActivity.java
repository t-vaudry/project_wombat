package com.project_wombat.runsmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView walkButton;
    DBHandler dbHandler;
    GPSTracker gps;
    TextView stepCountView;

    //For drawerlist
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    //For count steps
    private StepCountBroadcastReceiver myBroadcastReceiver;

    private final String TAG = "Main Activity";
    private final int MY_PERMISSION = 0;

    //For drawer use
    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }

    public class StepCountBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String steps = intent.getStringExtra(StepCounter.EXTRA_KEY_STEPS);
            //NumberFormat numberFormat = new DecimalFormat("00000");
            //steps = numberFormat.format(steps);
            if (steps != null)
            {
                steps = String.format("%05d",Integer.parseInt(steps));
                stepCountView.setText(steps);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stepCountView = (TextView) findViewById(R.id.stepCountView);

        myBroadcastReceiver = new StepCountBroadcastReceiver();

        //register receiver
        IntentFilter intentFilter = new IntentFilter(StepCounter.ACTION_UPDATE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        mNavItems.add(new NavItem("Profile", "View personal information", R.mipmap.ic_person_black_24dp));
        mNavItems.add(new NavItem("Statistics", "View historical data", R.mipmap.ic_show_chart_black_24dp));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "onDrawerOpen: " + getTitle());
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        walkButton = (TextView) findViewById(R.id.walkButton);
        if (StaticData.getInstance().getCountSteps())
            walkButton.setText("STOP WALK");
        dbHandler = new DBHandler(this);

        //Set up walk listener
        RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.walkLayout);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                countSteps(v);            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (dbHandler.getProfile().getName() == null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void startRun(View view)
    {
        Intent intent = new Intent(this, RunActivity.class);
        startActivity(intent);
    }

    private void selectItemFromDrawer(int position) {
        if(mNavItems.get(position).mTitle.equals("Profile"))
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(mNavItems.get(position).mTitle.equals("Statistics"))
        {
            Intent intent = new Intent(this, StatsActivity.class);
            startActivity(intent);
        }
    }

    public void viewStatistics(View view)
    {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    public void countSteps(View view)
    {
        boolean countSteps = StaticData.getInstance().getCountSteps();
        StaticData.getInstance().setCountSteps(!countSteps);

        if(countSteps == true)
        {
            stepCountView.setText("00000");
            walkButton.setText("START WALK");
        }
        else
        {
            //start stepcount service
            Intent intent = new Intent(this,StepCounter.class);
            startService(intent);
            walkButton.setText("STOP WALK");
        }
    }
}
