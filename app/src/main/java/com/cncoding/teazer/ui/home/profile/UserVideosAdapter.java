package com.cncoding.teazer.ui.home.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;

/**
 *
 * Created by Prem $ on 10/31/2017.
 */

public class UserVideosAdapter extends RecyclerView.Adapter<UserVideosAdapter.UserVideosHolder> {

    private final int numItems;

    UserVideosAdapter(int numItems) {
        this.numItems = numItems;
    }

    @Override
    public UserVideosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_videos, parent, false);
        return new UserVideosHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserVideosHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    static class UserVideosHolder extends RecyclerView.ViewHolder {
        UserVideosHolder(View itemView) {
            super(itemView);
        }
    }
}
