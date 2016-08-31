package com.m2team.worldcup.home;

import com.m2team.worldcup.BaseActivity;
import com.m2team.worldcup.R;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.qualifiers.MainActivity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_qualifier)
    public void openQualifier() {
        Common.startActivity(this, MainActivity.class);
    }

}