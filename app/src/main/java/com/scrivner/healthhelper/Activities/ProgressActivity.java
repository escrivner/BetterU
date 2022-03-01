package com.scrivner.healthhelper.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;

public class ProgressActivity extends AppCompatActivity {
    /*
    This class holds the logic for the main progress activity tab. It the progress you have achieved
    since using the app. It also allows you to weigh in and click into progress pictures.
     */
    Storage storage = new Storage();
    BottomNavigationView bottomNavigationView;
    TextView totalDeficitView;
    TextView totalBurnedView;
    TextView weightLostView;
    TextView currentWeightView;
    TextView startingWeightView;
    Button editWeighInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        initializeVariables();
        displayBottomNavigation();
        displayProgress();


    }

    public void openSettings(View view) {

        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }

    public void launchWeighInActivity(View view){

        Intent weighInIntent = new Intent(getApplicationContext(), WeighInActivity.class);
        startActivity(weighInIntent);
        finish();
    }

    private void displayProgress(){

        int totalDeficit = storage.loadIntFile(storage.TOTAL_DEFICIT, getApplicationContext());
        int totalBurned = storage.loadIntFile(storage.TOTAL_BURNED, getApplicationContext());
        int startingWeight = storage.loadIntFile(storage.STARTING_WEIGHT, getApplicationContext());
        int currentWeight = storage.loadIntFile(storage.CURRENT_WEIGHT, getApplicationContext());
        int weightLost = startingWeight - currentWeight;

        totalDeficitView.setText(Integer.toString(totalDeficit));
        totalBurnedView.setText(Integer.toString(totalBurned));
        weightLostView.setText(Integer.toString(weightLost));
        currentWeightView.setText(Integer.toString(currentWeight));
        startingWeightView.setText(Integer.toString(startingWeight));

    }

    private void initializeVariables(){

        totalDeficitView = findViewById(R.id.txt_totalDeficitProgress);
        totalBurnedView = findViewById(R.id.txt_totalBurnedProgress);
        weightLostView = findViewById(R.id.txt_totalWeightLostProgress);
        currentWeightView = findViewById(R.id.txt_currentWeightProgress);
        startingWeightView = findViewById(R.id.txt_startingWeightProgress);
        editWeighInButton = findViewById(R.id.btn_editWeightIn);

        editWeighInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), EditWeighInActivity.class));
                finish();
            }
        });


    }

    private void displayBottomNavigation(){

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.progress);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.exercise) {

                    startActivity(new Intent(getApplicationContext(), ExerciseActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.progress) {

                    return true;
                } else if (item.getItemId() == R.id.calories) {

                    startActivity(new Intent(getApplicationContext(), CaloriesActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }

                return false;
            }
        });
    }
}