package com.aidankelly.projectmanager.recyclerview;

import com.aidankelly.projectmanager.entities.UserProject;

public interface OnHomeEditRVListener {
    void onProjectNameChangeClick(UserProject project);
    void onProjectImageChangeClick(UserProject project);
    void onProjectSetToTopClick(UserProject project);
    void onProjectDeleteClick(UserProject project);
}
