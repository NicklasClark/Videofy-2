package com.cncoding.teazer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.post.FragmentLikedUser;
import com.cncoding.teazer.home.post.PostDetailsActivity;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.post.LikedUser;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.user.BlockedUser;
import com.cncoding.teazer.ui.fragment.activity.BlockUserList;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 02/01/18.
 */

public class LikedUserAdapter extends RecyclerView.Adapter<LikedUserAdapter.ViewHolder> {

    private List<RecyclerView.LayoutManager> layoutManager;
    List<LikedUser> list;
    private Context context;
    public static final int UNBLOCK_STATUS=2;
    public static boolean isLikedUser=false;
    PostDetails postDetails;
    Fragment fragment;

    public LikedUserAdapter(Context context, List<LikedUser> list, PostDetails postDetails,Fragment fragment) {
        this.context = context;
        this.list = list;
        this.postDetails = postDetails;
        this.fragment = fragment;
    }

    @Override
    public LikedUserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_followers, viewGroup, false);
        return new LikedUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikedUserAdapter.ViewHolder viewHolder, int i) {
        final LikedUser cont = list.get(i);
        try {

            final String likedUsername = cont.getUserName();
            final boolean islikedUserDp = cont.getHasProfileMedia();

            if (islikedUserDp) {
                String LikedUserDp = cont.getProfileMedia().getMediaUrl();

                Glide.with(context)
                        .load(LikedUserDp)
                        .skipMemoryCache(false)
                        .into(viewHolder.dp);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ic_user_male_dp_small)
                        .fit().centerInside()
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(viewHolder.dp);
            }

            final int userId = cont.getUserId();
            final boolean ismyself=cont.getMySelf();
            viewHolder.username.setText(likedUsername);
            viewHolder.action.setVisibility(View.GONE);

            viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((FragmentLikedUser)fragment).callFragmentLikedUser(userId,ismyself);


                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        LinearLayout cardview;
        CircularAppCompatImageView dp;

        View line;
        ProximaNovaSemiboldTextView action;
        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.name);
            action = view.findViewById(R.id.action);
            cardview = view.findViewById(R.id.root_layout);
            dp = view.findViewById(R.id.dp);

        }
    }

    public interface ProfileListener
    {
        public void openprofile(int userId,boolean ismyself);

    }
}
