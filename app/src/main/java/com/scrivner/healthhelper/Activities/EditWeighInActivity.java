package com.scrivner.healthhelper.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scrivner.healthhelper.ListAdapter;
import com.scrivner.healthhelper.Methods;
import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;

import java.util.ArrayList;

public class EditWeighInActivity extends AppCompatActivity {

    Methods methods = new Methods();
    Storage storage = new Storage();
    ArrayList<EntryObject> array = new ArrayList<>();
    TextView inputsView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weigh_in);

        inputsView = findViewById(R.id.weighInInputsDetected);
        listView = findViewById(R.id.weighInListView);

        array = methods.buildArray(methods.WEIGH_IN, getApplicationContext());

        for(int i = 0; i < array.size(); i++){

            EntryObject object = array.get(i);
            Log.d("WeighIn", "array: " + object.getEntry());
        }

        if(array.size() > 0){

            inputsView.setVisibility(View.INVISIBLE);
        }

        ListAdapter adapter = new ListAdapter(this, com.scrivner.healthhelper.R.layout.adapter_view_layout, array, methods.WEIGH_IN);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (array.size() > 1) {

                    EntryObject selectedObject = array.get(i);
                    EntryObject lastObject = array.get(array.size() - 2);
                    int selectedWeight = selectedObject.getEntry();
                    int currentWeight = storage.loadIntFile(storage.CURRENT_WEIGHT, getApplicationContext());

                    if (i == array.size() - 1) {

                        int lastWeight = lastObject.getEntry();
                        storage.saveFile(lastWeight, storage.CURRENT_WEIGHT, getApplicationContext());
                    }

                    methods.cleanInputFiles(i, methods.WEIGH_IN, getApplicationContext());
                    methods.buildArray(methods.WEIGH_IN, getApplicationContext());
                    ListAdapter adapter = new ListAdapter(getApplicationContext(), R.layout.adapter_view_layout, array, methods.WEIGH_IN);
                    listView.setAdapter(adapter);

                    Intent intent = new Intent(getApplicationContext(), ProgressActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(getApplicationContext(), "You must keep atleast one weight input.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
        finish();
    }
}