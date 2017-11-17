package com.cncoding.teazer.home.post;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.utilities.Pojos.TaggedUser;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 11/17/2017.
 */

public class TagListAdapter extends BaseAdapter {

    private ArrayList<TaggedUser> taggedUserList;

    public TagListAdapter(ArrayList<TaggedUser> taggedUserList) {
        this.taggedUserList = taggedUserList;
    }

    @Override
    public int getCount() {
        return taggedUserList.size();
    }

    @Override
    public Object getItem(int i) {
        return taggedUserList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TaggedViewHolder holder;
        if (view == null) {
            holder = new TaggedViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tagged_user, null);
            holder.dp = view.findViewById(R.id.tagged_user_dp);
            holder.username = view.findViewById(R.id.tagged_user_name);
        }
        else {
            holder = (TaggedViewHolder) view.getTag();
        }
        return view;
    }

    static class TaggedViewHolder {
        CircularAppCompatImageView dp;
        ProximaNovaRegularTextView username;
    }
}