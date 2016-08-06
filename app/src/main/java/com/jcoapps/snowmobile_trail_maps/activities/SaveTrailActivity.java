package com.jcoapps.snowmobile_trail_maps.activities;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jcoapps.snowmobile_trail_maps.R;
import com.jcoapps.snowmobile_trail_maps.dao.TrailsDao;
import com.jcoapps.snowmobile_trail_maps.models.TrailsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

/**
 * Created by Jeremy on 8/5/2016.
 */
public class SaveTrailActivity extends AppCompatActivity {

    private TrailsDB trail;
    private SnowmobileTrailDatabaseHelper dbHelper;

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
            trailAddedText.setText("Trail successfully created.");
        }
        else {
            trailAddedText.setText("Something went wrong. Trail was not successfully created.");
        }
    }
}
