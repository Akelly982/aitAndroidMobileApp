package com.aidankelly.projectmanager.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aidankelly.projectmanager.R;
import com.google.android.material.snackbar.Snackbar;

import static com.aidankelly.projectmanager.entities.Constants.NEW_PROJECT_FETCH_IMAGE_CODE;

public class newProjectActivity extends AppCompatActivity {

    Button exitButton;
    Button importImageButton;
    Button createProjectButton;
    EditText projectNameInputEditText;
    ImageView imagePreviewImageView;
    View rootView;

    Uri imageFilePath;
    Bitmap imageToStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        projectNameInputEditText = findViewById(R.id.projectNameInputEditText);
        createProjectButton = findViewById(R.id.createProjectButton);
        importImageButton = findViewById(R.id.importImgButton);
        imagePreviewImageView = findViewById(R.id.imagePreviewImageView);
        exitButton = findViewById(R.id.exitButton);
        rootView = findViewById(R.id.content);




        // get the image from the phone
        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageObject = new Intent();
                getImageObject.setType("image/*");

                getImageObject.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(getImageObject,NEW_PROJECT_FETCH_IMAGE_CODE);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PROJECT_FETCH_IMAGE_CODE){
            if (resultCode == RESULT_OK){
                setImageToPreview(data);

                imagePreviewImageView.setImageBitmap(imageToStore);
            }
        }
    }

    private void setImageToPreview(Intent data) {
        try {
            imageFilePath = data.getData();
            imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
        }
        catch (Exception e){
            Snackbar.make(rootView, e.toString() , Snackbar.LENGTH_LONG).show();   // send a small alert msg using the exception
        }
    }
}



