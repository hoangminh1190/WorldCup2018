package com.m2team.worldcup.qualifiers.teams;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.m2team.worldcup.R;
import com.m2team.worldcup.model.Team;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class AllTeamQualifierAdapter extends RecyclerView.Adapter<AllTeamQualifierAdapter.ViewHolder> {

    private Context context;
    private List<Team> teamList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Gson gson;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_team)
        TextView tv_name;
        @BindView(R.id.img_flag)
        CircleImageView imgFlag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public AllTeamQualifierAdapter(Context context) {
        this.context = context;
        teamList = new ArrayList<>();
        gson = new Gson();
        initImageLoader();

    }

    public void setTeams(List<Team> teams) {
        this.teamList = teams;
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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_each_team, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Team team = teamList.get(position);
        holder.tv_name.setText(team.getName());
        imageLoader.displayImage(team.getAvatar(), holder.imgFlag);

        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = gson.toJson(team);
                context.startActivity(TeamDetailActivity.createIntent(context, json));
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }
}