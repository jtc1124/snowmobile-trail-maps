package com.jcoapps.snowmobile_trail_maps.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jcoapps.snowmobile_trail_maps.R;
import com.jcoapps.snowmobile_trail_maps.dao.TrailsDao;
import com.jcoapps.snowmobile_trail_maps.models.TrailsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 8/5/2016.
 */
public class SaveTrailActivity extends AppCompatActivity {

    private TrailsDB trail;
    private SnowmobileTrailDatabaseHelper dbHelper;
    private ListView list;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_trail);
        dbHelper = new SnowmobileTrailDatabaseHelper(this);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            trail = (TrailsDB) b.getSerializable("TRAIL_DATA");
            EditText nameField = (EditText)findViewById(R.id.trailNameField);
            nameField.setText(trail.getName());
        }

        showTrails(dbHelper);
    }

    public void addTrail(View view) {
        EditText nameField = (EditText)findViewById(R.id.trailNameField);
        TextView trailAddedText = (TextView)findViewById(R.id.trailAddedText);

        TrailsDao trailsDao = new TrailsDao(dbHelper);
        trail.setName(nameField.getText().toString());
        if (trailsDao.saveOrUpdateTrail(trail)) {
            trailAddedText.setText("Trail successfully created.");
            showTrails(dbHelper);
        }
        else {
            trailAddedText.setText("Something went wrong. Trail was not successfully created.");
        }
    }

    public void showTrails(SnowmobileTrailDatabaseHelper dbHelper) {
        list = (ListView)findViewById(R.id.trailsList);
        TrailsDao trailsDao = new TrailsDao(dbHelper);
        List<TrailsDB> trailsDBList = trailsDao.getAllTrails();
        List<String> trailsList = new ArrayList<String>();

        // Transform the DB object into a string for each trail DB
        // and add to the list
        for (TrailsDB trail : trailsDBList) {
            String name = trail.getName();
            Long id = trail.getId();
            Integer numPoints = trail.getPaths().size();

            String trailString = name + "\nID: " + id + "\nPoint Count: " + numPoints;
            trailsList.add(trailString);
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.trails_list, trailsList);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postition, long id) {
                // TODO: when a list item is clicked, get the trail path data and send it to be drawn on the map

            }
        });
    }
}
