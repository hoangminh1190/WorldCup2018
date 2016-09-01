package com.m2team.worldcup.qualifiers.matches.presenter;

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
import com.m2team.worldcup.model.Match;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OceniaMatchesFragment extends Fragment implements OnMatchesDataCompleteListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    MatchQualifierAdapter adapter;
    public static final String link = "http://www.fifa.com/worldcup/preliminaries/oceania/all-matches.html";

    public static OceniaMatchesFragment newInstance(int position) {
        OceniaMatchesFragment f = new OceniaMatchesFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EuroMatchesPresenter presenter = new EuroMatchesPresenter(getActivity(), link);
        presenter.setOnDataComplete(this);
        presenter.getData(getActivity(), Common.OCENIA_MATCHES_QUALIFIER, Common.ONE_DAY_IN_MILLISECONDS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, null);

        ButterKnife.bind(this, view);

        adapter = new MatchQualifierAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void updateView(List<Match> matches) {
        if (matches == null) {
            Snackbar.make(recyclerView, getString(R.string.error_get_data), Snackbar.LENGTH_SHORT).show();
        } else {
            adapter.setMatches(matches);

        }
    }

    @Override
    public void loadDone() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}