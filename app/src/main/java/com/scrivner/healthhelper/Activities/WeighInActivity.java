package com.scrivner.healthhelper.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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

import com.scrivner.healthhelper.R;
import com.scrivner.healthhelper.Storage;

import java.io.File;

public class WeighInActivity extends AppCompatActivity {

    Storage storage = new Storage();

    EditText editWeight;
    Button takePictureButton;
    ImageView pictureImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weigh_in);

        editWeight = findViewById(R.id.editWeighIn);
        takePictureButton = findViewById(R.id.takePictureButton);
        pictureImage = findViewById(R.id.weighInImage);
    }

    public void confirmWeighIn(View view){

        finish();
    }

    public void launchTakePictureIntent(View view){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            File photoFile = null;

            try{

                photoFile = storage.createImageFile(getApplicationContext());
            } catch (Exception e){

                Toast.makeText(getApplicationContext(), "photo file not created", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.scrivner.healthhelper.Activities", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                displayImage();
            } else {

                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE){

            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            pictureImage.setImageBitmap(captureImage);
        }
    }

    public void displayImage(){

        /*File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        int pos = storage.loadIntFile(storage.WEIGH_IN_INPUTS, getApplicationContext());
        String photoPath = Environment.getExternalStorageDirectory() + "/JPEG_weigh_in_image_" + pos;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        pictureImage.setImageBitmap(bitmap);*/



    }
}