package com.m2team.worldcup.model;

import java.util.List;

public class Round {

    private String roundName;
    private List<Group> groups;

    public Round() {
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Round(String roundName, List<Group> groups) {
        this.roundName = roundName;
        this.groups = groups;
    }
}
