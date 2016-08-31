package com.m2team.worldcup.qualifiers.group;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.m2team.worldcup.R;
import com.m2team.worldcup.model.Group;
import com.m2team.worldcup.qualifiers.group.presenter.CentralAmericaQualifierPresenter;
import com.m2team.worldcup.qualifiers.group.presenter.EuroQualifierPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CentralAmericaQualifierFragment extends Fragment implements OnDataCompleteListener {


    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    GroupQualifierAdapter expandableListAdapter;

    public static CentralAmericaQualifierFragment newInstance(int position) {
        CentralAmericaQualifierFragment f = new CentralAmericaQualifierFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CentralAmericaQualifierPresenter presenter = new CentralAmericaQualifierPresenter(getActivity());
        presenter.setOnDataComplete(this);
        presenter.getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, null);

        ButterKnife.bind(this, view);

        expandableListAdapter = new GroupQualifierAdapter(getActivity());
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        return view;
    }


    @Override
    public void updateView(List<Group> groups) {
        if (groups == null) {
            Snackbar.make(expandableListView, getString(R.string.error_get_data), Snackbar.LENGTH_SHORT).show();
        } else {
            expandableListAdapter.setGroups(groups);
        }
    }

    @Override
    public void loadDone() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}