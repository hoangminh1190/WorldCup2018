package com.m2team.worldcup.stadiums;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.m2team.worldcup.R;
import com.m2team.worldcup.model.Stadium;
import com.m2team.worldcup.qualifiers.teams.AllTeamQualifierAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StadiumFragment extends Fragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private StadiumQualifierAdapter adapter;
    private List<Stadium> stadiumList;

    public static StadiumFragment newInstance(int position) {
        StadiumFragment f = new StadiumFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stadiumList = new ArrayList<>();
        Stadium stadium = new Stadium("Ekaterinburg Arena", "http://www.fifa.com/worldcup/destination/stadiums/stadium=5031304/index.html",
                "http://img.fifa.com/mm/photo/tournament/destination/02/70/91/99/2709199_full-lnd.jpg");
        Stadium stadium2 = new Stadium("Kaliningrad Stadium", "http://www.fifa.com/worldcup/destination/stadiums/stadium=5000437/index.html",
                "http://img.fifa.com/mm/photo/tournament/destination/02/70/92/25/2709225_full-lnd.jpg");
        Stadium stadium3 = new Stadium("Volgograd Arena", "http://www.fifa.com/worldcup/destination/stadiums/stadium=5000569/index.html",
                "http://img.fifa.com/mm/photo/tournament/destination/02/70/92/21/2709221_full-lnd.jpg");
        Stadium stadium4 = new Stadium("Fisht Stadium", "http://www.fifa.com/worldcup/destination/stadiums/stadium=5031302/index.html",
                "http://img.fifa.com/mm/photo/tournament/destination/02/70/92/18/2709218_full-lnd.jpg");
        Stadium stadium5 = new Stadium("Kazan Arena", "http://www.fifa.com/worldcup/destination/stadiums/stadium=5028773/index.html",
                "http://img.fifa.com/mm/photo/tournament/destination/02/70/92/01/2709201_full-lnd.jpg");
        stadiumList.add(stadium);
        stadiumList.add(stadium2);
        stadiumList.add(stadium3);
        stadiumList.add(stadium4);
        stadiumList.add(stadium5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stadium, null);
        ButterKnife.bind(this, view);
        progressBar.setVisibility(View.INVISIBLE);

        adapter = new StadiumQualifierAdapter(getActivity());
        adapter.setData(stadiumList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return view;
    }
}