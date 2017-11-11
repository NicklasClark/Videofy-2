package com.cncoding.teazer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.model.profile.followers.Follower;

import java.util.List;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private List<Follower> list;
    private Context context;

    public FollowersAdapter(Context context, List<Follower> list) {
        this.context = context;
        this.list =list;
    }
    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_followers, viewGroup, false);
        return new FollowersAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final FollowersAdapter.ViewHolder viewHolder, int i) {
          Follower  cont = list.get(i);
          String username=cont.getUserName();
          viewHolder.followersname.setText(username);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView followersname, address;
        Button follow;

        public ViewHolder(View view) {
            super(view);
            followersname = view.findViewById(R.id.followers_name);


        }
    }
}
