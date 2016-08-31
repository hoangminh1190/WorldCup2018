package com.m2team.worldcup.qualifiers.group;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.m2team.worldcup.R;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Group;
import com.m2team.worldcup.model.Team;
import com.m2team.worldcup.qualifiers.teams.TeamActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GroupQualifierAdapter extends BaseExpandableListAdapter {
    @BindView(R.id.avatar)
    ImageView imageView;
    @BindView(R.id.name)
    TextView textViewName;
    @BindView(R.id.mp)
    TextView textViewMP;
    @BindView(R.id.goals)
    TextView textViewGoals;
    @BindView(R.id.pts)
    TextView textViewPts;

    private Context context;
    private List<Group> groups;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Gson gson;

    public GroupQualifierAdapter(Context context) {
        this.context = context;
        groups = new ArrayList<>();
        gson = new Gson();
        initImageLoader();

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

    public void setGroups(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @Override
    public Team getChild(int listPosition, int expandedListPosition) {
        return this.groups.get(listPosition).getTeams().get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Team team = getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_list_view, null);
        }
        ButterKnife.bind(this, convertView);

        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = gson.toJson(team);
                context.startActivity(TeamActivity.createIntent(context, json));
            }
        });

        imageLoader.displayImage(team.getAvatar(), imageView, options);
        textViewName.setText(team.getName());
        textViewGoals.setText(team.getGoals());
        textViewMP.setText(team.getMp());
        textViewPts.setText(team.getPts());

        return convertView;
    }

    /*
    So luong doi bong
     */
    @Override
    public int getChildrenCount(int listPosition) {
        return groups.get(listPosition).getTeams().size();
    }

    @Override
    public Group getGroup(int listPosition) {
        return this.groups.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = getGroup(listPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_header, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.header_text);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(group.getGroupName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}