package com.scrivner.healthhelper.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scrivner.healthhelper.CalListAdapter;
import com.scrivner.healthhelper.Methods;
import com.scrivner.healthhelper.Storage;

import java.util.ArrayList;

public class EditCaloriesActivity extends AppCompatActivity {

    /*
    This class holds the logic runs the edit calories activity. It has the ability load
    all of the saved calorie files into a single ArrayList and display it into a listview.
    It can also remove and sort the text files after an entry was deleted.
     */

    Storage storage = new Storage();
    Methods methods = new Methods();
    CaloriesActivity caloriesActivity = new CaloriesActivity();

    TextView calEntryView;
    ListView calListView;
    ArrayList<CalObject> array;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent calIntent = new Intent(getApplicationContext(), CaloriesActivity.class);
        startActivity(calIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(com.scrivner.healthhelper.R.layout.activity_edit_calories);

        calEntryView = findViewById(com.scrivner.healthhelper.R.id.calInputsDetected);
        calListView = findViewById(com.scrivner.healthhelper.R.id.caloriesListView);


        array = methods.buildArray(methods.CALORIES, getApplicationContext());

        if(array.size() > 0){

            calEntryView.setVisibility(View.INVISIBLE);
        }
        CalListAdapter adapter = new CalListAdapter(this, com.scrivner.healthhelper.R.layout.adapter_view_layout, array);
        calListView.setAdapter(adapter);
        calListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String currentEntryFile = "edit_calories_input_" + i + ".txt";
                String currentTimeFile = "edit_calories_time_" + i + ".txt";
                CalObject object = array.get(i);
                int currentCal = object.getEntry();
                int totalCal = storage.loadIntFile(storage.CURRENT_CAL, getApplicationContext());
                totalCal -= currentCal;

                storage.saveFile(totalCal, storage.CURRENT_CAL, getApplicationContext());
                cleanInputFiles(i);

                methods.buildArray(methods.CALORIES, getApplicationContext());
                CalListAdapter adapter = new CalListAdapter(getApplicationContext(), com.scrivner.healthhelper.R.layout.adapter_view_layout, array);
                calListView.setAdapter(adapter);

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

        /*int totalPositions = storage.loadIntFile(storage.TOTAL_CALORIES_INPUTS, getApplicationContext());

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

        storage.saveFile( (totalPositions - 1), storage.TOTAL_CALORIES_INPUTS, getApplicationContext());*/


        methods.cleanInputFiles(position, methods.CALORIES, getApplicationContext());
    }



}