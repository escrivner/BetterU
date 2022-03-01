package com.scrivner.healthhelper.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.scrivner.healthhelper.ListAdapter;
import com.scrivner.healthhelper.Methods;
import com.scrivner.healthhelper.R;

import java.util.ArrayList;

public class EditWeighInActivity extends AppCompatActivity {

    Methods methods = new Methods();
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


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
        finish();
    }
}