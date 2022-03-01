package com.scrivner.healthhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scrivner.healthhelper.Activities.EntryObject;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<EntryObject> {

    Storage storage = new Storage();
    private Context mContext;
    int mResource;
    int mType;
    public ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<EntryObject> objects, int objectType) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mType = objectType;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent){

        String entryFile = "";
        String timeFile = "";

            if(mType == 0){

                entryFile = "edit_calories_input_" + pos + ".txt";
                timeFile = "edit_calories_time_" + pos + ".txt";
            } else if(mType == 1){

                entryFile = "edit_exercise_input_" + pos + ".txt";
                timeFile = "edit_exercise_time_" + pos + ".txt";
            } else if(mType == 2){

                entryFile = "edit_weigh_in_input_" + pos + ".txt";
                timeFile = "edit_weigh_in_time_" + pos + ".txt";
            }


            int entry = storage.loadIntFile(entryFile, mContext);
            String time = storage.loadStringFile(timeFile, mContext);

            EntryObject calEntry = new EntryObject(entry, time);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView timeView = convertView.findViewById(R.id.timeView);
            TextView calView = convertView.findViewById(R.id.caloriesView);

            timeView.setText(time);
            calView.setText(Integer.toString(entry) + " cal");

            return convertView;


    }
}
