package com.m2team.worldcup.model;

/**
 * Created by Tom on 9/1/2016.
 */
public class Stadium {
    public String name;
    public String link;
    public String background;
    public String detail;

    public Stadium(String name, String link) {
        this.name = name;
        this.link = link;
    }
    public Stadium(String name, String link, String background) {
        this.name = name;
        this.link = link;
        this.background = background;
    }
}
