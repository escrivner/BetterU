package com.scrivner.healthhelper;

import android.content.Context;
import android.os.CountDownTimer;

import com.scrivner.healthhelper.Activities.CaloriesActivity;

public class Timer {
    /*
    TODO: The entire timer class needs to be rewritten because this code is total shit and broken.
     */
    Storage storage = new Storage();

    int fastTimeHours;
    int fastTimeMillis;
    int eatTimeHours;
    int eatTimeMillis;
    long millisLeft;
    int fastState = 0;
    boolean isRunning = false;

    final int FAST = 0;
    final int EAT = 1;
    CountDownTimer countDownTimer;

    public void initializeVariables(Context context) {

        fastTimeHours = storage.loadIntFile(storage.FAST_TIME, context);
        fastTimeMillis = fastTimeHours * 3600000;
        eatTimeHours = storage.loadIntFile(storage.EAT_TIME, context);
        eatTimeMillis = eatTimeHours * 3600000;

        if (fastState == FAST) {

            millisLeft = fastTimeMillis;
        } else {

            millisLeft = eatTimeMillis;
        }

    }

    public void startTimer(Context context) {

        initializeVariables(context);
        countDownTimer = new CountDownTimer(millisLeft, 1000) {
            @Override
            public void onTick(long millisLeftUntilFinished) {

                //fastView.setText(Long.toString(millisLeftUntilFinished));
            }

            @Override
            public void onFinish() {

                if (fastState == FAST) {

                    fastState = EAT;
                    initializeVariables(context);
                } else {

                    fastState = FAST;
                    initializeVariables(context);
                }

            }
        }.start();
    }
}
