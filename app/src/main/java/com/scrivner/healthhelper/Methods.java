package com.scrivner.healthhelper;

import android.content.Context;
import android.view.View;

import java.util.Calendar;

public class Methods {

    Storage storage = new Storage();

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


}
