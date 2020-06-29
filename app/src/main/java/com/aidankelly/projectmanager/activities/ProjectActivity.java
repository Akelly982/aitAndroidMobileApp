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
import android.widget.TextView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.Constants;
import com.aidankelly.projectmanager.entities.ImageManager;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.entities.UserProjectItem;
import com.aidankelly.projectmanager.recyclerview.OnProjectRVListener;
import com.aidankelly.projectmanager.recyclerview.ProjectRecyclerViewAdapter;
import com.aidankelly.projectmanager.services.DataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity implements OnProjectRVListener {

    private UserProject projectParent;    // the projects passed need its id for it's the foreign key

    private DataService myDataService;
    private List<UserProjectItem> projectItems;
    private ProjectRecyclerViewAdapter adapter;
    private Integer deletePositionRV = null;


    private Button closeOptionsButton;
    private Button openOptionsButton;
    private Button returnHomeButton;
    private Button addItemButton;
    private TextView projectTitleTextView;
    private TextView projectCostTextView;

    private View rootView;


    // for notes on anim refer to home activity
    private float animHideHeight = -570f;
    private float animShowHeight = 30f;
    private View optionsCardView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        closeOptionsButton = findViewById(R.id.projectCloseOptionsWindowButton);
        openOptionsButton = findViewById(R.id.projectOpenOptionsButton);
        returnHomeButton = findViewById(R.id.projectOptionsReturnHomeButton);
        addItemButton = findViewById(R.id.projectNewItemButton);
        optionsCardView = findViewById(R.id.projectOptionsCardView);

        projectCostTextView = findViewById(R.id.projectCostTitleTextView);
        projectTitleTextView = findViewById(R.id.projectTitleTextView);

        rootView = findViewById(android.R.id.content).getRootView();

        // get the parent project
        Intent incomingIntent = getIntent();
        projectParent = (UserProject) incomingIntent.getSerializableExtra(UserProject.USER_PROJECT_KEY);   // do not forget to cast

        // set parentProject details
        projectTitleTextView.setText(projectParent.getProjectName());
        projectCostTextView.setText(projectParent.getTotalProjectCost().toString());


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



        //Load Data from the database
        myDataService = new DataService();
        myDataService.init(this);



        //implement Rv
        RecyclerView projectRecyclerView = findViewById(R.id.projectRecyclerView);  // grab from content layout of activity
        // RecyclerView homeRecyclerView = findViewById(R.id.homeRecyclerView);

        // set layout manager  (which way you RV works)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);  // use  Class LinearLayoutManager not ViewHolder or something random from auto insert
        projectRecyclerView.setLayoutManager(linearLayoutManager);

        // get list of all items
        projectItems = myDataService.getAllItemsOfProject(projectParent);
        //create a RecyclerViewAdapter and pass the data
        adapter = new ProjectRecyclerViewAdapter(projectItems, this,this, projectParent);
        //set the adapter to the RecyclerView
        projectRecyclerView.setAdapter(adapter);




        // set buttons
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addItemIntent = new Intent(ProjectActivity.this,ProjectAddItem.class);
                addItemIntent.putExtra(UserProject.USER_PROJECT_KEY, projectParent);  // this has the foreign key to be used on item creation
                startActivityForResult(addItemIntent, Constants.PROJECT_ADD_ITEM_CODE);
            }
        });

        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // total cost gets updated from db on return
                setResult(RESULT_OK);
                finish();
            }
        });

        // update project cost (make sure it is correct)
        updateProjectCost();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PROJECT_ADD_ITEM_CODE){
            if (resultCode == RESULT_OK){
                updateNewItem(data);
            }
        }
        if (requestCode == Constants.DELETE_CONFIRMATION_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){
                deleteItem(data);
            }
        }
    }

    private void deleteItem(Intent data) {
        UserProjectItem myItem = (UserProjectItem) data.getSerializableExtra(UserProjectItem.USER_PROJECT_ITEM_KEY);

        //delete local image
        ImageManager imgManager = new ImageManager(this);
        boolean resultLocal = imgManager.deleteImageFileProjectItem(projectParent,myItem);
        if (resultLocal){
            Snackbar.make(rootView, " item deleted local "  , Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(rootView, " item not deleted local " , Snackbar.LENGTH_SHORT).show();
        }


        // delete from DB
        boolean result = myDataService.deleteItem(myItem.getId());
        // display result
        if (result){
            Snackbar.make(rootView, " item deleted: "  , Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(rootView, " item not deleted: " , Snackbar.LENGTH_SHORT).show();
        }

        // RemoveFromRV
        if (deletePositionRV != null){   // if exists
            adapter.deleteItemByIndex(deletePositionRV);
        }
        // reset deletePos to null
        deletePositionRV = null;

        // update parent project cost
        updateProjectCost();


    }

    private void updateNewItem(Intent data) {

        // get data from intent
        UserProjectItem myNewItem;
        myNewItem = (UserProjectItem) data.getSerializableExtra(UserProjectItem.USER_PROJECT_ITEM_KEY);


        // update rv
        adapter.addItem(myNewItem);

        //update the parentProject cost
        updateProjectCost();  // works based on all items being used by the RV with its list.

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


    @Override
    public void onDeleteItemClick(UserProjectItem item, Integer position) {
        Intent deleteItemIntent = new Intent(this, DeleteConfirmationActivity.class);
        deletePositionRV = position; // hold the position of this item
        deleteItemIntent.putExtra(UserProjectItem.USER_PROJECT_ITEM_KEY,item);
        startActivityForResult(deleteItemIntent, Constants.DELETE_CONFIRMATION_ACTIVITY_CODE);

    }



    public void updateProjectCost(){
        Float totalCost = 0f;
        // for each get cost and add to total
        List<UserProjectItem> currentList = adapter.getRvList();

        for (int i = 0; i < currentList.size(); i++){
            totalCost += currentList.get(i).getCost();
        }

        // set to projectParent
        projectParent.setTotalProjectCost(totalCost);

        // update current page
        projectCostTextView.setText(projectParent.getTotalProjectCost().toString());

        // set it to the db
        myDataService.updateProjectCost(projectParent);

    }





}
