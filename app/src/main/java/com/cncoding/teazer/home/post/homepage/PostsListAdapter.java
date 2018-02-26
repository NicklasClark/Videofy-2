package com.cncoding.teazer.home.post.homepage;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.home.BaseRecyclerView;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.utilities.diffutil.PostsDiffCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails}
 */
public class PostsListAdapter extends BaseRecyclerView.Adapter {

    List<PostDetails> posts;
    protected PostsListFragment fragment;

    PostsListAdapter(PostsListFragment fragment) {
        this.fragment = fragment;
        if (posts == null) {
            posts = new ArrayList<>();
        }
    }

    @Override public PostListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(PostListViewHolder.LAYOUT_RES, parent, false);
        return new PostListViewHolder(this, view);
    }

    @Override public int getItemCount() {
        return posts.size();
    }

    @Override public void release() {
        fragment = null;
    }

    @Override
    public void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    void addPosts(int page, List<PostDetails> postDetailsList) {
        try {
            if (page == 1) {
                clearData();
                posts.addAll(postDetailsList);
                notifyDataSetChanged();
            } else {
                posts.addAll(postDetailsList);
                notifyItemRangeInserted((page - 1) * 30, postDetailsList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateNewPosts(List<PostDetails> postDetailsList) {
        try {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new PostsDiffCallback(new ArrayList<>(posts.subList(0, 30)), postDetailsList));
            clearData();
            posts.addAll(postDetailsList);
            result.dispatchUpdatesTo(this);
        } catch (Exception e) {
            e.printStackTrace();
            if (posts == null) posts = new ArrayList<>();
            posts.addAll(postDetailsList);
            notifyDataSetChanged();
        }
    }

    private void clearData() {
        if (posts != null) {
            posts.clear();
        }
    }
}