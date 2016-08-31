package com.m2team.worldcup.qualifiers.group;

import com.m2team.worldcup.model.Group;

import java.util.List;

public interface OnDataCompleteListener {

    void updateView(List<Group> groups);

    void loadDone();
}
