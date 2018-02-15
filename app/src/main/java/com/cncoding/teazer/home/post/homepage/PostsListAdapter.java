package com.cncoding.teazer.home.post.homepage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.home.BaseRecyclerViewAdapter;
import com.cncoding.teazer.home.BaseRecyclerViewHolder;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.utilities.diffutil.PostsDiffCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends BaseRecyclerViewAdapter {

    protected OnPostAdapterInteractionListener listener;
    ArrayList<PostDetails> posts;
    protected Context context;
    private RecyclerView recyclerView;

    PostsListAdapter(Context context) {
        this.context = context;
        if (posts == null) {
            posts = new ArrayList<>();
        }
        if (context instanceof OnPostAdapterInteractionListener) {
            listener = (OnPostAdapterInteractionListener) context;
        }
    }

    @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (this.recyclerView == null) {
            this.recyclerView = recyclerView;
        }
    }

    @Override public PostListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(PostListViewHolder.LAYOUT_RES, parent, false);
        return new PostListViewHolder(this, view);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override public int getItemCount() {
        return posts.size();
    }

    @Override public void release() {
//        clearData();
//        posts = null;
        listener = null;
        context = null;
        recyclerView = null;
    }

    void addPosts(int page, List<PostDetails> postDetailsList) {
        try {
            if (page == 1) {
                clearData();
                posts.addAll(postDetailsList);
                notifyDataSetChanged();
                if (recyclerView != null) {
                    recyclerView.smoothScrollBy(0, 1);
                    recyclerView.smoothScrollBy(0, -1);
                }
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
//        posts.addAll(0, postDetailsList);
            result.dispatchUpdatesTo(this);
        } catch (Exception e) {
            e.printStackTrace();
            if (posts == null) posts = new ArrayList<>();
            posts.addAll(postDetailsList);
            notifyDataSetChanged();
            if (recyclerView != null) {
                recyclerView.smoothScrollBy(0, 1);
                recyclerView.smoothScrollBy(0, -1);
            }
        }
    }

    private void clearData() {
        if (posts != null) {
            posts.clear();
        }
    }

    public interface OnPostAdapterInteractionListener {
        void onPostInteraction(int action, PostDetails postDetails);
        void postDetails(PostDetails postDetails, Bitmap image, boolean isComingFromHomePage,
                         boolean isDeepLink, String getThumbUrl, String reactId);
    }
}