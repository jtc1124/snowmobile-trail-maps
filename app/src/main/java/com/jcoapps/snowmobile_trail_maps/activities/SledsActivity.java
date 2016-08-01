package com.jcoapps.snowmobile_trail_maps.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.jcoapps.snowmobile_trail_maps.R;
import com.jcoapps.snowmobile_trail_maps.dao.SledsDao;
import com.jcoapps.snowmobile_trail_maps.models.SledsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

/**
 * Created by Jeremy on 8/1/2016.
 */
public class SledsActivity extends AppCompatActivity {

    private SnowmobileTrailDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new SnowmobileTrailDatabaseHelper(this);
        setContentView(R.layout.activity_sleds);
    }

    public void addSled(View view) {
        EditText nameField = (EditText)findViewById(R.id.sledNameField);
        EditText mileageField = (EditText)findViewById(R.id.sledMileageField);
        EditText notesField = (EditText)findViewById(R.id.sledNotesField);

        String name = nameField.getText().toString();
        Double mileage = new Double(mileageField.getText().toString());
        String notes = notesField.getText().toString();

        SledsDao sledsDao = new SledsDao(dbHelper);
        SledsDB sled = new SledsDB();
        sled.setName(name);
        sled.setMileage(mileage);
        sled.setNotes(notes);

        sledsDao.saveOrUpdateSled(sled);
    }
}
