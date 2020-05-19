package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.google.android.material.snackbar.Snackbar;

public class ChangeTextActivity extends AppCompatActivity {

    private TextView dataName;
    private EditText userInputField;
    private Button setTextButton;
    private Button cancelButton;
    private Button exitButton;

    private View rootView;


    String stringToEdit = null;
    Integer projectId = null;

    Integer dataType; // == const from bellow
    private static final Integer STRING_PROJECT_NAME = 1;
    private static final String PROJECT_NAME_TEXT = "Project name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_text);

        rootView = findViewById(android.R.id.content).getRootView();

        dataName = findViewById(R.id.changeTextDataNameTextView);
        userInputField = findViewById(R.id.changeTextUserInputFieldEditText);
        setTextButton = findViewById(R.id.changeTextSetTextButton);
        cancelButton = findViewById(R.id.changeTextCancelButton);
        exitButton = findViewById(R.id.changeTextExitButton);


        Intent intentThatCalled = getIntent();

        //get passed text and determine data type
        if (intentThatCalled.hasExtra(UserProject.USER_PROJECT_PROJECT_NAME)) {  // if its a UserProject name
            stringToEdit = intentThatCalled.getStringExtra(UserProject.USER_PROJECT_PROJECT_NAME);
            projectId = intentThatCalled.getIntExtra(UserProject.USER_PROJECT_ID_KEY, -1);
            dataType = STRING_PROJECT_NAME;
            dataName.setText(PROJECT_NAME_TEXT);

            // if id gotten is the default -1
            if(projectId == -1){
                Snackbar.make(rootView, " project id not correctly passed == -1 " , Snackbar.LENGTH_SHORT).show();
            }

        }



        setTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the new string
                String myEditedString = userInputField.getText().toString();

                // check not empty or just spaces
                if(myEditedString.trim().isEmpty()){
                    Snackbar.make(v, "Text is required", Snackbar.LENGTH_SHORT).show();
                    userInputField.getText().clear();
                    userInputField.requestFocus();
                    return;
                }


                // return as the same type it came in as
                if (dataType == STRING_PROJECT_NAME){
                    Intent goingBack = new Intent();
                    goingBack.putExtra(UserProject.USER_PROJECT_PROJECT_NAME, myEditedString);
                    goingBack.putExtra(UserProject.USER_PROJECT_ID_KEY, projectId);
                    setResult(RESULT_OK, goingBack);
                    finish();
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



    private void exit() {
        setResult(RESULT_CANCELED);
        finish();
    }



}
