package com.cncoding.teazer.home.post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.post.PostDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.ViewUtils.getPixels;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends RecyclerView.Adapter<PostListViewHolder> {

    private SparseIntArray colorArray;
    protected OnPostAdapterInteractionListener listener;
    ArrayList<PostDetails> posts;
    protected Context context;
    boolean isPostClicked = false;
    boolean isMuted;
    private RecyclerView recyclerView;

    PostsListAdapter(ArrayList<PostDetails> posts, Context context) {
        this.posts = posts;
        this.context = context;
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

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public PostListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(PostListViewHolder.LAYOUT_RES, parent, false);
        return new PostListViewHolder(this, view);
    }

    @Override
    public void onBindViewHolder(final PostListViewHolder holder, int position) {
        holder.bind(position);
    }

//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position, List<Object> payloads) {
//        try {
//            if ((payloads != null && payloads.isEmpty()) || payloads == null) {
//
//            }
//            else {
//                Bundle bundle = (Bundle) payloads.get(0);
//                for (String key : bundle.keySet()) {
//                    switch (key) {
//                        case POST_DETAILS:
//                            posts.remove(position);
//                            posts.add(position, (PostDetails) bundle.getParcelable(POST_DETAILS));
//                            return;
//                        case LIKES:
//                            posts.get(position).likes = bundle.getInt(LIKES);
//                            break;
//                        case TOTAL_REACTIONS:
//                            posts.get(position).total_reactions = bundle.getInt(TOTAL_REACTIONS);
//                            break;
//                        case TOTAL_TAGS:
//                            posts.get(position).total_tags = bundle.getInt(TOTAL_TAGS);
//                            break;
//                        case HAS_CHECKIN:
//                            posts.get(position).has_checkin = bundle.getBoolean(HAS_CHECKIN);
//                            break;
//                        case TITLE:
//                            posts.get(position).title = bundle.getString(TITLE);
//                            break;
//                        case CAN_REACT:
//                            posts.get(position).canReact = bundle.getBoolean(CAN_REACT);
//                            break;
//                        case CAN_LIKE:
//                            posts.get(position).canLike = bundle.getBoolean(CAN_LIKE);
//                            break;
//                        case CHECKIN:
//                            posts.get(position).check_in = bundle.getParcelable(CHECKIN);
//                            break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    GradientDrawable getBackground(TextView title, int position, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (colorArray.get(position) == 0) {
            colorArray.put(position, color);
        }
        gradientDrawable.setColor(colorArray.get(position));
        gradientDrawable.setCornerRadius(getPixels(context, 2));
        title.setTextColor(Color.WHITE);
        return gradientDrawable;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface OnPostAdapterInteractionListener {
        void onPostInteraction(int action, PostDetails postDetails);
        void postDetails(PostDetails postDetails, Bitmap image, boolean isComingFromHomePage,
                         boolean isDeepLink, String getThumbUrl, String reactId);
    }

    public void release() {
        colorArray.clear();
        colorArray = null;
        posts.clear();
        listener = null;
        context = null;
        recyclerView = null;
    }

    void fetchPostDetails(int postId, final int adapterPosition, final Bitmap thumbnail) {
        ApiCallingService.Posts.getPostDetails(postId, context)
                .enqueue(new Callback<PostDetails>() {
                    @Override
                    public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                PostsListFragment.positionToUpdate = adapterPosition;
                                PostsListFragment.postDetails = response.body();
//                                listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail, holder.layout);

                                listener.postDetails(response.body(), thumbnail, true,
                                        false, response.body().getMedias().get(0).getThumbUrl(), null);
                            } else {
                                Toast.makeText(context, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<PostDetails> call, Throwable t) {
                        Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}