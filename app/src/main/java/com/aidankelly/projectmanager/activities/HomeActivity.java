package com.aidankelly.projectmanager.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.recyclerview.HomeRecyclerViewAdapter;
import com.aidankelly.projectmanager.recyclerview.OnRecyclerViewListener;
import com.aidankelly.projectmanager.services.DataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.aidankelly.projectmanager.entities.Constants.NEW_PROJECT_ACTIVITY_CODE;

public class HomeActivity extends AppCompatActivity implements OnRecyclerViewListener {

    private DataService myDataService;
    private List<UserProject> projects;

    private Button newProjectButton;
    private Button optionsButton;
    private Button closeOptionsWindowButton;
    private Button searchProjectButton;
    private Button editProjectButton;
    private View rootView;
    private View optionsCardView;
    private float animHideHeight = -570f;   // not sure why these are so different
    private float animShowHeight = 40f;     // may be due to it loses track once off screen (very weird)



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rootView = findViewById(android.R.id.content).getRootView();

        newProjectButton = findViewById(R.id.homeNewProjectButton);
        optionsButton = findViewById(R.id.homeOptionsButton);
        closeOptionsWindowButton = findViewById(R.id.homeCloseOptionsWindowButton);
        optionsCardView = findViewById(R.id.optionsCardView);
        searchProjectButton = findViewById(R.id.homeSearchProjectsButton);
        editProjectButton = findViewById(R.id.homeEditOveralProjectsButton);


        // close options window Animation
        optionsMenuHide(0); // So i can still see my buttons in Activity XML edit, hide on load




        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show options window
                optionsMenuShow();
            }
        });

        closeOptionsWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close options window
                optionsMenuHide(600);
            }

        });



        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewProjectActivity = new Intent(HomeActivity.this, newProjectActivity.class);
                startActivity(goToNewProjectActivity);
            }
        });



        //Load Data from the database
        myDataService = new DataService();
        myDataService.init(this);


        //implementing the recycler view
        RecyclerView HomeRecyclerView = findViewById(R.id.homeRecyclerView);   // pass it its recycler view from content_myActivity
        // setting the layout linear
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);   // can change between horizontal and vertical
        // set the layout manager
        HomeRecyclerView.setLayoutManager(linearLayoutManager);

        // get a list of all projects
        projects = myDataService.getProjects();
        //create a RecyclerViewAdapter and pass the data
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(projects , this, this);
        //set the adapter to the RecyclerView
        HomeRecyclerView.setAdapter(adapter);



        searchProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO add search ability;
            }
        });

        editProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProjectsActivity = new Intent(HomeActivity.this, HomeEditActivity.class);
                startActivity(editProjectsActivity);
            }
        });

    }

    private void optionsMenuHide(int duration) {
        ObjectAnimator animationHideStart = ObjectAnimator.ofFloat(optionsCardView, "translationY", animHideHeight);  //100f refers to num of pixels
        animationHideStart.setDuration(duration);
        animationHideStart.start();
    }

    private void optionsMenuShow() {
        ObjectAnimator animationShow = ObjectAnimator.ofFloat(optionsCardView, "translationY", animShowHeight);  //100f refers to num of pixels
        animationShow.setDuration(600);
        animationShow.start();
    }


    @Override    // runs when Activity is not visible   (app lifecycle)
    protected void onStop() {
        super.onStop();
        optionsMenuHide(0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_PROJECT_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){

            }
        }
    }





    // if click is pressed
    @Override
    public void onProjectItemEnterClick(UserProject project) { // home enter button on activity intent with carried project
        Snackbar.make(rootView, " project id:  " + project.getProjectName() , Snackbar.LENGTH_SHORT).show();
        // intent enter project data
    }
}
