package com.m2team.worldcup.qualifiers.teams;

import com.m2team.worldcup.model.Team;

import java.util.List;

public interface OnDataCompleteListener {

    void updateView(Team team);
    void updateTeamDetail(List<String> detail);
    void loadDone();
}
