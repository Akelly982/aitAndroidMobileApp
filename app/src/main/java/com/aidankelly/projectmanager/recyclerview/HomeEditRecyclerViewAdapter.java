package com.aidankelly.projectmanager.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

import java.util.List;

public class HomeEditRecyclerViewAdapter extends RecyclerView.Adapter<HomeEditViewHolder> {

    private List<UserProject> projects;
    private Context context;
    private OnHomeEditRVListener onHomeEditRVListener;


    public HomeEditRecyclerViewAdapter(List<UserProject> projects, Context context, OnHomeEditRVListener onHomeEditRVListener) {
        this.projects = projects;
        this.context = context;
        this.onHomeEditRVListener = onHomeEditRVListener;
    }

    @NonNull
    @Override
    public HomeEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create the ui from the XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // for this you may need to import the class R
        View homeEditViewItem = inflater.inflate(R.layout.recycler_item_view_home_edit, parent, false);

        HomeEditViewHolder homeEditViewHolder = new HomeEditViewHolder(homeEditViewItem);
        return homeEditViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeEditViewHolder holder, int position) {
        UserProject project = projects.get(position);
        holder.updateEditProject(project);
        holder.bindUpdateButtons(project, onHomeEditRVListener);

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }


    // for recycler view update
    public List<UserProject> getRvList() {
        return projects;
    }

    public void replaceItem(int position, UserProject project) {
        projects.set(position,project);   // use the new update project
        notifyItemChanged(position,null);
    }

    public void moveItemToFirst(int position, UserProject project){  // works
        // delete project at old position
        projects.remove(position);
        notifyItemRemoved(position);

        // add project at new position (top of list)
        int newPosition = 0;
        projects.add(newPosition,project);
        notifyItemInserted(newPosition);


    }

    public void deleteItemByIndex(int position){
        projects.remove(position);
        notifyItemRemoved(position);
    }



}
