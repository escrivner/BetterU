package com.scrivner.healthhelper;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scrivner.healthhelper.Activities.ExerciseActivity;
import com.scrivner.healthhelper.Activities.ProgressActivity;

import java.util.Calendar;

public class Methods {

    Storage storage = new Storage();
    BottomNavigationView bottomNavigationView;
    public final int CALORIES = 0;
    public final int EXERCISE = 1;
    public final int PROGRESS = 2;

    public boolean checkForNewDay(Context context) {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int lastYear = 0;
        int lastDay = 0;
        try {
            lastYear = storage.loadIntFile(storage.YEAR, context);
        } catch (Exception e) {
            e.printStackTrace();
            storage.saveFile(currentYear, storage.YEAR, context);
        }

        try {
            lastDay = storage.loadIntFile(storage.DAY, context);

        } catch (Exception e) {
            e.printStackTrace();
            storage.saveFile(currentDay, storage.DAY, context);
        }

        storage.saveFile(currentYear, storage.YEAR, context.getApplicationContext());
        storage.saveFile(currentDay, storage.DAY, context.getApplicationContext());

        if (currentDay > lastDay || currentYear > lastYear) {

            return true;
        } else {

            return false;
        }
    }

    public void resetNewDay(Context context){

        if(checkForNewDay(context)) {

            int currentCalories = storage.loadIntFile(storage.CURRENT_CAL, context);
            int lifetimeDeficit = storage.loadIntFile(storage.TOTAL_DEFICIT, context);
            int limitCalories = storage.loadIntFile(storage.LIMIT, context);
            int deficit = limitCalories - currentCalories;
            int calStreak = storage.loadIntFile(storage.STREAK_CAL, context);
            int currentBurned = storage.loadIntFile(storage.BURNED_CAL, context);
            int lifetimeBurned = storage.loadIntFile(storage.TOTAL_BURNED, context);
            int burnedStreak = storage.loadIntFile(storage.EXERCISE_STREAK, context);

            if (currentCalories > 0 && currentCalories < limitCalories) {

                calStreak++;
                lifetimeDeficit += deficit;
            } else {

                calStreak = 0;
            }

            if (currentBurned > 0) {

                burnedStreak++;
                lifetimeBurned += currentBurned;
            } else {

                burnedStreak = 0;
            }

            storage.saveFile(lifetimeDeficit, storage.TOTAL_DEFICIT, context);
            storage.saveFile(lifetimeBurned, storage.TOTAL_BURNED, context);
            storage.saveFile(calStreak, storage.STREAK_CAL, context);
            storage.saveFile(burnedStreak, storage.EXERCISE_STREAK, context);
            storage.saveFile(0, storage.BURNED_CAL, context);
            storage.saveFile(0, storage.CURRENT_CAL, context);

        }
    }


}
