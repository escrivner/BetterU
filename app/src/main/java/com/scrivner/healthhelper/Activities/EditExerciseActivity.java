package com.scrivner.healthhelper.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.scrivner.healthhelper.R;

public class EditExerciseActivity extends AppCompatActivity {

    /*
    This class is basically the same thing as the edit calories activity but just for exercise entries.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_excercise);
    }
}