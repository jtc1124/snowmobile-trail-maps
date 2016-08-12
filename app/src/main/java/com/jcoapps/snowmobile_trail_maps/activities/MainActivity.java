package com.jcoapps.snowmobile_trail_maps.activities;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.tasks.geocode.Locator;
import com.jcoapps.snowmobile_trail_maps.R;
import com.jcoapps.snowmobile_trail_maps.dao.ConditionTypesDao;
import com.jcoapps.snowmobile_trail_maps.models.ConditionTypesDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;
import com.jcoapps.snowmobile_trail_maps.types.ConditionTypes;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SnowmobileTrailDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get or create SQLite database
        dbHelper = new SnowmobileTrailDatabaseHelper(this);
    }

    public void viewMap(View view) {
        Intent map = new Intent(MainActivity.this, MapActivity.class);
        startActivity(map);
    }

    public void viewDB(View view) {
        Intent db = new Intent(MainActivity.this, AndroidDatabaseManager.class);
        startActivity(db);
    }

    public void sleds(View view) {
        Intent sleds = new Intent(MainActivity.this, SledsActivity.class);
        startActivity(sleds);
    }

    public void trails(View view) {
        Intent trails = new Intent(MainActivity.this, TrailsActivity.class);
        startActivity(trails);
    }
}
