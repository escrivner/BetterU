package com.scrivner.healthhelper.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scrivner.healthhelper.Methods;
import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;
import com.scrivner.healthhelper.Timer;

import java.util.Calendar;

public class CaloriesActivity extends AppCompatActivity {
    /*
     This class hold all of the logic that runs the calories tracking activity.
     */

    //Declaring all of the UI objects and variables.
    Storage storage = new Storage();
    Methods methods = new Methods();

    SettingsActivity settings = new SettingsActivity();
    BottomNavigationView bottomNavigationView;
    public ProgressBar progressBar;
    public TextView fastView;
    public TextView currentView, limitView, lifetimeView, streakView;
    NumberPicker onesPicker, tensPicker, hundredsPicker, thousandsPicker;
    EditText popupEditWeight, popupEditHeight, popupTargetDeficit, popupEditAge;
    RadioGroup popupGenderGroup, popupActivityGroup;
    RadioButton popupMale, popupFemale, popupSedentary, popupLight, popupModerate, popupVeryActive, popupExtraActive;
    Button confirmButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    int currentCalories = 0;
    int limitCalories = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        This method runs when the activity is first started. It initializes all of the UI elements
        in the activity, resets all of the information if there is a new day, and stores the code for
        the botton navigation bar.
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.calorieBar);
        progressBar.setMax(limitCalories);
        currentView = findViewById(R.id.txt_current);
        limitView = findViewById(R.id.txt_limit);
        lifetimeView = findViewById(R.id.txt_deficit);
        streakView = findViewById(R.id.txt_cal_streak);
        fastView = findViewById(R.id.txt_fast);
        onesPicker = findViewById(R.id.onesNumPicker);
        tensPicker = findViewById(R.id.tensNumPicker);
        hundredsPicker = findViewById(R.id.hundNumPicker);
        thousandsPicker = findViewById(R.id.thousNumPicker);
        onesPicker.setMinValue(0);
        tensPicker.setMinValue(0);
        hundredsPicker.setMinValue(0);
        thousandsPicker.setMinValue(0);
        onesPicker.setMaxValue(9);
        tensPicker.setMaxValue(9);
        hundredsPicker.setMaxValue(9);
        thousandsPicker.setMaxValue(9);

        if (methods.checkForNewDay(getApplicationContext())) {

            currentCalories = storage.loadIntFile(storage.CURRENT_CAL, getApplicationContext());
            limitCalories = limitCalories;
            int lifetimeDeficit = storage.loadIntFile(storage.TOTAL_DEFICIT, getApplicationContext());
            int deficit = limitCalories - currentCalories;
            int streak = storage.loadIntFile(storage.STREAK_CAL, getApplicationContext());

            if (currentCalories > 0 && currentCalories < limitCalories) {

                streak++;
            }

            lifetimeDeficit += deficit;
            storage.saveFile(lifetimeDeficit, storage.TOTAL_DEFICIT, getApplicationContext());
            storage.saveFile(0, storage.BURNED_CAL, getApplicationContext());
            storage.saveFile(0, storage.CURRENT_CAL, getApplicationContext());
            storage.saveFile(streak, storage.STREAK_CAL, getApplicationContext());
            progressBar.setProgress(0);
        }

        displayCalories();
        displayPopUpMenu();
        displayTimer();

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.calories);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.exercise) {

                    startActivity(new Intent(getApplicationContext(), ExcerciseActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.progress) {

                    startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.calories) {

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
        displayCalories();
    }

    public void addCalories(View view) {
        /*
        Gets the numbers from the number wheel and adds it to the value
        of current calories.
         */

        int ones = onesPicker.getValue();
        int tens = tensPicker.getValue() * 10;
        int hundreds = hundredsPicker.getValue() * 100;
        int thousands = thousandsPicker.getValue() * 1000;
        int addedCalories = ones + tens + hundreds + thousands;

        currentCalories += addedCalories;
        currentView.setText(Integer.toString(currentCalories));
        progressBar.setProgress(currentCalories);
        onesPicker.setValue(0);
        tensPicker.setValue(0);
        hundredsPicker.setValue(0);
        thousandsPicker.setValue(0);

        storage.saveFile(currentCalories, storage.CURRENT_CAL, this.getApplicationContext());
        addCalorieEntry(addedCalories);
        displayCalories();
    }

    public void displayCalories() {
        /*
        Loads the values of current calories and the limit and displays that information
        to the UI elements. This is bad code and there is definitely a better way of doing this.
         */

        try {
            currentCalories = storage.loadIntFile(storage.CURRENT_CAL, getApplicationContext());
        } catch (Exception e) {

            e.printStackTrace();
            storage.saveFile(0, storage.CURRENT_CAL, getApplicationContext());
            currentCalories = storage.loadIntFile(storage.CURRENT_CAL, getApplicationContext());
        }

        try {
            limitCalories = storage.loadIntFile(storage.LIMIT, getApplicationContext());
        } catch (Exception e) {

            e.printStackTrace();
            storage.saveFile(2500, storage.LIMIT, getApplicationContext());
            limitCalories = storage.loadIntFile(storage.LIMIT, getApplicationContext());
        }

        int totalDeficit = storage.loadIntFile(storage.TOTAL_DEFICIT, getApplicationContext());
        int streak = storage.loadIntFile(storage.STREAK_CAL, getApplicationContext());


        streakView.setText("Current Streak: " + streak);
        lifetimeView.setText("Lifetime Deficit: " + totalDeficit);
        currentView.setText(Integer.toString(currentCalories));
        limitView.setText(Integer.toString(limitCalories));
        progressBar.setProgress(currentCalories);
        progressBar.setMax(limitCalories);
    }

    public void displayEditActivity(View view) {
        /*
        Creates and launches the Edit Calories Activity and finishes the current
        instance of the Calories Activity, so that when the user exits the edit calories activity,
        the Calorie Activity can be restarted and will have updated information.
         */

        startActivity(new Intent(getApplicationContext(), EditCaloriesActivity.class));
    }

    public void addCalorieEntry(int entry) {
        /*
        Saves the current time and amount of calories entered into storage, so that the Edit Calories Activity
        can display it. It also formats the time so that it is easily readable by the user.
         */

        int currentCalPos = storage.loadIntFile(storage.TOTAL_CALORIES_INPUTS, getApplicationContext()) + 1;
        String calEntryFile = "edit_calories_input_" + currentCalPos + ".txt";
        String calTimeFile = "edit_calories_time_" + currentCalPos + ".txt";
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

        storage.saveFile(currentCalPos, storage.TOTAL_CALORIES_INPUTS, getApplicationContext());
        storage.saveFile(entry, calEntryFile, getApplicationContext());
        storage.saveStringFile(timeString, calTimeFile, getApplicationContext());

    }

    public void displayPopUpMenu(){
        /*
        This method checks to see if this is the first time the app has been opened. If it is, then it displays a popup menu and prompts
        the user to enter their information. The method then retrieves the information and stores it in storage and updates the UI.
        It also does some error checking to make sure that the user input information and only input numbers, to prevent
        the app from crashing.
         */

        int openState = storage.loadIntFile(storage.IS_FIRST_TIME_APP_HAS_OPENED, getApplicationContext());

        if(openState == 0) {

            dialogBuilder = new AlertDialog.Builder(this);
            final View firstTimePopup = getLayoutInflater().inflate(R.layout.startup_popup, null);
            dialogBuilder.setView(firstTimePopup);
            dialog = dialogBuilder.create();
            dialog.show();

            popupEditWeight = dialog.findViewById(R.id.editPopupWeight);
            popupEditHeight = dialog.findViewById(R.id.editPopupHeight);
            popupTargetDeficit = dialog.findViewById(R.id.editPopupDeficit);
            popupEditAge = dialog.findViewById(R.id.editPopupAge);
            popupGenderGroup = dialog.findViewById(R.id.popupGenderGroup);
            popupActivityGroup = dialog.findViewById(R.id.popupActivityGroup);
            popupMale = dialog.findViewById(R.id.popupMaleButton);
            popupFemale = dialog.findViewById(R.id.popupFemaleButton);
            popupSedentary = dialog.findViewById(R.id.popupSedentary);
            popupLight = dialog.findViewById(R.id.popupLightActivity);
            popupModerate = dialog.findViewById(R.id.popupModerate);
            popupVeryActive = dialog.findViewById(R.id.popupVeryActive);
            popupExtraActive = dialog.findViewById(R.id.popupExtraActive);
            confirmButton = dialog.findViewById(R.id.popupConfirm);


            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    boolean canReset = true;
                    String stringWeight = popupEditWeight.getText().toString();
                    String stringHeight = popupEditHeight.getText().toString();
                    String stringDeficit = popupTargetDeficit.getText().toString();
                    String stringAge = popupEditAge.getText().toString();
                    int weight;
                    int height;
                    int age;
                    int deficit;

                    if(stringWeight.equals("") || stringWeight.equals("0")){

                        canReset = false;
                        Toast.makeText(getApplicationContext(), "Please enter your weight.", Toast.LENGTH_SHORT).show();
                    } else if(stringHeight.equals("") || stringHeight.equals("0")){

                        canReset = false;
                        Toast.makeText(getApplicationContext(), "Please enter your height.", Toast.LENGTH_SHORT).show();
                    } else if(stringDeficit.equals("")){

                        canReset = false;
                        Toast.makeText(getApplicationContext(), "Please enter your daily deficit.", Toast.LENGTH_SHORT).show();
                    } else if(stringAge.equals("") || stringAge.equals("0")){

                        canReset = false;
                        Toast.makeText(getApplicationContext(), "Please enter your age.", Toast.LENGTH_SHORT).show();
                    } else if(popupGenderGroup.getCheckedRadioButtonId() == -1){

                        canReset = false;
                        Toast.makeText(getApplicationContext(), "Please select your biological gender.", Toast.LENGTH_SHORT).show();
                    } else if(popupActivityGroup.getCheckedRadioButtonId() == -1){

                        canReset = false;
                        Toast.makeText(getApplicationContext(), "Please select your activity level.", Toast.LENGTH_SHORT).show();
                    }

                    try{

                        weight = Integer.parseInt(stringWeight);
                        height = Integer.parseInt(stringHeight);
                        deficit = Integer.parseInt(stringDeficit);
                        age = Integer.parseInt(stringAge);
                    } catch(Exception e){

                        canReset = false;
                        Toast.makeText(getApplicationContext(), "Please only enter number values.", Toast.LENGTH_SHORT).show();
                    }

                    if(canReset){

                        weight = Integer.parseInt(stringWeight);
                        height = Integer.parseInt(stringHeight);
                        deficit = Integer.parseInt(stringDeficit);
                        age = Integer.parseInt(stringAge);

                        if(popupGenderGroup.getCheckedRadioButtonId() == R.id.popupMaleButton){

                            storage.saveFile(0, storage.GENDER, dialog.getContext());
                        } else {

                            storage.saveFile(1, storage.GENDER, dialog.getContext());
                        }

                        if(popupActivityGroup.getCheckedRadioButtonId() == R.id.popupSedentary){

                            storage.saveStringFile("1.2", storage.ACTIVITY_FACTOR, dialog.getContext());
                        } else if(popupActivityGroup.getCheckedRadioButtonId() == R.id.popupLightActivity){

                            storage.saveStringFile("1.375", storage.ACTIVITY_FACTOR, dialog.getContext());
                        } else if(popupActivityGroup.getCheckedRadioButtonId() == R.id.popupModerate){

                            storage.saveStringFile("1.55", storage.ACTIVITY_FACTOR, dialog.getContext());
                        } else if(popupActivityGroup.getCheckedRadioButtonId() == R.id.popupVeryActive){

                            storage.saveStringFile("1.725", storage.ACTIVITY_FACTOR, dialog.getContext());
                        } else if(popupActivityGroup.getCheckedRadioButtonId() == R.id.popupExtraActive) {

                            storage.saveStringFile("1.9", storage.ACTIVITY_FACTOR, dialog.getContext());
                        }

                        storage.saveFile(weight, storage.CURRENT_WEIGHT, dialog.getContext());
                        storage.saveFile(height, storage.HEIGHT, dialog.getContext());
                        storage.saveFile(deficit, storage.DEFICIT, dialog.getContext());
                        storage.saveFile(age, storage.AGE, dialog.getContext());
                        storage.saveFile(1, storage.IS_FIRST_TIME_APP_HAS_OPENED, dialog.getContext());

                        int limit = settings.calculateDailyLimit(dialog.getContext());
                        storage.saveFile(limit, storage.LIMIT, dialog.getContext());
                        limitView.setText(Integer.toString(limit));
                        dialog.dismiss();
                    }

                }


            });
        }


    }

    public void displayTimer(){
        /*
        This method creates the fast timer if it has not already been started, and restarts when the timer
        runs out.
         */

        int fastTimeHours = storage.loadIntFile(storage.FAST_TIME, getApplicationContext());
        long fastTimeMillis = fastTimeHours * 3600000;
        int eatTimeHours = storage.loadIntFile(storage.EAT_TIME, getApplicationContext());
        long eatTimeMillis = fastTimeHours * 3600000;
        int isTimerRunning = storage.loadIntFile(storage.IS_TIMER_RUNNING, getApplicationContext());


            storage.saveFile(1, storage.IS_TIMER_RUNNING, getApplicationContext());

            CountDownTimer countDownTimer = new CountDownTimer(fastTimeMillis, 1000) {
                @Override
                public void onTick(long millisLeftUntilFinished) {

                    fastView.setText(Long.toString(millisLeftUntilFinished));
                }

                @Override
                public void onFinish() {

                }
            }.start();

    }
}
