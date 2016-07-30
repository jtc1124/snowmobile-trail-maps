package com.jcoapps.snowmobile_trail_maps.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.map.MapView;
import com.jcoapps.snowmobile_trail_maps.R;

public class MainActivity extends AppCompatActivity {

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get or create SQLite database

        setContentView(R.layout.activity_main);
    }
}
