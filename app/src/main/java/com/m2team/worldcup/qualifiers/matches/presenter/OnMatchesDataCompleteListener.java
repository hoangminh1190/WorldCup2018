package com.m2team.worldcup.qualifiers.matches.presenter;

import com.m2team.worldcup.model.Group;
import com.m2team.worldcup.model.Match;

import java.util.List;

/**
 * Created by Tom on 8/31/2016.
 */
public interface OnMatchesDataCompleteListener {
    void updateView(List<Match> matchList);

    void loadDone();
}
