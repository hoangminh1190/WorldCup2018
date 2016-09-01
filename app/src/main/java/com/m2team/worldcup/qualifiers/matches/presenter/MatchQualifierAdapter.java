package com.m2team.worldcup.qualifiers.matches.presenter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.m2team.worldcup.R;
import com.m2team.worldcup.model.Group;
import com.m2team.worldcup.model.Match;
import com.m2team.worldcup.model.Team;
import com.m2team.worldcup.qualifiers.teams.TeamActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MatchQualifierAdapter extends RecyclerView.Adapter<MatchQualifierAdapter.ViewHolder> {

    private Context context;
    private List<Match> matches;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Gson gson;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_away_name)
        TextView tv_away_name;
        @BindView(R.id.tv_day)
        TextView tv_day;
        @BindView(R.id.tv_group)
        TextView tv_group;
        @BindView(R.id.tv_home_name)
        TextView tv_home_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.img_away_flag)
        ImageView imgAwayFlag;
        @BindView(R.id.img_home_flag)
        ImageView imgHomeFlag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MatchQualifierAdapter(Context context) {
        this.context = context;
        matches = new ArrayList<>();
        gson = new Gson();
        initImageLoader();

    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    public void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                //.bitmapConfig(Bitmap.Config.RGB_565)
                //.displayer(new RoundedBitmapDisplayer(12))
                .build();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_each_match, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.tv_away_name.setText(match.teamA.getName());
        holder.tv_home_name.setText(match.teamH.getName());
        holder.tv_day.setText(match.day);
        holder.tv_group.setText(match.group);
        holder.tv_time.setText(match.time);
        imageLoader.displayImage(match.teamH.getAvatar(), holder.imgHomeFlag);
        imageLoader.displayImage(match.teamA.getAvatar(), holder.imgAwayFlag);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }
}