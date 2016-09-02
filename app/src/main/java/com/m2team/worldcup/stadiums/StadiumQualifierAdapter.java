package com.m2team.worldcup.stadiums;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.m2team.worldcup.R;
import com.m2team.worldcup.model.Stadium;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StadiumQualifierAdapter extends RecyclerView.Adapter<StadiumQualifierAdapter.ViewHolder> {

    private Context context;
    private List<Stadium> dataList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Gson gson;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_team)
        TextView tv_name;
        @BindView(R.id.img_background)
        ImageView img_background;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public StadiumQualifierAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
        gson = new Gson();
        initImageLoader();

    }

    public void setData(List<Stadium> list) {
        this.dataList = list;
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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_each_stadium, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Stadium stadium = dataList.get(position);
        holder.tv_name.setText(stadium.name);
        imageLoader.displayImage(stadium.background, holder.img_background);

        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(StadiumDetailActivity.createIntent(context, stadium.link, stadium.name));
            }
        });
        holder.img_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(StadiumDetailActivity.createIntent(context, stadium.link, stadium.name));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}