package com.aidankelly.projectmanager.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.services.DataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.aidankelly.projectmanager.entities.Constants.NEW_PROJECT_FETCH_IMAGE_CODE;

public class newProjectActivity extends AppCompatActivity {

    private View rootView;
    private DataService projectDataService;

    private Button exitNewProjectButton;
    private Button importImageButton;
    private Button createProjectButton;
    private EditText projectNameInputEditText;
    private ImageView imagePreviewImageView;

    public Uri imageFilePath;
    public Bitmap imageToStore;

    private Context myContext;    // needed for an imageView but not used just their

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        myContext = this;

        projectNameInputEditText = findViewById(R.id.projectNameInputEditText);
        createProjectButton = findViewById(R.id.createProjectButton);
        importImageButton = findViewById(R.id.importImgButton);
        imagePreviewImageView = findViewById(R.id.imagePreviewImageView);
        exitNewProjectButton = findViewById(R.id.newProjectExitButton);
        rootView = findViewById(R.id.content);


        //Load Data from the database
        projectDataService = new DataService();
        projectDataService.init(this);


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



        exitNewProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goingHome = new Intent();
                exit(v);
            }
        });

        createProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProject(v);
            }
        });

    }

            // create the project for import to database
    private void createProject(View v) {

        UserProject project = new UserProject();


        String name = projectNameInputEditText.getText().toString();

        // name.trim.isEmpty = deletes empty spaces in front of the string and checks to see if something is still their if not move cursor to component
        if(name.trim().isEmpty()){
            Snackbar.make(v, "Name is required", Snackbar.LENGTH_SHORT).show();
            projectNameInputEditText.getText().clear();
            projectNameInputEditText.requestFocus();
            return;
        }

        // get the projectName
        project.setProjectName(projectNameInputEditText.getText().toString());

        //get the projectImage
        if (imageToStore == null){
            imageToStore = findADefaultImage();
        }
        project.setProjectImage(imageToStore);

        // create a list to store potential errors
        ArrayList<Long> foundErrors = new ArrayList<Long>();

        // insert to db
        projectDataService.addProject(project, foundErrors);

        // check for errors
        if (foundErrors.get(0) > 0){
            Snackbar.make(v, "Error in ListPos Increment " + foundErrors.get(0) , Snackbar.LENGTH_SHORT).show();
        }
        else if (foundErrors.get(1) == -1){
            Snackbar.make(v, "Error in database insert " + foundErrors.get(0) , Snackbar.LENGTH_SHORT).show();
        }
        else{
            Snackbar.make(v, "Project added " + foundErrors.get(0) , Snackbar.LENGTH_SHORT).show();
        }



    }




    private void exit(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }


    @Override    // getting img from phone result
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PROJECT_FETCH_IMAGE_CODE){
            if (resultCode == RESULT_OK){
                setImageForStorage(data);
                imagePreviewImageView.setImageBitmap(imageToStore); // set image to preview window
            }
        }
    }

    private void setImageForStorage(Intent data) {
        try {
            imageFilePath = data.getData(); // URI
            imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath); // get bitmap from image and stores it in imageToStore
        }
        catch (Exception e){
            Snackbar.make(rootView, e.toString() , Snackbar.LENGTH_LONG).show();   // send a small alert msg using the exception
        }
    }



     // code for getting image from drawable not sure if i will use it or need it.....
    private Bitmap findADefaultImage() {
        ImageView myImageView = new ImageView(myContext);      //  myContext declared above but unused  (image View wants one)
        myImageView.setImageResource(R.drawable.project_default_image);
        BitmapDrawable drawable = (BitmapDrawable) myImageView.getDrawable();
        //BitmapDrawable drawable = ((BitmapDrawable) myContext.getDrawable(R.drawable.project_default_image));   // works with sdk update to 21   // myContext declared above to access context
        Bitmap myNewBitmap = drawable.getBitmap();
        return myNewBitmap;
    }



}



