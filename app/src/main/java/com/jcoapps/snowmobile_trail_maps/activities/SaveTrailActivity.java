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
import com.jcoapps.snowmobile_trail_maps.dao.TrailJournalsDao;
import com.jcoapps.snowmobile_trail_maps.dao.TrailsDao;
import com.jcoapps.snowmobile_trail_maps.models.TrailJournalsDB;
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
    }

    public void addTrail(View view) {
        EditText nameField = (EditText)findViewById(R.id.trailNameField);
        TextView trailAddedText = (TextView)findViewById(R.id.trailAddedText);

        TrailsDao trailsDao = new TrailsDao(dbHelper);
        trail.setName(nameField.getText().toString());
        if (trailsDao.saveOrUpdateTrail(trail)) {
            TrailJournalsDB journal = trail.getJournals().get(0);
            journal.setTrail(trail);
            TrailJournalsDao journalsDao = new TrailJournalsDao(dbHelper);
            journalsDao.saveOrUpdateTrailJournal(journal);
            // Open TrailsActivity to display the trails list on successful save
            Intent trails = new Intent(SaveTrailActivity.this, TrailsActivity.class);
            Bundle b = new Bundle();
            b.putString("TRAIL_SAVE_MESSAGE", "Trail was saved successfully.");
            trails.putExtras(b);
            startActivity(trails);
        }
        else {
            trailAddedText.setText("Something went wrong. Trail was not successfully created.");
        }
    }
}
