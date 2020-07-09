package com.aidankelly.projectmanager.recyclerview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.ImageManager;
import com.aidankelly.projectmanager.entities.UserProject;

public class HomeEditViewHolder extends RecyclerView.ViewHolder {

    private ImageManager imgManager;
    private Context myContext;

    private Button changeImageButton;
    private Button deleteButton;
    private Button changeProjectNameButton;
    private Button setTopButton;
    private ImageView projectImageView;
    private TextView projectNameTextView;

    public HomeEditViewHolder(@NonNull View itemView, Context myContext) {
        super(itemView);
        this.myContext = myContext;

        // add ui connections
        changeImageButton = itemView.findViewById(R.id.homeEditChangeProjectImageButton);
        changeProjectNameButton = itemView.findViewById(R.id.homeEditChangeProjectNameButton);
        deleteButton = itemView.findViewById(R.id.homeEditDeleteProjectButton);
        setTopButton = itemView.findViewById(R.id.homeEditSetProjectTopButton);
        projectNameTextView = itemView.findViewById(R.id.homeEditProjectNameTextView);
        projectImageView = itemView.findViewById(R.id.homeEditProjectImageImageView);

    }

    // puts data into my ViewHolder
    public void updateEditProject(UserProject project){
        imgManager = new ImageManager(myContext);
        imgManager.setDirNameAndFileName(project.getProjectDirectory(),project.getHomeImageFileName());

        projectImageView.setImageBitmap(imgManager.load());
        projectNameTextView.setText(project.getProjectName());

    }

    public void bindUpdateButtons(final UserProject project, final OnHomeEditRVListener onHomeEditRVListener){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeEditRVListener.onProjectDeleteClick(project);
            }
        });
        setTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeEditRVListener.onProjectSetToTopClick(project);
            }
        });
        changeProjectNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeEditRVListener.onProjectNameChangeClick(project);
            }
        });
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeEditRVListener.onProjectImageChangeClick(project);
            }
        });
    }



}
