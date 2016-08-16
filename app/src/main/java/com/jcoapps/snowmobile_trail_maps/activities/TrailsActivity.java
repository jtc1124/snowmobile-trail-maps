package com.jcoapps.snowmobile_trail_maps.activities;

import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 8/7/2016.
 */
public class TrailsActivity extends AppCompatActivity {

    private SnowmobileTrailDatabaseHelper dbHelper;
    private ListView list;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trails);
        dbHelper = new SnowmobileTrailDatabaseHelper(this);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            String message = b.getString("TRAIL_SAVE_MESSAGE");
            TextView trailAddedText = (TextView) findViewById(R.id.trailAddedText);
            trailAddedText.setText(message);
        }

        showTrails(dbHelper);
    }

    public void showTrails(SnowmobileTrailDatabaseHelper dbHelper) {
        list = (ListView)findViewById(R.id.trailsList);
        TrailsDao trailsDao = new TrailsDao(dbHelper);
        final List<TrailsDB> trailsDBList = trailsDao.getAllTrails();
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the trail that was selected
                TrailsDB selectedTrail = trailsDBList.get(position);

                // Pass the trail data to the MapActivity
                Intent displayTrail = new Intent(TrailsActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("SELECTED_TRAIL", selectedTrail);
                displayTrail.putExtras(b);
                startActivity(displayTrail);
            }
        });
    }
}
