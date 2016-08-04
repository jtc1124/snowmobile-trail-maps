package com.jcoapps.snowmobile_trail_maps.activities;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.jcoapps.snowmobile_trail_maps.R;
import com.jcoapps.snowmobile_trail_maps.dao.TrailsDao;
import com.jcoapps.snowmobile_trail_maps.models.TrailPathsDB;
import com.jcoapps.snowmobile_trail_maps.models.TrailsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapView = null;
    private LocationDisplayManager locationDisplayManager;
    private Locator locator;
    private SpatialReference mapSr = null;
    private final static double ZOOM_BY = 1;
    private MultiPoint mapPoints;
    private GraphicsLayer graphicsLayer;
    private Polyline multipath;
    private Graphic path;
    private SimpleLineSymbol LINE_SYMBOL = new SimpleLineSymbol(Color.GREEN, 3, SimpleLineSymbol.STYLE.DASH);
    private List<Point> trailPoints;
    private List<TrailPathsDB> trailPaths;
    private TrailsDB trail;
    private SnowmobileTrailDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        trailPoints = new ArrayList<Point>();
        trailPaths = new ArrayList<TrailPathsDB>();
        trail = new TrailsDB();
        mapPoints = new MultiPoint();
        mapView = (MapView) findViewById(R.id.map);
        mapView.setOnStatusChangedListener(statusChangedListener);

        dbHelper = new SnowmobileTrailDatabaseHelper(this);
        //mapView.setOnSingleTapListener(mapTapCallback);

        // Setup geocoding service
        //setupLocator();
        // Setup service to display current device location
        setupLocationListener();
    }

    public void saveTrail(View view) {
        // Reset current path once saved?
        TextView trailAddedText = (TextView)findViewById(R.id.trailAddedText);
        TrailsDao trailsDao = new TrailsDao(dbHelper);
        trail.setName("My Trail");
        trail.setPaths(trailPaths);
        if (trailsDao.saveOrUpdateTrail(trail)) {
            trailAddedText.setText("Trail successfully created.");
        }
        else {
            trailAddedText.setText("Something went wrong. Trail was not created.");
        }
    }

    public void showTrail(View view) {
        TrailsDao trailsDao = new TrailsDao(dbHelper);
        TrailsDB trail = trailsDao.getTrailByName("My Trail");
        Collection<TrailPathsDB> paths = trail.getPaths();
        mapPoints.setEmpty();

        for (TrailPathsDB path : paths) {
            // TODO change trailpathsDb lat and lon to double
            Point coord = new Point(new Double(path.getLatitude()), new Double(path.getLongitude()));
            mapPoints.add(coord);
        }

        drawFullTrailPath(mapPoints);
    }

    private void setupLocationListener() {
        if ((mapView != null) && (mapView.isLoaded())) {
            locationDisplayManager = mapView.getLocationDisplayManager();
            locationDisplayManager.setLocationListener(new LocationListener() {
                boolean locationChanged = false;

                // Zooms to the current location when the first GPS fix arrives
                @Override
                public void onLocationChanged(Location loc) {
                    locationChanged = true;
                    zoomToLocation(loc);

                    // After zooming, turn on the location pan mode to show the location
                    // symbol. This will  disable as soon as you interact with the map.
                    locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);

                    Point currentCoord = getAsPoint(loc);

                    // Add the point to the drawable series of points
                    mapPoints.add(currentCoord);

                    TrailPathsDB currentPath = new TrailPathsDB();
                    currentPath.setLatitude(new Float(currentCoord.getX()));
                    currentPath.setLongitude(new Float(currentCoord.getY()));
                    currentPath.setTrail(trail);
                    trailPaths.add(currentPath);

                    // Add the point to the list of points that will be added to the database
                    //trailPoints.add(currentCoord);

                    // Only draw when there are 2 points available
                    if (mapPoints.getPointCount() == 2) {
                        drawPolylineOrPolygon(mapPoints);
                        // Once line is drawn, remove first point so we only keep track of 2 points at a time
                        mapPoints.removePoint(0);
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

    // Method to draw an entire trail path that was saved in the DB
    private void drawFullTrailPath(MultiPoint points) {
        if (graphicsLayer == null) {
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        if (multipath == null) {
            // Initialize the line and set the starting point
            multipath = new Polyline();
            multipath.startPath(points.getPoint(0).getX(), points.getPoint(0).getY());
        }
        if (path != null) {
            // If a graphic already exists, remove it since we'll be adding a new one.
            graphicsLayer.removeGraphic(path.getUid());
        }

        for (int i = 0; i < points.getPointCount(); i++) {
            multipath.lineTo(points.getPoint(i).getX(), points.getPoint(i).getY());
        }
        path = new Graphic(multipath, LINE_SYMBOL);
        graphicsLayer.addGraphic(path);
    }

    private void drawPolylineOrPolygon(MultiPoint points) {
        // Create and add graphics layer if it doesn't already exist
        if (graphicsLayer == null) {
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        if (multipath == null) {
            // Initialize the line and set the starting point
            multipath = new Polyline();
            multipath.startPath(points.getPoint(0).getX(), points.getPoint(0).getY());
        }
        if (path != null) {
            // If a graphic already exists, remove it since we'll be adding a new one.
            graphicsLayer.removeGraphic(path.getUid());
        }

        if (points.getPointCount() > 1) {
            multipath.lineTo(points.getPoint(1).getX(), points.getPoint(1).getY());

            path = new Graphic(multipath, LINE_SYMBOL);
            graphicsLayer.addGraphic(path);

            //Remove old point in the first place to save memory
            multipath.removePoint(0);
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
                SpatialReference.create(4326), mapSr);
    }
    private Point getAsPoint(TrailPathsDB path) {
        Point wgsPoint = new Point(new Double(path.getLongitude()), new Double(path.getLatitude()));
        return  (Point) GeometryEngine.project(wgsPoint,
                SpatialReference.create(4326), mapSr);
    }
}
