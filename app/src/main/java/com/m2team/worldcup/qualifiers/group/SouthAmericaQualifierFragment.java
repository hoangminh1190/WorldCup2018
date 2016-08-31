package com.m2team.worldcup.qualifiers.group;

/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.m2team.worldcup.R;

public class SouthAmericaQualifierFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static SouthAmericaQualifierFragment newInstance(int position) {
        SouthAmericaQualifierFragment f = new SouthAmericaQualifierFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }
    private final String[] array = {"Android", "is", "Awesome", "World"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group, null);
       /* final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.view_row, R.id.header_text, array);
        final ExpandableLayoutListView expandableLayoutListView = (ExpandableLayoutListView) view.findViewById(R.id.listview);

        expandableLayoutListView.setAdapter(arrayAdapter);*/
        return view;
    }

}