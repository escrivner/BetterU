package com.scrivner.healthhelper.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.scrivner.healthhelper.Methods;
import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;

public class SettingsActivity extends AppCompatActivity {

    Storage storage = new Storage();
    Methods methods = new Methods();

    EditText editWeight;
    EditText editHeight;
    EditText editDeficit;
    EditText editAge;
    EditText editFastTime;
    EditText editEatTime;
    RadioGroup radioGroup;
    RadioGroup activityGroup;
    RadioButton sedentaryButton;
    RadioButton lightActivityButton;
    RadioButton moderateActivityButton;
    RadioButton veryActiveButton;
    RadioButton extraActive;
    RadioButton maleButton;
    RadioButton femaleButton;

    final int MALE = 0;
    final int FEMALE = 1;
    int gender = 0;
    int weight = 0;
    int height = 0;
    int age = 0;
    int fastTime = 0;
    int eatTime = 0;
    int deficit = 0;
    int BMR;
    double activityFactor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        editDeficit = findViewById(R.id.editDeficit);
        editAge = findViewById(R.id.editAge);
        editFastTime = findViewById(R.id.editFastTime);
        editEatTime = findViewById(R.id.editEatTime);
        radioGroup = findViewById(R.id.radioGroup);
        maleButton = findViewById(R.id.male_button);
        femaleButton = findViewById(R.id.female_button);
        activityGroup = findViewById(R.id.activityGroup);
        sedentaryButton = findViewById(R.id.SedentaryButton);
        lightActivityButton = findViewById(R.id.LightActivityButton);
        moderateActivityButton = findViewById(R.id.ModerateActivityButton);
        veryActiveButton = findViewById(R.id.VeryActiveButton);
        extraActive = findViewById(R.id.ExtraActiveButton);

        displayInfo();
    }

    public int calculateDailyLimit(Context context) {

        weight = storage.loadIntFile(storage.CURRENT_WEIGHT, context);
        height = storage.loadIntFile(storage.HEIGHT, context);
        deficit = storage.loadIntFile(storage.DEFICIT, context);
        age = storage.loadIntFile(storage.AGE, context);
        gender = storage.loadIntFile(storage.GENDER, context);
        activityFactor = Double.parseDouble(storage.loadStringFile(storage.ACTIVITY_FACTOR, context));
        BMR = 0;

        if (gender == MALE) {

            BMR = (int) ((4.536 * weight) + (15.88 * height) - (5 * age) + 5);
        } else {

            BMR = (int) ((4.536 * weight) + (15.88 * height) - (5 * age) - 161);
        }

        BMR *= activityFactor;
        return BMR - deficit;
    }

    public void displayInfo() {

        weight = storage.loadIntFile(storage.CURRENT_WEIGHT, getApplicationContext());
        height = storage.loadIntFile(storage.HEIGHT, getApplicationContext());
        deficit = storage.loadIntFile(storage.DEFICIT, getApplicationContext());
        age = storage.loadIntFile(storage.AGE, getApplicationContext());
        gender = storage.loadIntFile(storage.GENDER, getApplicationContext());
        fastTime = storage.loadIntFile(storage.FAST_TIME, getApplicationContext());
        eatTime = storage.loadIntFile(storage.EAT_TIME, getApplicationContext());
        activityFactor = Double.parseDouble(storage.loadStringFile(storage.ACTIVITY_FACTOR, getApplicationContext()));

        editWeight.setText(Integer.toString(weight));
        editHeight.setText(Integer.toString(height));
        editDeficit.setText(Integer.toString(deficit));
        editAge.setText(Integer.toString(age));
        editFastTime.setText(Integer.toString(fastTime));
        editEatTime.setText(Integer.toString(eatTime));

        if (gender == MALE) {

            maleButton.setChecked(true);
        } else {

            femaleButton.setChecked(true);
        }

        if(activityFactor == 1.2){

            sedentaryButton.setChecked(true);
        } else if(activityFactor == 1.375){

            lightActivityButton.setChecked(true);
        } else if(activityFactor == 1.55){

            moderateActivityButton.setChecked(true);
        } else if(activityFactor == 1.725){

            veryActiveButton.setChecked(true);
        } else if(activityFactor == 1.9){

            extraActive.setChecked(true);
        }
    }

    public void reset(View view) {

        storage.resetStorage(getApplicationContext());
        Toast.makeText(getApplicationContext(), "All stats and settings reset.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void confirmClick(View view) {

        weight = Integer.parseInt(String.valueOf(editWeight.getText()));
        height = Integer.parseInt(String.valueOf(editHeight.getText()));
        deficit = Integer.parseInt(String.valueOf(editDeficit.getText()));
        age = Integer.parseInt(String.valueOf(editAge.getText()));
        fastTime = Integer.parseInt(String.valueOf(editFastTime.getText()));
        eatTime = Integer.parseInt(String.valueOf(editEatTime.getText()));


        if (radioGroup.getCheckedRadioButtonId() == R.id.male_button) {

            gender = MALE;
        } else {

            gender = FEMALE;
        }

        if(activityGroup.getCheckedRadioButtonId() == R.id.SedentaryButton){

            activityFactor = 1.2;
        } else if(activityGroup.getCheckedRadioButtonId() == R.id.LightActivityButton){

            activityFactor = 1.375;
        } else if(activityGroup.getCheckedRadioButtonId() == R.id.ModerateActivityButton){

            activityFactor = 1.55;
        } else if(activityGroup.getCheckedRadioButtonId() == R.id.VeryActiveButton){

            activityFactor = 1.725;
        } else if(activityGroup.getCheckedRadioButtonId() == R.id.ExtraActiveButton){

            activityFactor = 1.9;
        }

        storage.saveFile(weight, storage.CURRENT_WEIGHT, getApplicationContext());
        storage.saveFile(height, storage.HEIGHT, getApplicationContext());
        storage.saveFile(deficit, storage.DEFICIT, getApplicationContext());
        storage.saveFile(age, storage.AGE, getApplicationContext());
        storage.saveFile(fastTime, storage.FAST_TIME, getApplicationContext());
        storage.saveFile(eatTime, storage.EAT_TIME, getApplicationContext());
        storage.saveFile(gender, storage.GENDER, getApplicationContext());
        storage.saveStringFile(Double.toString(activityFactor), storage.ACTIVITY_FACTOR, getApplicationContext());
        storage.saveFile(calculateDailyLimit(getApplicationContext()), storage.LIMIT, getApplicationContext());

        Toast.makeText(getApplicationContext(), "Settings confirmed.", Toast.LENGTH_SHORT).show();
        finish();
    }




}