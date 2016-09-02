package com.m2team.worldcup.qualifiers.teams;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.m2team.worldcup.R;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Team;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamQualifierFragment extends Fragment implements OnAllTeamsDataCompleteListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    AllTeamQualifierAdapter adapter;

    public static TeamQualifierFragment newInstance(int position) {
        TeamQualifierFragment f = new TeamQualifierFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AllTeamPresenter presenter = new AllTeamPresenter(getActivity(), Common.EURO_QUALIFIER_LINK);
        presenter.setListener(this);
        presenter.getData(Common.EURO_TEAMS_QUALIFIER, Common.ONE_MONTH_IN_MILLISECONDS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, null);

        ButterKnife.bind(this, view);

        adapter = new AllTeamQualifierAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void updateView(List<Team> teams) {
        if (teams == null) {
            Snackbar.make(recyclerView, getString(R.string.error_get_data), Snackbar.LENGTH_SHORT).show();
        } else {
            adapter.setTeams(teams);

        }
    }

    @Override
    public void loadDone() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}