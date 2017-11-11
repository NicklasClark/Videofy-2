package com.cncoding.teazer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.model.profile.following.Following;

import java.util.List;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<Following> list;
    private Context context;

    public FollowingAdapter(Context context, List<Following> list) {
        this.context = context;
        this.list =list;
    }
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_following, viewGroup, false);
        return new FollowingAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final FollowingAdapter.ViewHolder viewHolder, int i) {
        Following cont = list.get(i);
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
