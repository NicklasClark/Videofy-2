package com.cncoding.teazer.home.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.Pojos.Discover.MyInterests;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.customViews.MediaControllerView.SPACE;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListItemAdapter extends RecyclerView.Adapter<MyInterestsListItemAdapter.ViewHolder> {

    private ArrayList<MyInterests> myInterestsList;
    private Context context;

    MyInterestsListItemAdapter(ArrayList<MyInterests> myInterestsList, Context context) {
        this.myInterestsList = myInterestsList;
        this.context = context;
    }

    @Override
    public MyInterestsListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_interests_list_item, parent, false);
        return new MyInterestsListItemAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyInterestsListItemAdapter.ViewHolder holder, int position) {
        holder.myInterests = myInterestsList.get(position);

        holder.title.setText(holder.myInterests.getTitle());
        holder.name.setText(holder.myInterests.getName());
        holder.likes.setText(SPACE + String.valueOf(holder.myInterests.getLikes()));
        holder.views.setText(SPACE + String.valueOf(holder.myInterests.getViews()));
        holder.reactions.setText(SPACE + String.valueOf(holder.myInterests.getReactions()) + " reactions");

        if (holder.myInterests.getThumbUrl().contains(".gif")) {
            Glide.with(context)
                    .load(holder.myInterests.getThumbUrl())
                    .asGif()
                    .crossFade()
                    .into(holder.thumbnail);
        } else
            Glide.with(context)
                    .load(holder.myInterests.getThumbUrl())
                    .crossFade()
                    .into(holder.thumbnail);

        Glide.with(context)
                .load(holder.myInterests.getProfileThumbUrl())
                .crossFade()
                .into(holder.dp);

        Glide.with(context)
                .load(holder.myInterests.getReaction1Url())
                .crossFade()
                .into(holder.reactionImage1);

        Glide.with(context)
                .load(holder.myInterests.getReaction2Url())
                .crossFade()
                .into(holder.reactionImage2);

        Glide.with(context)
                .load(holder.myInterests.getReaction3Url())
                .crossFade()
                .into(holder.reactionImage3);
    }

    @Override
    public int getItemCount() {
        return myInterestsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) ProximaNovaSemiboldTextView title;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_count) ProximaNovaRegularTextView reactions;
        @BindView(R.id.video_thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.reaction_1) CircularAppCompatImageView reactionImage1;
        @BindView(R.id.reaction_2) CircularAppCompatImageView reactionImage2;
        @BindView(R.id.reaction_3) CircularAppCompatImageView reactionImage3;
        MyInterests myInterests;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}