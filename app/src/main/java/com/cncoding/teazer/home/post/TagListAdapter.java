package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.utilities.Pojos.TaggedUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/17/2017.
 */

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TaggedViewHolder> {

    private Context context;
    private ArrayList<TaggedUser> taggedUserList;

    TagListAdapter(Context context, ArrayList<TaggedUser> taggedUserList) {
        this.context = context;
        this.taggedUserList = taggedUserList;
    }

    @Override
    public TaggedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tagged_user, viewGroup, false);
        return new TaggedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaggedViewHolder holder, int i) {
        holder.username.setText("@" + taggedUserList.get(i).getUserName());
        Glide.with(context)
                .load(taggedUserList.get(i).getProfileMedia().getThumbUrl())
                .crossFade()
                .into(holder.dp);
    }

    @Override
    public int getItemCount() {
        return taggedUserList.size();
    }

    class TaggedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tagged_user_dp) CircularAppCompatImageView dp;
        @BindView(R.id.tagged_user_name) ProximaNovaRegularTextView username;

        TaggedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}