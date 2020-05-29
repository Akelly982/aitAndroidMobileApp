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
import com.aidankelly.projectmanager.entities.Constants;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.entities.UserProjectItem;
import com.google.android.material.snackbar.Snackbar;

import static com.aidankelly.projectmanager.entities.Constants.FETCH_IMAGE_CODE;

public class ProjectAddItem extends AppCompatActivity {

    Button exitButton;
    Button createProjectItemButton;
    EditText itemCost;
    EditText itemDesc;
    ImageView imageImageView;
    Button importImageButton;
    View rootView;

    Bitmap myImageToStore;

    UserProject parentProject;  // holds the FK


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add_item);

        rootView = findViewById(android.R.id.content).getRootView();

        // connectButtons
        exitButton = findViewById(R.id.projectAddItemExitButton);
        createProjectItemButton = findViewById(R.id.projectAddItemAddToProjectButton);
        itemCost = findViewById(R.id.projectAddItemCostTextInput);
        itemDesc = findViewById(R.id.projectAddItemDescExpenseInputEditText);
        importImageButton = findViewById(R.id.projectAddItemImportImageButton);
        imageImageView = findViewById(R.id.projectAddItemPreviewImageView);


        // get the parentProject
        Intent intentThatCalled = getIntent();
        parentProject = (UserProject) intentThatCalled.getSerializableExtra(UserProject.USER_PROJECT_KEY);



        createProjectItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create and return new item
                UserProjectItem item = new UserProjectItem();

                // set desc
                String desc = itemDesc.getText().toString();
                if(desc.trim().isEmpty()){
                    Snackbar.make(rootView, "Expense Description required", Snackbar.LENGTH_SHORT).show();
                    itemDesc.getText().clear();
                    itemDesc.requestFocus();
                    return;
                }
                item.setDescription(desc);


                //set cost
                String cost = itemCost.getText().toString();
                // check if empty
                if(cost.trim().isEmpty()){
                    Snackbar.make(rootView, "Cost input required", Snackbar.LENGTH_SHORT).show();
                    itemCost.getText().clear();
                    itemCost.requestFocus();
                    return;
                }
                //check if not decimal
                if(!checkForIsNumeric(cost)){
                    Snackbar.make(rootView, "Cost must be numeric", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                item.setCost(Float.parseFloat(cost));


                //set image if not null
                if (myImageToStore == null){   // TODO make it so that it can work with no image
                    Snackbar.make(rootView, "add an image", Snackbar.LENGTH_SHORT).show();
                    return;
                }else{
                    item.setImage(myImageToStore);
                }


                //return
                Intent returnNewItem = new Intent();
                returnNewItem.putExtra(UserProjectItem.USER_PROJECT_ITEM_KEY,item);
                setResult(RESULT_OK, returnNewItem);
                finish();
            }


        });


        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageObject = new Intent();
                getImageObject.setType("image/*");

                getImageObject.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(getImageObject, FETCH_IMAGE_CODE);
            }
        });



        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });



    }




    private boolean checkForIsNumeric(String cost) {
        boolean onlyNumeric = true;
        for(int i = 0; i < cost.length(); i++){
            if (!Character.isDigit(cost.charAt(i))){
                onlyNumeric = false;
                break;
            }
        }
        return onlyNumeric;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FETCH_IMAGE_CODE){
            if (resultCode == RESULT_OK){
                readyImage(data);
            }
        }
    }

    private void readyImage(Intent data) {
        try {
            Uri imageFilePath;
            imageFilePath = data.getData(); // URI
            myImageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath); // get bitmap from image and stores it in imageToStore
        }
        catch (Exception e){
            Snackbar.make(rootView, e.toString() , Snackbar.LENGTH_LONG).show();   // send a small alert msg using the exception
        }
        imageImageView.setImageBitmap(myImageToStore); // set image to preview window
    }


}
