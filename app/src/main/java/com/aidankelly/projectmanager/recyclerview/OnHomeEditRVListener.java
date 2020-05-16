package com.aidankelly.projectmanager.recyclerview;

import com.aidankelly.projectmanager.entities.UserProject;

public interface OnHomeEditRVListener {
    void onProjectNameChangeClick(UserProject project);
    void onProjectDescriptionChangeClick(UserProject project);
    void onProjectSetToTopClick(UserProject project);
    void onProjectDeleteClick(UserProject project);
}
