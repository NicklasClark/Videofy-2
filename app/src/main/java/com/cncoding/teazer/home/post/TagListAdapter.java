package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.base.TaggedUser;

import java.io.Serializable;
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
    private TaggedListInteractionListener mListener;

    TagListAdapter(Context context, ArrayList<TaggedUser> taggedUserList) {
        this.context = context;
        this.taggedUserList = taggedUserList;
        if (context instanceof TaggedListInteractionListener)
            mListener = (TaggedListInteractionListener) context;
    }

    @Override
    public TaggedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tagged_user, viewGroup, false);
        return new TaggedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaggedViewHolder holder, int i) {
        Glide.with(context)
                .load(taggedUserList.get(i).hasProfileMedia() ? taggedUserList.get(i).getProfileMedia().getThumbUrl() :
                        R.drawable.ic_user_male_dp)
                .placeholder(R.drawable.ic_user_male_dp)
                .crossFade()
                .listener(new RequestListener<Serializable, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Serializable model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Serializable model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.dp.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.dp);

        final int userId = taggedUserList.get(i).getUserId();
        final boolean isSelf = taggedUserList.get(i).isMySelf();
        holder.dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onTaggedUserInteraction(userId, isSelf);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taggedUserList.size();
    }

    class TaggedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tagged_user_dp) CircularAppCompatImageView dp;

        TaggedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface TaggedListInteractionListener {
        void onTaggedUserInteraction(int userId, boolean isSelf);
    }
}