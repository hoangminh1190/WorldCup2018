package com.m2team.worldcup.stadiums;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m2team.worldcup.BaseActivity;
import com.m2team.worldcup.R;
import com.m2team.worldcup.common.Applog;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Team;
import com.m2team.worldcup.qualifiers.teams.OnDataCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class StadiumDetailActivity extends BaseActivity implements OnDataCompleteListener, AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.avatar)
    CircleImageView imageViewAvatar;
    @BindView(R.id.team_detail)
    TextView textViewDetail;
    @BindView(R.id.team_bg)
    ImageView imageViewBG;
    @BindView(R.id.title)
    TextView textViewTitle;
    @BindView(R.id.tv_profile)
    TextView tv_profile;
    @BindView(R.id.tv_profile_desc)
    TextView tv_profile_desc;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.main_linearlayout_title)
    LinearLayout mTitleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_detail);

        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("url");
        String stadium = getIntent().getStringExtra("stadium");

        if (TextUtils.isEmpty(url)) {
            Snackbar.make(imageViewAvatar, getString(R.string.error_open_stadium), Snackbar.LENGTH_LONG)
                    .show();
            finish();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);

        startAlphaAnimation(textViewTitle, 0, View.INVISIBLE);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initImageLoader();

        textViewTitle.setText(stadium);
        tv_profile.setText(stadium);
        tv_profile_desc.setText("");

        imageViewAvatar.setImageResource(R.mipmap.ic_russia);

        StadiumDetailPresenter presenter = new StadiumDetailPresenter(this);
        presenter.setListener(this);
        presenter.getStadium(stadium, url);

    }

    public void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                //.bitmapConfig(Bitmap.Config.RGB_565)
                //.displayer(new RoundedBitmapDisplayer(12))
                .build();
    }

    public static Intent createIntent(Context context, String url, String stadium) {
        Intent intent = new Intent(context, StadiumDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("stadium", stadium);
        return intent;
    }

    @Override
    public void updateView(Team team) {

    }

    @Override
    public void updateTeamDetail(List<String> detail) {
        Applog.d(Common.TAG, "haizz data= " + detail);
        if (detail == null) {
            Snackbar.make(textViewDetail, getString(R.string.error_get_data), Snackbar.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(detail.get(0))) {
                textViewDetail.setText(Html.fromHtml(detail.get(0)));
            }
            if (!TextUtils.isEmpty(detail.get(1))) {
                imageLoader.displayImage(detail.get(1), imageViewBG, options);
            }
        }
    }

    @Override
    public void loadDone() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(textViewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textViewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
