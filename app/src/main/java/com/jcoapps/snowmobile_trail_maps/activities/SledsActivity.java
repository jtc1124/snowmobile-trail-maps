package com.jcoapps.snowmobile_trail_maps.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jcoapps.snowmobile_trail_maps.R;
import com.jcoapps.snowmobile_trail_maps.dao.SledsDao;
import com.jcoapps.snowmobile_trail_maps.models.SledsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 8/1/2016.
 */
public class SledsActivity extends AppCompatActivity {

    private SnowmobileTrailDatabaseHelper dbHelper;
    private ListView list;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new SnowmobileTrailDatabaseHelper(this);
        setContentView(R.layout.activity_sleds);
        showSleds(dbHelper);
    }

    public void addSled(View view) {
        EditText yearField = (EditText)findViewById(R.id.sledYearField);
        EditText makeField = (EditText)findViewById(R.id.sledMakeField);
        EditText modelField = (EditText)findViewById(R.id.sledModelField);
        EditText mileageField = (EditText)findViewById(R.id.sledMileageField);
        EditText notesField = (EditText)findViewById(R.id.sledNotesField);
        TextView sledAddedText = (TextView)findViewById(R.id.sledAddedText);

        Integer year = new Integer(yearField.getText().toString());
        String make = makeField.getText().toString();
        String model = modelField.getText().toString();
        Double mileage = new Double(mileageField.getText().toString());
        String notes = notesField.getText().toString();

        SledsDao sledsDao = new SledsDao(dbHelper);
        SledsDB sled = new SledsDB();
        sled.setYear(year);
        sled.setMake(make);
        sled.setModel(model);
        sled.setMileage(mileage);
        sled.setNotes(notes);

        if (sledsDao.saveOrUpdateSled(sled)) {
            sledAddedText.setText("Sled successfully created.");
            showSleds(dbHelper);
        }
        else {
            sledAddedText.setText("Something went wrong. Sled was not created.");
        }
    }

    // Populate sleds list to display all the sleds in the database
    public void showSleds(SnowmobileTrailDatabaseHelper dbHelper) {
        list = (ListView)findViewById(R.id.sledsList);
        SledsDao sledsDao = new SledsDao(dbHelper);
        List<SledsDB> sledsDBList = sledsDao.getAllSleds();
        List<String> sledsList = new ArrayList<String>();

        // Transform the DB object into a string for each sled DB
        // and add to the list
        for (SledsDB sled : sledsDBList) {
            String year = sled.getYear().toString();
            String make = sled.getMake();
            String model = sled.getModel();
            String mileage = sled.getMileage().toString();
            String notes = sled.getNotes();
            String sledString = year + " " + make + " " + model + "\nMileage: " + mileage + "\nNotes: " + notes;
            sledsList.add(sledString);
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.sleds_list, sledsList);
        list.setAdapter(listAdapter);
    }
}
