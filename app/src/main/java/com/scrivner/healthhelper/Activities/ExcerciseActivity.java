package com.scrivner.healthhelper.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scrivner.healthhelper.Methods;
import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;
import com.scrivner.healthhelper.Timer;

public class ExcerciseActivity extends AppCompatActivity {

    /*

     */
    Storage storage = new Storage();
    Methods methods = new Methods();
    Timer timer = new Timer();

    BottomNavigationView bottomNavigationView;
    NumberPicker onesPicker;
    NumberPicker tensPicker;
    NumberPicker hundredsPicker;
    NumberPicker thousandsPicker;
    TextView burnedView;
    Button addExcerciseButton;

    int currentBurned = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise);

        burnedView = findViewById(R.id.txt_burned);

        onesPicker = findViewById(R.id.onesNumPicker2);
        tensPicker = findViewById(R.id.tensNumPicker2);
        hundredsPicker = findViewById(R.id.hundredsNumPicker2);
        thousandsPicker = findViewById(R.id.thousNumPicker2);
        onesPicker.setMinValue(0);
        tensPicker.setMinValue(0);
        hundredsPicker.setMinValue(0);
        thousandsPicker.setMinValue(0);
        onesPicker.setMaxValue(9);
        tensPicker.setMaxValue(9);
        hundredsPicker.setMaxValue(9);
        thousandsPicker.setMaxValue(9);

        if (methods.checkForNewDay(getApplicationContext())) {

            storage.saveFile(0, storage.CURRENT_CAL, getApplicationContext());
            storage.saveFile(0, storage.BURNED_CAL, getApplicationContext());
        }
        displayExercise();
        //timer.startTimer(getApplicationContext());
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.exercise);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.exercise) {

                    return true;
                } else if (item.getItemId() == R.id.progress) {

                    startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.calories) {

                    startActivity(new Intent(getApplicationContext(), CaloriesActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.counter) {

                    startActivity(new Intent(getApplicationContext(), CounterActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        displayExercise();
    }

    public void addExcercise(View view) {

        currentBurned = storage.loadIntFile(storage.BURNED_CAL, getApplicationContext());
        int ones = onesPicker.getValue();
        int tens = tensPicker.getValue() * 10;
        int hundreds = hundredsPicker.getValue() * 100;
        int thousands = thousandsPicker.getValue() * 1000;
        int addedExcercise = ones + tens + hundreds + thousands;

        currentBurned += addedExcercise;
        onesPicker.setValue(0);
        tensPicker.setValue(0);
        hundredsPicker.setValue(0);
        thousandsPicker.setValue(0);


        storage.saveFile(currentBurned, storage.BURNED_CAL, getApplicationContext());
        displayExercise();
    }

    public void displayExercise() {

        try {
            currentBurned = storage.loadIntFile(storage.BURNED_CAL, getApplicationContext());
        } catch (Exception e) {

            e.printStackTrace();
            storage.saveFile(0, storage.BURNED_CAL, getApplicationContext());
            currentBurned = storage.loadIntFile(storage.BURNED_CAL, getApplicationContext());
        }

        burnedView.setText(Integer.toString(currentBurned));
    }

    public void displayEditActivity(View view) {

        startActivity(new Intent(getApplicationContext(), EditExcerciseActivity.class));
    }
}