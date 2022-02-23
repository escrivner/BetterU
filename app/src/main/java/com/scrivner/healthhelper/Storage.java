package com.scrivner.healthhelper;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Storage {
    /*
    This is the class that stores all of the functions used to store permanent information.
     */

    //calories activity
    public final String CURRENT_CAL = "current_calories_health.txt";
    public final String LIMIT = "limit_calories_health.txt";
    public final String STREAK_CAL = "cal_streak_health.txt";
    public final String TOTAL_DEFICIT = "total_deficit_health.txt";
    public final String IS_FIRST_TIME_APP_HAS_OPENED = "is_first_time_app_has_opened_health.txt";
    public final String IS_TIMER_RUNNING = "is_timer_running_health.txt";
    public final String TIMER_STATE = "timer_state_health.txt";

    //exercise activity
    public final String BURNED_CAL = "burned_health.txt";
    public final String EXERCISE_STREAK = "exercise_streak_health.txt";
    public final String TOTAL_BURNED = "total_cal_burned_health.txt";


    //tracking time
    public final String YEAR = "year_health.txt";
    public final String DAY = "day_health.txt";
    public final String HOUR = "hour_health.txt";

    //progress
    public final String STARTING_WEIGHT = "starting_weight_health.txt";

    //settings
    public final String CURRENT_WEIGHT = "current_weight_health.txt";
    public final String HEIGHT = "height_health.txt";
    public final String GENDER = "gender_health.txt";
    public final String AGE = "age_health.txt";
    public final String MEASURE = "measure_system_health.txt";
    public final String BASAL = "basal_rate_health.txt";
    public final String ACTIVITY_FACTOR = "activity_factor_health.txt";
    public final String DEFICIT = "deficit_health.txt";
    public final String START_FAST_TIME = "start_fast_time_health.txt";
    public final String END_FAST_TIME = "end_fast_time_health.txt";

    //edit calories activity
    public final String TOTAL_CALORIES_INPUTS = "total_calories_input_health.txt";

    //weight in
    public final String WEIGH_IN_INPUTS = "weigh_in_inputs_health.txt";


    public void saveFile(int input, String file, Context context) {
        /*
        This function takes an integer input and a file name and saves
        that input into a textfile.
         */

        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = context.openFileOutput(file, MODE_PRIVATE);
            fileOutputStream.write(Integer.toString(input).getBytes());
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int loadIntFile(String file, Context context) {
        /*
        This function accepts a file name and tries to load the information from that file.
        If the file exists it will parse the string into a int value. If it doesn't
        exist, then it will create and store a 0 into the file.
         */

        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {

            fileInputStream = context.openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String contents;

            while ((contents = bufferedReader.readLine()) != null) {

                stringBuilder.append(contents);
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            if (fileInputStream != null) {

                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String string = stringBuilder.toString();

        if (string.equals("")) {

            saveFile(0, file, context);
            string = "0";
        }

        return Integer.parseInt(string);
    }

    public void saveStringFile(String input, String file, Context context) {
        /*
        This function just saves a string into a file.
         */
        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = context.openFileOutput(file, MODE_PRIVATE);
            fileOutputStream.write(input.getBytes());
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String loadStringFile(String file, Context context) {
        /*
        Is basically the same function as loadIntFile, except it doesn't parse the value
        at the end.
         */
        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {

            fileInputStream = context.openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String contents;

            while ((contents = bufferedReader.readLine()) != null) {

                stringBuilder.append(contents);
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            if (fileInputStream != null) {

                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String string = stringBuilder.toString();

        if (string.equals("")) {

            saveFile(0, file, context);
            string = "0";
        }

        return string;
    }

    public File createImageFile(Context context) throws IOException {
        /*
        Creates a new file name everytime the user takes a picture in the weigh in activity. It increments
        the amount of weigh ins that have occured, then stores the image file name in that position.
         */

        int weighInPos = loadIntFile(WEIGH_IN_INPUTS, context) + 1;
        String imageFileName = "JPEG_weigh_in_image_" + weighInPos;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        saveFile(weighInPos, WEIGH_IN_INPUTS, context);
        return image;
    }

    public void resetStorage(Context context){
        /*
        Resets all files the app uses back to zero. Golden Experience Requiem.
         */

        saveFile(0, CURRENT_CAL, context);
        saveFile(0, LIMIT, context);
        saveFile(0, STREAK_CAL, context);
        saveFile(0, TOTAL_DEFICIT, context);
        saveFile(0, BURNED_CAL, context);
        saveFile(0, CURRENT_WEIGHT, context);
        saveFile(0, EXERCISE_STREAK, context);
        saveFile(0, CURRENT_WEIGHT, context);
        saveFile(0, CURRENT_WEIGHT, context);
        saveFile(0, START_FAST_TIME, context);
        saveFile(0, END_FAST_TIME, context);
        saveFile(0, HEIGHT, context);
        saveFile(0, DEFICIT, context);
        saveFile(0, AGE, context);
        saveFile(0, IS_FIRST_TIME_APP_HAS_OPENED, context);
        saveFile(0, IS_TIMER_RUNNING, context);
        saveStringFile("", ACTIVITY_FACTOR, context);
        int currentCalInputs = loadIntFile(TOTAL_CALORIES_INPUTS, context);

        for(int i = 0; i < currentCalInputs; i++){

            String calEntryFile = "edit_calories_input_" + i + ".txt";
            String calTimeFile = "edit_calories_time_" + i + ".txt";
            saveFile(0, calEntryFile, context);
            saveStringFile(null, calTimeFile, context);
        }

        saveFile(0, TOTAL_CALORIES_INPUTS, context);
        //caloriesActivity.progressBar.setProgress(0);
    }
}
