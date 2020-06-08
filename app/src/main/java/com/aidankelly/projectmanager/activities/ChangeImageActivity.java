package com.aidankelly.projectmanager.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.google.android.material.snackbar.Snackbar;

import static com.aidankelly.projectmanager.entities.Constants.FETCH_IMAGE_CODE;

public class ChangeImageActivity extends AppCompatActivity {

    private Button exitButton;
    private Button cancelButton;
    private Button importImageButton;
    private Button setImageButton;

    private UserProject project;
    private ImageView imagePreviewImageView;
    private Bitmap imageToStore;
    private boolean notGotAnImage = true;

    private Integer setDataChangeType = null;
    private static final Integer USER_PROJECT_IMAGE = 1;

    private View rootView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        rootView = findViewById(android.R.id.content).getRootView();

        exitButton = findViewById(R.id.changeImageExitButton);
        cancelButton = findViewById(R.id.changeImageCancelButton);
        importImageButton = findViewById(R.id.changeImageImportImageButton);
        setImageButton = findViewById(R.id.changeImageSetImageButton);
        imagePreviewImageView = findViewById(R.id.changeImagePreviewImageView);



        Intent intentThatCalled = getIntent();
        if (intentThatCalled.hasExtra(UserProject.USER_PROJECT_KEY)){
            project = (UserProject) intentThatCalled.getSerializableExtra(UserProject.USER_PROJECT_KEY);

            setDataChangeType = USER_PROJECT_IMAGE;

            // set original image into the preview
            imagePreviewImageView.setImageBitmap(project.getProjectImage());

        }else{
            Snackbar.make(rootView, "error intent dose not hold recognisable data" , Snackbar.LENGTH_LONG).show();
        }


        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the new image
                Intent getImageObject = new Intent();
                getImageObject.setType("image/*");

                getImageObject.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(getImageObject, FETCH_IMAGE_CODE);
            }
        });



        setImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (setDataChangeType == USER_PROJECT_IMAGE){
                    if (notGotAnImage){
                        // if new image == old image
                        Snackbar.make(rootView, "Import an img first." , Snackbar.LENGTH_SHORT).show();

                    }else{
                        //set image to the project
                        project.setProjectImage(imageToStore);

                        // return new project
                        Intent goingBackWithNewImage = new Intent();
                        goingBackWithNewImage.putExtra(UserProject.USER_PROJECT_KEY, project);
                        setResult(RESULT_OK, goingBackWithNewImage);
                        finish();
                    }
                }else {
                    Snackbar.make(rootView, "error do not know what data you are changing" , Snackbar.LENGTH_LONG).show();
                }



            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

    }



    @Override    // getting img from phone result
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FETCH_IMAGE_CODE){
            if (resultCode == RESULT_OK){
                setImageForStorage(data);
            }
        }
    }

    private void setImageForStorage(Intent data) {
        try {
            Uri imageFilePath;
            imageFilePath = data.getData(); // URI
            imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath); // get bitmap from image and stores it in imageToStore
        }
        catch (Exception e){
            Snackbar.make(rootView, e.toString() , Snackbar.LENGTH_LONG).show();   // send a small alert msg using the exception
        }

        imagePreviewImageView.setImageBitmap(imageToStore); // set the new image to preview window and set to global variable
        notGotAnImage = false;
    }







    private void exit() {
        setResult(RESULT_CANCELED);
        finish();
    }




}
