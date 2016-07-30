package com.jcoapps.snowmobile_trail_maps.activities;

import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private SnowmobileTrailDatabaseHelper dbHelper;
    private MapView mapView = null;
    private LocationDisplayManager locationDisplayManager;
    private Locator locator;
    private SpatialReference mapSr = null;
    private final static double ZOOM_BY = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get or create SQLite database
        dbHelper = new SnowmobileTrailDatabaseHelper(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.setOnStatusChangedListener(statusChangedListener);
        //mapView.setOnSingleTapListener(mapTapCallback);

        // Setup geocoding service
        //setupLocator();
        // Setup service to display current device location
        setupLocationListener();
    }

    private void setupLocationListener() {
        if ((mapView != null) && (mapView.isLoaded())) {
            locationDisplayManager = mapView.getLocationDisplayManager();
            locationDisplayManager.setLocationListener(new LocationListener() {

                boolean locationChanged = false;

                // Zooms to the current location when the first GPS fix arrives
                @Override
                public void onLocationChanged(Location loc) {
                    if (!locationChanged) {
                        locationChanged = true;
                        zoomToLocation(loc);

                        // After zooming, turn on the location pan mode to show the location
                        // symbol. This will  disable as soon as you interact with the map.
                        locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    }
                }

                @Override
                public void onProviderDisabled(String arg0) {}

                @Override
                public void onProviderEnabled(String arg0) {}

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
            });

            locationDisplayManager.start();
        }
    }

    // Zoom to location
    private void zoomToLocation(Location loc) {
        Point mapPoint = getAsPoint(loc);
        Unit mapUnit = mapSr.getUnit();
        double zoomFactor = Unit.convertUnits(ZOOM_BY,
                Unit.create(LinearUnit.Code.MILE_US), mapUnit);
        Envelope zoomExtent = new Envelope(mapPoint, zoomFactor, zoomFactor);
        mapView.setExtent(zoomExtent);
    }

    private final OnStatusChangedListener statusChangedListener = new OnStatusChangedListener() {
        @Override
        public void onStatusChanged(Object source, STATUS status) {
            if (source == mapView && status == STATUS.INITIALIZED) {
                mapSr = mapView.getSpatialReference();

                if(locationDisplayManager == null) {
                    setupLocationListener();
                }
            }
        }
    };

    private Point getAsPoint(Location loc) {
        Point wgsPoint = new Point(loc.getLongitude(), loc.getLatitude());
        return  (Point) GeometryEngine.project(wgsPoint,
                SpatialReference.create(SpatialReference.WKID_WGS84), mapSr);
    }
}
