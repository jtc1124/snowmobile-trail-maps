package com.jcoapps.snowmobile_trail_maps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.map.MapView;

public class MainActivity extends AppCompatActivity {

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
