package com.scrivner.healthhelper.Activities;

import static android.os.Build.VERSION_CODES.R;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scrivner.healthhelper.CalListAdapter;
import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;

import java.util.ArrayList;

public class EditCaloriesActivity extends AppCompatActivity {

    /*
    This class holds the logic runs the edit calories activity. It has the ability load
    all of the saved calorie files into a single ArrayList and display it into a listview.
    It can also remove and sort the text files after an entry was deleted.
     */

    Storage storage = new Storage();
    CaloriesActivity caloriesActivity = new CaloriesActivity();
    TextView calPosView;
    TextView calTimeView;
    TextView calEntryView;
    ListView calListView;
    ArrayList<CalObject> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(com.scrivner.healthhelper.R.layout.activity_edit_calories);

        calPosView = findViewById(com.scrivner.healthhelper.R.id.calPosView );
        calTimeView = findViewById(com.scrivner.healthhelper.R.id.calTimeView);
        calEntryView = findViewById(com.scrivner.healthhelper.R.id.calEntryView);
        calListView = findViewById(com.scrivner.healthhelper.R.id.caloriesListView);

        buildArray();
        setAdapter();
        calListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), i + " ", Toast.LENGTH_SHORT).show();
                i -= 1;
                int pos = i + 1;
                String currentEntryFile = "edit_calories_input_" + pos + ".txt";
                String currentTimeFile = "edit_calories_time_" + pos + ".txt";
                CalObject object = array.get(i);
                int currentCal = object.getEntry();
                int totalCal = storage.loadIntFile(storage.CURRENT_CAL, getApplicationContext());
                totalCal -= currentCal;

                storage.saveFile(totalCal, storage.CURRENT_CAL, getApplicationContext());
                storage.saveStringFile("0:00:00", currentTimeFile, getApplicationContext());
                storage.saveFile(0, currentEntryFile, getApplicationContext());
                cleanInputFiles(pos);
                //caloriesActivity.displayCalories();
                buildArray();
                setAdapter();

                Intent calIntent = new Intent(getApplicationContext(), CaloriesActivity.class);
                startActivity(calIntent);
                finish();
            }
        });

    }

    public void cleanInputFiles(int position){
        /*
        This method takes the position of the file that has been deleted and shifts the data
        so that there aren't just a bunch of empty files left around.
         */

        int totalPositions = storage.loadIntFile(storage.TOTAL_CALORIES_INPUTS, getApplicationContext());

        for(int i = position; i < totalPositions; i++){

            String currentFile = "edit_calories_input_" + i + ".txt";
            String nextFile = "edit_calories_input_" + (i + 1) + ".txt";
            String currentTimeFile = "edit_calories_time_" + i + ".txt";
            String nextTimeFile = "edit_calories_time_" + (i + 1) + ".txt";

            int nextInput = storage.loadIntFile(nextFile, getApplicationContext());
            String nextTime = storage.loadStringFile(nextTimeFile, getApplicationContext());
            storage.saveFile(nextInput, currentFile, getApplicationContext());
            storage.saveStringFile(nextTime, currentTimeFile, getApplicationContext());

        }

        storage.saveFile( (totalPositions - 1), storage.TOTAL_CALORIES_INPUTS, getApplicationContext());



    }

    public void buildArray(){
        /*
        Opens all of the input text files that have been saved and loads them into an ArrayList.
         */

        array = new ArrayList<CalObject>();
        int totalInputs = storage.loadIntFile(storage.TOTAL_CALORIES_INPUTS, getApplicationContext());

        for(int i = 0; i < totalInputs + 1; i++){

            int pos = i + 1;
            String calEntryFile = "edit_calories_input_" + pos + ".txt";
            String calTimeFile = "edit_calories_time_" + pos + ".txt";
            int calEntry = storage.loadIntFile(calEntryFile, getApplicationContext());
            String calTime = storage.loadStringFile(calTimeFile, getApplicationContext());

            CalObject entry = new CalObject(calEntry, calTime);
            array.add(entry);
        }

        if(totalInputs == 0){
            CalObject entry = new CalObject(0, "0:00");
            array.add(entry);
        }

    }

    public void setAdapter(){
        /*
        Just creates the list adapter for the list view, could probably just be moved into buildArray().
         */
        CalListAdapter adapter = new CalListAdapter(this, com.scrivner.healthhelper.R.layout.adapter_view_layout, array);
        calListView.setAdapter(adapter);

    }
}