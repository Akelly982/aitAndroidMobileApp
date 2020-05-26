package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

public class ProjectActivity extends AppCompatActivity {

    private UserProject project;    // the projects passed

    private Button closeOptionsButton;
    private Button openOptionsButton;
    private Button returnHomeButton;
    private Button editProjectItemsButton;
    private Button addItemButton;

    private View rootView;


    // for notes on anim refer to home activity
    private float animHideHeight = -570f;
    private float animShowHeight = 40f;
    private View optionsCardView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        closeOptionsButton = findViewById(R.id.projectCloseOptionsWindowButton);
        openOptionsButton = findViewById(R.id.projectOpenOptionsButton);
        returnHomeButton = findViewById(R.id.projectOptionsReturnHomeButton);
        editProjectItemsButton = findViewById(R.id.projectOptionsReturnHomeButton);
        addItemButton = findViewById(R.id.projectNewItemButton);
        optionsCardView = findViewById(R.id.projectOptionsCardView);

        rootView = findViewById(android.R.id.content).getRootView();



        // close options window Animation
        optionsMenuHide(0);




        openOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show options window
                optionsMenuShow();
            }
        });

        closeOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close options window
                optionsMenuHide(600);
            }

        });





    }



    private void optionsMenuHide(int duration) {  // adjustable so the first one can be instant on load
        ObjectAnimator animationHideStart = ObjectAnimator.ofFloat(optionsCardView, "translationY", animHideHeight);  //100f refers to num of pixels
        animationHideStart.setDuration(duration);
        animationHideStart.start();
    }

    private void optionsMenuShow() {
        ObjectAnimator animationShow = ObjectAnimator.ofFloat(optionsCardView, "translationY", animShowHeight);  //100f refers to num of pixels
        animationShow.setDuration(600);
        animationShow.start();
    }






}
