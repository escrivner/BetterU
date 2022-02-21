package com.scrivner.healthhelper;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

public class Timer {
    /*
    TODO: The entire timer class needs to be rewritten because this code is total shit and broken.
     */
    Storage storage = new Storage();
    public CountDownTimer countDownTimer;



    public void startTimer(Context context, TextView textView) {
        long currentTimeMillis = Calendar.getInstance().get(Calendar.MILLISECOND);
        long startTimeHours = (long) storage.loadIntFile(storage.START_FAST_TIME, context);
        long endTimeHours = (long) storage.loadIntFile(storage.END_FAST_TIME, context);
        long startTimeMillis = startTimeHours * 3600000;
        long endTimeMillis = endTimeHours * 3600000;
        long timerDuration;
        int isTimerRunning = storage.loadIntFile(storage.IS_TIMER_RUNNING, context);

        if(currentTimeMillis < startTimeMillis){

            storage.saveStringFile("FASTING", storage.TIMER_STATE, context);
            timerDuration = startTimeMillis - currentTimeMillis;
        } else if(currentTimeMillis > endTimeMillis){

            storage.saveStringFile("FASTING", storage.TIMER_STATE, context);
            timerDuration = (86400000 - currentTimeMillis) + startTimeMillis;
        } else{

            storage.saveStringFile("EATING", storage.TIMER_STATE, context);
            timerDuration = endTimeMillis - currentTimeMillis;
        }

        if(isTimerRunning != 0) {

            storage.saveFile(1, storage.IS_TIMER_RUNNING, context);

            countDownTimer = new CountDownTimer(timerDuration, 1000) {
                @Override
                public void onTick(long remainingTime) {

                    textView.setText(remainingTime + " ");
                    Log.d("Timer", String.valueOf(remainingTime));
                }

                @Override
                public void onFinish() {

                    storage.saveFile(0, storage.IS_TIMER_RUNNING, context);
                    startTimer(context, textView);
                }
            };
        }


    }
}
