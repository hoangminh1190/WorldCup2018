package com.m2team.worldcup.model;

import java.util.List;

public class Group {

    private String groupName;
    private List<Team> teams;

    public Group() {

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Group(String groupName, List<Team> teams) {
        this.groupName = groupName;
        this.teams = teams;
    }
}
