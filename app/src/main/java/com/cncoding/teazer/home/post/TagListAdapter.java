package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.base.TaggedUser;

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
    Fragment fragment;

    TagListAdapter(Context context, ArrayList<TaggedUser> taggedUserList, Fragment fragment) {
        this.context = context;
        this.taggedUserList = taggedUserList;
        this.fragment=fragment;

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

        boolean imageURL = taggedUserList.get(i).hasProfileMedia();

        Log.d("TagListAdapter", "onBindViewHolder: imageurl"+imageURL);
        Log.d("TagListAdapter", "onBindViewHolder: profile media"+taggedUserList.get(i).toString());
        if(imageURL) {
            Log.d("TagListAdapter", "onBindViewHolder: image url found");
            String image = taggedUserList.get(i).getProfileMedia().getThumbUrl();
            if(image != null) {
                Log.d("TagListAdapter", "onBindViewHolder: image not null");
                Glide.with(context)
                        .load(image)
                        .apply(new RequestOptions()
                                .centerInside()
                                .placeholder(R.drawable.ic_user_male_dp_small)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(holder.dp);
            }  else {
                Log.d("TagListAdapter", "onBindViewHolder: image null");
                Glide.with(context)
                        .load(R.drawable.ic_user_male_dp_small)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(holder.dp);
            }
       }
       else {
           Log.d("TagListAdapter", "onBindViewHolder: image url not found");
           Glide.with(context)
                   .load(R.drawable.ic_user_male_dp_small)
                   .apply(new RequestOptions()
                           .diskCacheStrategy(DiskCacheStrategy.NONE))
                   .into(holder.dp);
       }

        final int userId = taggedUserList.get(i).getUserId();
        final boolean isSelf = taggedUserList.get(i).isMySelf();
        holder.dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentPostDetails)fragment).callUserProfile(userId,isSelf);
//                if (mListener != null)
//                    mListener.onTaggedUserInteraction(userId, isSelf);
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