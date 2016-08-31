package com.m2team.worldcup.qualifiers.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2team.worldcup.R;

public class AsiaQualifierFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static AsiaQualifierFragment newInstance(int position) {
        AsiaQualifierFragment f = new AsiaQualifierFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }
    private final String[] array = {"Hello", "World", "Android"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group, null);
        return view;
    }

}