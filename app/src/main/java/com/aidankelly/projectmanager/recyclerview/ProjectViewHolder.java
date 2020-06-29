package com.aidankelly.projectmanager.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.ImageManager;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.entities.UserProjectItem;


public class ProjectViewHolder extends RecyclerView.ViewHolder {

    private ImageManager imgManager;
    private Context myContext;

    private TextView itemDescTextView;
    private TextView costTextView;
    private Button deleteButton;
    private ImageView image;


    // maps UI components to the XML Layout.
    public ProjectViewHolder(@NonNull View itemView, Context myContext) {
        super(itemView);
        this.myContext = myContext;

        // link ui components
        itemDescTextView = itemView.findViewById(R.id.projectRVItemDescriptionTextView);
        costTextView = itemView.findViewById(R.id.projectRVItemCostTextView);
        deleteButton = itemView.findViewById(R.id.projectRVDeleteItemButton);
        image = itemView.findViewById(R.id.projectRVItemImageView2);

    }

    // puts data into my ViewHolder
    public void updateProjectItem(UserProjectItem item, UserProject parentProject){
        imgManager = new ImageManager(myContext);
        imgManager.setDirNameAndFileName(parentProject.getProjectDirectory(),item.getImagePath());

        image.setImageBitmap(imgManager.load());
        itemDescTextView.setText(item.getDescription());
        costTextView.setText(item.getCost().toString());

        //todo make it so that the image can be collapsed in the rv for just text responses
        //if (item.getImage() != null){     // if it returns null do not set image
            //image.setImageBitmap(item.getImage());
        //}

    }



    public void bindUpdateButtons(final UserProjectItem item, final OnProjectRVListener onProjectRVListener, final Integer position){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProjectRVListener.onDeleteItemClick(item, position);
            }
        });
    }


}
