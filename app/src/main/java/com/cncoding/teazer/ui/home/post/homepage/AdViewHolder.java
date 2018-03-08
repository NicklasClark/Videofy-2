package com.cncoding.teazer.ui.home.post.homepage;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.model.post.AdFeedItem;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.inmobi.ads.InMobiNative;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 *
 * Created by amit on 14/2/18.
 */

public class AdViewHolder extends BaseRecyclerView.ViewHolder {

    @LayoutRes
    public static final int LAYOUT_RES = R.layout.recycler_card_layout;
    private PostsListAdapter postsListAdapter;
    private PostDetails postDetails;
    CardView cardView;
    View adView;

    ImageView adIcon;
    ProximaNovaSemiBoldTextView adTitle, adDescription;
    ProximaNovaRegularTextView adSponsored;
    Button adAction;
    FrameLayout adContent;
    RatingBar adRating;


    public AdViewHolder(PostsListAdapter postsListAdapter, View adCardView) {
        super(adCardView);

        ButterKnife.bind(this, adCardView);
        this.postsListAdapter = postsListAdapter;
        cardView = (CardView) adCardView;
        adView = LayoutInflater.from(postsListAdapter.fragment.context).inflate(R.layout.layout_ad, null);

        adIcon = adView.findViewById(R.id.adIcon);
        adTitle = adView.findViewById(R.id.adTitle);
        adDescription = adView.findViewById(R.id.adDescription);
        adAction = adView.findViewById(R.id.adAction);
        adContent = adView.findViewById(R.id.adContent);
        adRating = adView.findViewById(R.id.adRating);
        adSponsored = adView.findViewById(R.id.adSponsored);
    }

    @Override
    public void bind() {
        postDetails = postsListAdapter.posts.get(getAdapterPosition());
        if (postDetails instanceof AdFeedItem) {
            final InMobiNative inMobiNative = ((AdFeedItem) postDetails).mNativeStrand;

            cardView.removeAllViews();
            adContent.removeAllViews();

            JSONObject customContent = inMobiNative.getCustomAdContent();
            boolean isBackFillBanner = false;
            try {
                isBackFillBanner = customContent.getBoolean("isHTMLResponse");
            } catch (JSONException e) {
                isBackFillBanner = false;
            }

            Glide.with(postsListAdapter.fragment.context)
                    .load(inMobiNative.getAdIconUrl())
                    .into(adIcon);
            adTitle.setText(inMobiNative.getAdTitle());
            adDescription.setText(inMobiNative.getAdDescription());
            adAction.setText(inMobiNative.getAdCtaText());

            if(isBackFillBanner)
            {
                adContent.addView(inMobiNative.getPrimaryViewOfWidth(postsListAdapter.fragment.context, adView, cardView, Math.round(postsListAdapter.fragment.context.getResources().getDisplayMetrics().density*250)));
                adAction.setVisibility(View.GONE);
            }
            else
            {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) postsListAdapter.fragment.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                adContent.addView(inMobiNative.getPrimaryViewOfWidth(postsListAdapter.fragment.context , adView,
                        cardView, cardView.getWidth()));
            }

            float rating  = inMobiNative.getAdRating();
//            if (rating != 0) {
//                adRating.setRating(rating);
//            }
//            adRating.setVisibility(rating != 0 ? View.VISIBLE : View.GONE);

            adAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inMobiNative.reportAdClickAndOpenLandingPage();
                }
            });

            cardView.addView(adView);
            //Detect whether its a backfill response or normal native
//            JSONObject customContent = inMobiNative.getCustomAdContent();
//            boolean isBackFillBanner = false;
//            try {
//                isBackFillBanner = customContent.getBoolean("isHTMLResponse");
//            } catch (JSONException e) {
//                isBackFillBanner = false;
//            }

//            if(isBackFillBanner){
//                View primaryView = inMobiNative.getPrimaryViewOfWidth(postsListAdapter.context, adView, cardView, Math.round(postsListAdapter.context.getResources().getDisplayMetrics().density*250));
//                if(adView==null){
//                    adView = LayoutInflater.from(postsListAdapter.context).inflate(R.layout.layout_ad, null);
//                }
//                adContent.addView(primaryView);
//            }
//            else {
//                View primaryView = inMobiNative.getPrimaryViewOfWidth(postsListAdapter.context, adView, cardView, cardView.getWidth());
//                if(adView==null){
//                    adView = LayoutInflater.from(postsListAdapter.context).inflate(R.layout.layout_ad, cardView, false);
//                }
//                adContent.addView(primaryView);
//                adTitle.setText(inMobiNative.getAdTitle());
//                adDescription.setText(inMobiNative.getAdDescription());
//                adAction.setText(inMobiNative.getAdCtaText());
//            }
        }
    }
}