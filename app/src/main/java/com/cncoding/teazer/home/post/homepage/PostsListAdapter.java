package com.cncoding.teazer.home.post.homepage;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.home.BaseRecyclerView;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.utilities.diffutil.PostsDetailsDiffCallback;

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

    void addPosts(final int page, final List<PostDetails> postDetailsList) {
        try {
            if (page == 1) {
                clearData();
                posts.addAll(postDetailsList);
                notifyDataChanged();
            } else {
                posts.addAll(postDetailsList);
                fragment.getParentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRangeInserted((page - 1) * 30, postDetailsList.size());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateNewPosts(final List<PostDetails> postDetailsList) {
        try {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                    new PostsDetailsDiffCallback(new ArrayList<>(posts.subList(0, 30)), postDetailsList));
            clearData();
            posts.addAll(postDetailsList);
            fragment.getParentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.dispatchUpdatesTo(PostsListAdapter.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            addPosts(1, postDetailsList);
        }
    }

    private void clearData() {
        if (posts != null) {
            posts.clear();
        }
    }
}