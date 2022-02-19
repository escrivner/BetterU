package com.scrivner.healthhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scrivner.healthhelper.Activities.CalObject;

import java.util.ArrayList;
import java.util.List;

public class CalListAdapter extends ArrayAdapter<CalObject> {

    Storage storage = new Storage();
    private Context mContext;
    int mResource;
    public CalListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CalObject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent){


            int i = pos;
            String calEntryFile = "edit_calories_input_" + i + ".txt";
            String calTimeFile = "edit_calories_time_" + i + ".txt";
            int entry = storage.loadIntFile(calEntryFile, mContext);
            String time = storage.loadStringFile(calTimeFile, mContext);

            CalObject calEntry = new CalObject(entry, time);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView timeView = convertView.findViewById(R.id.timeView);
            TextView calView = convertView.findViewById(R.id.caloriesView);

            timeView.setText(time);
            calView.setText(Integer.toString(entry) + " cal");

            return convertView;


    }
}
