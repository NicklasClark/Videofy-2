package com.cncoding.teazer.home.post.homepage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.home.BaseRecyclerViewAdapter;
import com.cncoding.teazer.home.BaseRecyclerViewHolder;
import com.cncoding.teazer.model.post.AdFeedItem;
import com.cncoding.teazer.model.post.PostDetails;

import java.util.ArrayList;
import java.util.List;

import static com.cncoding.teazer.utilities.ViewUtils.getPixels;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends BaseRecyclerViewAdapter {

    private SparseIntArray colorArray;
    protected OnPostAdapterInteractionListener listener;
    ArrayList<PostDetails> posts;
    protected Context context;
    //    boolean isPostClicked = false;
    private RecyclerView recyclerView;
    private final int POST = 0, AD = 1;

    PostsListAdapter(Context context) {
        this.context = context;
        if (posts == null) {
            posts = new ArrayList<>();
        }
        colorArray = new SparseIntArray();
        if (context instanceof OnPostAdapterInteractionListener) {
            listener = (OnPostAdapterInteractionListener) context;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (this.recyclerView == null) {
            this.recyclerView = recyclerView;
        }
    }

    void addPosts(int page, List<PostDetails> postDetailsList) {
        try {
            if (page == 1) {
                posts.clear();
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

    void addInMobiPost(int page, AdFeedItem postDetailsList, int adPosition) {
        try {
            if (page == 1) {
                posts.clear();
                posts.add(adPosition, postDetailsList);
                notifyDataSetChanged();
                if (recyclerView != null) {
                    recyclerView.smoothScrollBy(0, 1);
                    recyclerView.smoothScrollBy(0, -1);
                }
            } else {
                posts.add(adPosition, postDetailsList);
//                notifyItemRangeInserted((page - 1) *
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void clear() {
        posts.clear();
    }

    @Override
    public PostListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(PostListViewHolder.LAYOUT_RES, parent, false);
        switch (viewType)
        {
            case POST:
                return new PostListViewHolder(this, view);
            case AD:
                return new PostListViewHolder(this, view);
            default:
                return new PostListViewHolder(this, view);
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void release() {
        colorArray.clear();
        colorArray = null;
        posts.clear();
        listener = null;
        context = null;
        recyclerView = null;
    }

    GradientDrawable getBackground(ColorStateList color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
//        if (colorArray.get(position) == 0) {
//            colorArray.put(position, color);
//        }
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(getPixels(context, 2));
        return gradientDrawable;
    }

    public interface OnPostAdapterInteractionListener {
        void onPostInteraction(int action, PostDetails postDetails);
        void postDetails(PostDetails postDetails, Bitmap image, boolean isComingFromHomePage,
                         boolean isDeepLink, String getThumbUrl, String reactId);
    }

    @Override
    public int getItemViewType(int position) {
        if (posts.get(position) instanceof PostDetails) {
            return POST;
        } else if (posts.get(position) instanceof AdFeedItem) {
            return AD;
        }
        return -1;
    }
}