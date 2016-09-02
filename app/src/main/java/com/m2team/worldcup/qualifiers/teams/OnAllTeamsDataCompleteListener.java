package com.m2team.worldcup.qualifiers.teams;

import com.m2team.worldcup.model.Team;

import java.util.List;

public interface OnAllTeamsDataCompleteListener {

    void updateView(List<Team> teams);
    void loadDone();
}
