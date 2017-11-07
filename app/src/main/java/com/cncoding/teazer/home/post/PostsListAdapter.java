package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.ViewHolder> {

    private final String[] categories = new String[] {
            "Singing","Dancing","Movies","Adventure","Instruments","Comedy","Travel","Videography","Acting","Technology","iOS","Android"
        ,"Apple","Fashion","Lifestyle","Sports","Restaurants","Wildlife","Nightlife","Photography","Love","Health And Fitness","History"
        ,"Home Décor","Humour","Kids And Parenting","Men's Fashion","Outdoors","Photography","Quotes","Science","Nature","Sports","Tattoos"
        ,"Technology","Travel","Weddings","Women's Fashion","Popular","Everything","Animals And Pets","Architecture","Art"
        ,"Cars And Motorcycles","Celebrations And Events","Celebrities","DIY And Crafts","Design","Education","Entertainment"
        ,"Food And Drink","Gardening","Geek","Hair And Beauty"
    };

    private OnPostAdapterInteractionListener listener;
    private final List<PostDetails> posts;
    private Context context;

    PostsListAdapter(List<PostDetails> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_screen_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostDetails postDetails = posts.get(position);
        Pojos.MiniProfile postOwner = postDetails.getPostOwner();

        Glide.with(context)
                .load(postDetails.getMedias().get(0).getThumbUrl())
//                .apply(RequestOptions.circleCropTransform())
//                .apply(RequestOptions.bitmapTransform(new ColorFilterTransformation(PlaceHolderDrawableHelper.getColor())))
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                .crossFade()
//                .animate(android.R.anim.fade_in)
                .into(holder.postThumbnail);

        if (postOwner.hasProfileMedia())
            Glide.with(context)
                    .load(postOwner.getProfileMedia().getThumbUrl())
//                    .apply(RequestOptions.circleCropTransform())
//                    .apply(RequestOptions.bitmapTransform(new ColorFilterTransformation(PlaceHolderDrawableHelper.getColor())))
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                    .crossFade()
//                    .animate(R.anim.zoom_in)
                    .into(holder.profilePic);

        holder.caption.setText(postDetails.getTitle());
        if (postDetails.getCategories().size() > 0)
            holder.category.setText(postDetails.getCategories().get(0).getCategoryName());
        else
            holder.category.setText(categories[new Random().nextInt(categories.length - 1)]);
//        holder.name.setText(postOwner.getFirstName() + " " + postOwner.getLastName());
        holder.name.setText("@" + postOwner.getUserName());
        holder.popularity.setText(postDetails.getLikes() + " Likes | " + postDetails.getTotalReactions() + " Reactions");

        if (listener != null) {
            View.OnClickListener viewPostDetails = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostInteraction(ACTION_VIEW_POST, postDetails);
                }
            };
            View.OnClickListener viewProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostInteraction(ACTION_VIEW_PROFILE, postDetails);
                }
            };

            holder.postThumbnail.setOnClickListener(viewPostDetails);
            holder.profilePic.setOnClickListener(viewProfile);
            holder.name.setOnClickListener(viewProfile);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.home_screen_post_layout) RelativeLayout layout;
        @BindView(R.id.home_screen_post_thumb) ImageView postThumbnail;
        @BindView(R.id.home_screen_post_caption) ProximaNovaSemiboldTextView caption;
        @BindView(R.id.home_screen_post_category) ProximaNovaRegularTextView category;
        @BindView(R.id.home_screen_post_dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.home_screen_post_username) ProximaNovaSemiboldTextView name;
        @BindView(R.id.home_screen_post_popularity) ProximaNovaRegularTextView popularity;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "' : \"" + caption.getText() + "\"";
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (context instanceof OnPostAdapterInteractionListener) {
            listener = (OnPostAdapterInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserAdapterInteractionListener");
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listener = null;
    }

    public interface OnPostAdapterInteractionListener {
        void onPostInteraction(int action, PostDetails postDetails);
    }
}
