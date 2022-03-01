package com.scrivner.healthhelper;

import android.content.Context;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scrivner.healthhelper.Activities.EntryObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Methods {

    Storage storage = new Storage();
    BottomNavigationView bottomNavigationView;
    public final int CALORIES = 0;
    public final int EXERCISE = 1;
    public final int WEIGH_IN = 2;

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
            int calTotalInputs = storage.loadIntFile(storage.TOTAL_CALORIES_INPUTS, context);
            int exerciseTotalInputs = 0;
            int weightInTotalInputs = storage.loadIntFile(storage.WEIGH_IN_INPUTS, context);

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

            for(int i = 0; i < calTotalInputs; i++){

                String calEntryFile = "edit_calories_input_" + i + ".txt";
                String calTimeFile = "edit_calories_time_" + i + ".txt";
                storage.saveFile(0, calEntryFile, context);
                storage.saveStringFile("", calTimeFile, context);
            }

            storage.saveFile(lifetimeDeficit, storage.TOTAL_DEFICIT, context);
            storage.saveFile(lifetimeBurned, storage.TOTAL_BURNED, context);
            storage.saveFile(calStreak, storage.STREAK_CAL, context);
            storage.saveFile(burnedStreak, storage.EXERCISE_STREAK, context);
            storage.saveFile(0, storage.BURNED_CAL, context);
            storage.saveFile(0, storage.CURRENT_CAL, context);
            storage.saveFile(0, storage.TOTAL_CALORIES_INPUTS, context);

        }
    }

    public String getTimeString(){

        int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
        int currentHour = Calendar.getInstance().get(Calendar.HOUR);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        int currentSecond = Calendar.getInstance().get(Calendar.SECOND);
        String timeString = currentHour + ":" + currentMinute + ":" + currentSecond + " ";

        if (am_pm == 0) {

            timeString += "AM";
        } else {

            timeString += "PM";
        }
        return timeString;
    }

    public ArrayList<EntryObject> buildArray(int activity, Context context){

        ArrayList<EntryObject> array = new ArrayList<>();
        int totalInputs = 0;
        String entryFile = "";
        String timeFile = "";

        if(activity == CALORIES){

            totalInputs = storage.loadIntFile(storage.TOTAL_CALORIES_INPUTS, context);
            entryFile = "edit_calories_input_";
            timeFile = "edit_calories_time_";
        } else if(activity == EXERCISE){


        } else if(activity == WEIGH_IN){

            totalInputs = storage.loadIntFile(storage.WEIGH_IN_INPUTS, context);
            entryFile = "edit_weigh_in_input_";
            timeFile = "edit_weigh_in_time_";
        }

        for(int i = 0; i < totalInputs; i++){
            int entry = storage.loadIntFile((entryFile + i + ".txt"), context);
            String time = storage.loadStringFile((timeFile + i + ".txt"), context);
            EntryObject object = new EntryObject(entry, time);
            array.add(object);
            Log.d("WeighIn", "loop position: " + entry);
        }



        return array;

    }

    public void cleanInputFiles(int position, int activity, Context context){

        int totalPositions = 0;
        String entryFile = "";
        String timeFile = "";
        String positionsFile = "";

        if(activity == CALORIES){

            totalPositions = storage.loadIntFile(storage.TOTAL_CALORIES_INPUTS, context);
            entryFile = "edit_calories_input_";
            timeFile = "edit_calories_time_";
            positionsFile = storage.TOTAL_CALORIES_INPUTS;
        } else if(activity == EXERCISE){

        } else if(activity == WEIGH_IN){
            totalPositions = storage.loadIntFile(storage.WEIGH_IN_INPUTS, context);
            entryFile = "edit_weigh_in_input_";
            timeFile = "edit_weigh_in_time_";
            positionsFile = storage.WEIGH_IN_INPUTS;

        }

        if(totalPositions > 1) {
            for (int i = position ; i < totalPositions - 1; i++) {

                String currentFile = entryFile + i + ".txt";
                String nextFile = entryFile + (i + 1) + ".txt";
                String currentTimeFile = timeFile + i + ".txt";
                String nextTimeFile = timeFile + (i + 1) + ".txt";

                int nextInput = storage.loadIntFile(nextFile, context);
                String nextTime = storage.loadStringFile(nextTimeFile, context);
                storage.saveFile(nextInput, currentFile, context);
                storage.saveStringFile(nextTime, currentTimeFile, context);
            }
        }




        if(totalPositions >= 1) {

            storage.saveFile((totalPositions - 1), positionsFile, context);
        }else {
            storage.saveStringFile("No inputs recorded", timeFile + 0 + "txt", context);
        }
    }
}
