package com.m2team.worldcup.qualifiers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.m2team.worldcup.BaseActivity;
import com.m2team.worldcup.R;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.qualifiers.group.fragment.AfricaQualifierFragment;
import com.m2team.worldcup.qualifiers.group.fragment.AsiaQualifierFragment;
import com.m2team.worldcup.qualifiers.group.fragment.CentralAmericaQualifierFragment;
import com.m2team.worldcup.qualifiers.group.fragment.EuroQualifierFragment;
import com.m2team.worldcup.qualifiers.group.fragment.OceanQualifierFragment;
import com.m2team.worldcup.qualifiers.group.fragment.SouthAmericaQualifierFragment;
import com.m2team.worldcup.qualifiers.matches.presenter.AfricaMatchesFragment;
import com.m2team.worldcup.qualifiers.matches.presenter.AsiaMatchesFragment;
import com.m2team.worldcup.qualifiers.matches.presenter.CentralAmericaMatchesFragment;
import com.m2team.worldcup.qualifiers.matches.presenter.EuroMatchesFragment;
import com.m2team.worldcup.qualifiers.matches.presenter.OceniaMatchesFragment;
import com.m2team.worldcup.qualifiers.matches.presenter.SouthAmericaMatchesFragment;
import com.m2team.worldcup.qualifiers.teams.fragment.AfricaTeamQualifierFragment;
import com.m2team.worldcup.qualifiers.teams.fragment.AsiaTeamQualifierFragment;
import com.m2team.worldcup.qualifiers.teams.fragment.CentralAmericaTeamQualifierFragment;
import com.m2team.worldcup.qualifiers.teams.fragment.EuroTeamQualifierFragment;
import com.m2team.worldcup.qualifiers.teams.fragment.OceniaTeamQualifierFragment;
import com.m2team.worldcup.qualifiers.teams.fragment.SouthAmericaTeamQualifierFragment;
import com.m2team.worldcup.stadiums.HostedCountryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @BindView(R.id.pager)
    ViewPager pager;

    private MyPagerAdapter adapter;
    private int EURO = 0;
    private int SOUTH_AMERICA = 1;
    private int ASIA = 2;
    private int CENTRAL_AMERICA = 3;
    private int OCEAN = 4;
    private int AFRICA = 5;


    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initTabs();

        initDrawer(toolbar);
    }

    private static String QUALIFIER_TYPE = "qualifier_type";

    public static Intent createIntent(Context context, int qualifierType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(QUALIFIER_TYPE, qualifierType);
        return intent;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView tv_hosted_contry = (TextView) headerView.findViewById(R.id.tv_hosted_contry);
        tv_hosted_contry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HostedCountryActivity.class));
            }
        });
    }

    private void initTabs() {
        int qualifierType = getIntent().getIntExtra(QUALIFIER_TYPE, EURO);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), qualifierType);

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);

        tabs.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabs.setDividerColor(getColor(R.color.colorPrimary));
        } else {
            tabs.setDividerColor(getResources().getColor(R.color.colorPrimary));
        }
        tabs.setIndicatorColor(getResources().getColor(R.color.divider_color));
        tabs.setIndicatorHeight(8);
        tabs.setTypeface(Typeface.createFromAsset(getAssets(), Common.TAB_FONT), R.style.TextViewTabs);

        //changeColor(currentColor);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_africa) {
            finish();
            startActivity(MainActivity.createIntent(this, AFRICA));
        } else if (id == R.id.nav_central_america) {
            finish();
            startActivity(MainActivity.createIntent(this, CENTRAL_AMERICA));
        } else if (id == R.id.nav_ocenia) {
            finish();
            startActivity(MainActivity.createIntent(this, OCEAN));
        } else if (id == R.id.nav_euro) {
            finish();
            startActivity(MainActivity.createIntent(this, EURO));
        } else if (id == R.id.nav_asia) {
            finish();
            startActivity(MainActivity.createIntent(this, ASIA));
        } else if (id == R.id.nav_sa) {
            finish();
            startActivity(MainActivity.createIntent(this, SOUTH_AMERICA));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private int type;
        private final String[] TITLES = {"Group", "Matches", "Team"};

        public MyPagerAdapter(FragmentManager fm, int type) {
            super(fm);
            this.type = type;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(Common.TAG, "==========getItem " + position + " type " + type);
            if (position == 0) {
                if (type == EURO)
                    return EuroQualifierFragment.newInstance(position);
                else if (type == ASIA)
                    return AsiaQualifierFragment.newInstance(position);
                else if (type == SOUTH_AMERICA)
                    return SouthAmericaQualifierFragment.newInstance(position);
                else if (type == CENTRAL_AMERICA)
                    return CentralAmericaQualifierFragment.newInstance(position);
                else if (type == OCEAN)
                    return OceanQualifierFragment.newInstance(position);
                else if (type == AFRICA)
                    return AfricaQualifierFragment.newInstance(position);

            } else if (position == 1) {
                if (type == EURO)
                    return EuroMatchesFragment.newInstance(position);
                else if (type == ASIA)
                    return AsiaMatchesFragment.newInstance(position);
                else if (type == SOUTH_AMERICA)
                    return SouthAmericaMatchesFragment.newInstance(position);
                else if (type == CENTRAL_AMERICA)
                    return CentralAmericaMatchesFragment.newInstance(position);
                else if (type == OCEAN)
                    return OceniaMatchesFragment.newInstance(position);
                else if (type == AFRICA)
                    return AfricaMatchesFragment.newInstance(position);
            } else if (position == 2) {
                if (type == EURO)
                    return EuroTeamQualifierFragment.newInstance(position);
                else if (type == ASIA)
                    return AsiaTeamQualifierFragment.newInstance(position);
                else if (type == SOUTH_AMERICA)
                    return SouthAmericaTeamQualifierFragment.newInstance(position);
                else if (type == CENTRAL_AMERICA)
                    return CentralAmericaTeamQualifierFragment.newInstance(position);
                else if (type == OCEAN)
                    return OceniaTeamQualifierFragment.newInstance(position);
                else if (type == AFRICA)
                    return AfricaTeamQualifierFragment.newInstance(position);

            }
            return null;
        }

    }

    private void changeColor(int newColor) {


        // change ActionBar color just if an ActionBar is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            Drawable colorDrawable = new ColorDrawable(newColor);
            Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
            LayerDrawable ld = new LayerDrawable(new Drawable[]{colorDrawable, bottomDrawable});

            if (oldBackground == null) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    ld.setCallback(drawableCallback);
                } else {
                    getSupportActionBar().setBackgroundDrawable(ld);
                }

            } else {

                TransitionDrawable td = new TransitionDrawable(new Drawable[]{oldBackground, ld});

                // workaround for broken ActionBarContainer drawable handling on
                // pre-API 17 builds
                // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    td.setCallback(drawableCallback);
                } else {
                    getSupportActionBar().setBackgroundDrawable(td);
                }

                td.startTransition(200);

            }

            oldBackground = ld;

            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        }

        currentColor = newColor;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
        changeColor(currentColor);
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getSupportActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };
}
