package com.scrivner.healthhelper.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.scrivner.healthhelper.Methods;
import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;

import java.io.File;
import java.io.IOException;

public class WeighInActivity extends AppCompatActivity {

    Storage storage = new Storage();
    Methods methods = new Methods();
    ImageView imageView;
    EditText newWeightEdit;
    Button takePictureButton;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weigh_in);

        imageView = findViewById(R.id.weighInImage);
        newWeightEdit = findViewById(R.id.editWeighIn);
        takePictureButton = findViewById(R.id.takePictureButton);
        confirmButton = findViewById(R.id.confirmButton);



    }

    private void setupPictureButton(){

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA}, 100);
        };


        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                if(cameraIntent.resolveActivity(getPackageManager()) != null){

                    File photoFile = null;
                    try {

                        photoFile = storage.createImageFile(getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(photoFile != null){
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.android.fileprovider", photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, 100);
                    }
                }
            }
        });
    }

    private void saveWeighIn(){

        int currentWeightInInputs = storage.loadIntFile(storage.WEIGH_IN_INPUTS, getApplicationContext());
        int currentWeight = storage.loadIntFile(storage.WEIGH_IN_INPUTS, getApplicationContext());
        int newWeight;
        boolean weighInIsANumber;

        try{

            newWeight = Integer.parseInt(newWeightEdit.getText().toString());
            weighInIsANumber = true;
        } catch (Exception e){

            weighInIsANumber = false;
        }

        if(weighInIsANumber){

            currentWeightInInputs++;
            String weightInEntryFile = "edit_weigh_in_input_" + currentWeightInInputs + ".txt";
            String weightInTimeFile = "edit_weight_in_time_" + currentWeightInInputs + ".txt";
            String timeString = methods.getTimeString();



        }
    }

    private void setupConfirmButton(){

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){

            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
            imageView.setRotation(90);


        }
    }
}


