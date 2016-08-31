package com.m2team.worldcup.model;

public class Team {
    private String name;
    private String avatar;
    private String mp;
    private String win;
    private String draw;
    private String lose;
    private String gf;//win goal
    private String ga;//lose goal
    private String gdelta;
    private String pts;
    private String code;
    private String teamUrl;
    private String teamMatchesUrl;

    public Team(String name, String code, String avatar, String mp, String win, String draw, String lose, String gf,
                String ga, String gdelta, String pts, String teamUrl, String teamMatchesUrl) {
        this.name = name;
        this.avatar = avatar;
        this.mp = mp;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.gf = gf;
        this.ga = ga;
        this.gdelta = gdelta;
        this.pts = pts;
        this.code = code;
        this.teamUrl = teamUrl;
        this.teamMatchesUrl = teamMatchesUrl;
    }

    public Team() {

    }

    public String getTeamUrl() {
        return teamUrl;
    }

    public void setTeamUrl(String teamUrl) {
        this.teamUrl = teamUrl;
    }

    public String getTeamMatchesUrl() {
        return teamMatchesUrl;
    }

    public void setTeamMatchesUrl(String teamMatchesUrl) {
        this.teamMatchesUrl = teamMatchesUrl;
    }

    public String getGoals() {
        return gf + ":" + ga;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }

    public String getGf() {
        return gf;
    }

    public void setGf(String gf) {
        this.gf = gf;
    }

    public String getGa() {
        return ga;
    }

    public void setGa(String ga) {
        this.ga = ga;
    }

    public String getGdelta() {
        return gdelta;
    }

    public void setGdelta(String gdelta) {
        this.gdelta = gdelta;
    }

    public String getPts() {
        return pts;
    }

    public void setPts(String pts) {
        this.pts = pts;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
