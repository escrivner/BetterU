package com.scrivner.healthhelper.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scrivner.healthhelper.Activities.CaloriesActivity;
import com.scrivner.healthhelper.Activities.ExcerciseActivity;
import com.scrivner.healthhelper.R;

public class ProgressActivity extends AppCompatActivity {
    /*
    This class holds the logic for the main progress activity tab. It the progress you have achieved
    since using the app. It also allows you to weigh in and click into progress pictures.
     */
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.progress);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.exercise) {

                    startActivity(new Intent(getApplicationContext(), ExcerciseActivity.class));
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

    public void openSettings(View view) {

        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }

    public void launchWeighInActivity(View view){

        Intent weighInIntent = new Intent(getApplicationContext(), WeighInActivity.class);
        startActivity(weighInIntent);
    }
}