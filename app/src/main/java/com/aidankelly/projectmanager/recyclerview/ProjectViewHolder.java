package com.aidankelly.projectmanager.recyclerview;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.entities.UserProjectItem;


public class ProjectViewHolder extends RecyclerView.ViewHolder {

    private TextView itemDescTextView;
    private TextView costTextView;
    private Button deleteButton;
    private ImageView image;


    // maps UI components to the XML Layout.
    public ProjectViewHolder(@NonNull View itemView) {
        super(itemView);

        // link ui components
        itemDescTextView = itemView.findViewById(R.id.projectRVItemDescriptionTextView);
        costTextView = itemView.findViewById(R.id.projectRVItemCostTextView);
        deleteButton = itemView.findViewById(R.id.projectRVDeleteItemButton);
        image = itemView.findViewById(R.id.projectRVItemImageView2);

    }

    // puts data into my ViewHolder
    public void updateProjectItem(UserProjectItem item){

        itemDescTextView.setText(item.getDescription());
        costTextView.setText(item.getCost().toString());

        if (item.getImage() != null){     // if it returns null do not set image
            image.setImageBitmap(item.getImage());
        }


    }



    public void bindUpdateButtons(final UserProjectItem item, final OnProjectRVListener onProjectRVListener){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProjectRVListener.onDeleteItemClick(item);
            }
        });
    }


}
