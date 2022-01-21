package com.vinayakmix.mixifyy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;

import java.util.ArrayList;

public class ContentListAdapter extends
        RecyclerView.Adapter<ContentListAdapter.MyContentView> {

    ArrayList<ContentData> contentDataArrayList;
    Activity activity;
    int type;

    public static class MyContentView extends RecyclerView.ViewHolder {

        public ImageView imgContentThumb, imgPlayGame, imgPlayVideo;

        public MyContentView(View view) {
            super(view);

            imgContentThumb = view.findViewById(R.id.imgContentThumb);
            imgPlayGame = view.findViewById(R.id.imgPlayGame);
            imgPlayVideo = view.findViewById(R.id.imgPlayVideo);
        }
    }

    public ContentListAdapter(Activity activity, ArrayList<ContentData> contentDataArrayList,
                              int type) {
        this.activity = activity;
        this.contentDataArrayList = contentDataArrayList;
        this.type = type;

    }

    @NonNull
    @Override
    public MyContentView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content_list, parent, false);
        return new MyContentView(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final MyContentView holder, final int position) {

        try {
            final String imageUrl = contentDataArrayList.get(position).getThumbnail();

            Glide.with(activity)
                    .asBitmap()
                    .load(new GlideUrl(imageUrl))
                    .error(R.drawable.ic_not_image)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(holder.imgContentThumb);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (type == 0) {
            holder.imgPlayGame.setVisibility(View.VISIBLE);
            holder.imgPlayVideo.setVisibility(View.GONE);
        } else {
            holder.imgPlayGame.setVisibility(View.GONE);
            holder.imgPlayVideo.setVisibility(View.VISIBLE);
        }


        holder.imgPlayGame.setOnClickListener(view -> {
            if (AppConst.isNetworkAvailable(activity)) {
                AppConst.mixifyyInterstitialAds(activity);
                Intent intent = new Intent(activity, PlayViewActivity.class);
                intent.putExtra("playlink",
                        contentDataArrayList.get(position).getContent());
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity,
                        activity.getString(R.string.no_internet_connection) + "",
                        Toast.LENGTH_LONG).show();
            }

        });

        holder.imgPlayVideo.setOnClickListener(view -> {
            if (AppConst.isNetworkAvailable(activity)) {
                AppConst.mixifyyInterstitialAds(activity);
                Intent intent = new Intent(activity, PlayViewActivity.class);
                intent.putExtra("playlink",
                        contentDataArrayList.get(position).getContent());
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity,
                        activity.getString(R.string.no_internet_connection) + "",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentDataArrayList.size();
    }

}
