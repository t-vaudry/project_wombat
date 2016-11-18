package com.project_wombat.runsmart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private SupportMapFragment fragment;
    private DBHandler dbHandler;
    private List<RunData> runData;
    private Date runDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new SupportMapFragment();
        dbHandler = new DBHandler(this);
        runDate =  new Date();

        Bundle extras = getIntent().getExtras();
        runDate.setTime(extras.getLong("TIME_STAMP", -1));

        getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();

        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        PolylineOptions options = new PolylineOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        runData = dbHandler.getRunData(runDate);

        for(RunData i : runData)
        {
            LatLng position = new LatLng(i.getLatitude(), i.getLongitude());
            options.add(position);
            builder.include(position);
        }

        final LatLngBounds bounds = builder.build();
        map.addPolyline(options.color(Color.BLUE));

        // Move the map so that it is centered on the polyline.
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        });
    }
}
