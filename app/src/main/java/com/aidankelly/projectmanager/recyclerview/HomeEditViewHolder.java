package com.aidankelly.projectmanager.recyclerview;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

public class HomeEditViewHolder extends RecyclerView.ViewHolder {

    private Button changeImageButton;
    private Button deleteButton;
    private Button changeProjectNameButton;
    private Button setTopButton;
    private ImageView projectImageView;
    private TextView projectNameTextView;

    public HomeEditViewHolder(@NonNull View itemView) {
        super(itemView);
        // add ui connections
        changeImageButton = itemView.findViewById(R.id.changeImageButton);
        changeProjectNameButton = itemView.findViewById(R.id.changeProjectNameButton);
        deleteButton = itemView.findViewById(R.id.deleteProjectButton);
        setTopButton = itemView.findViewById(R.id.setTopButton);
        projectNameTextView = itemView.findViewById(R.id.projectNameHomeEditTextView);
        projectImageView = itemView.findViewById(R.id.projectImageHomeEditImageView);



        // button inputs
        setTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        changeProjectNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    // puts data into my ViewHolder
    public void updateEditProject(UserProject project){
        projectImageView.setImageBitmap(project.getProjectImage());
        projectNameTextView.setText(project.getProjectName());
    }

}
