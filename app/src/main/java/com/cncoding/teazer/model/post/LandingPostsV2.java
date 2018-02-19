package com.cncoding.teazer.model.post;

import com.cncoding.teazer.model.base.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Collections;
import java.util.Map;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class LandingPostsV2 {

    @SerializedName("featured_videos") @Expose private List<PostDetails> featuredVideos;
    @SerializedName("user_interests") @Expose private List<Category> userInterests;
    @SerializedName("trending_categories") @Expose private List<Category> trendingCategories;
    @SerializedName("my_interests") @Expose private Map<String, List<PostDetails>> myInterests;
    private Throwable error;

    public LandingPostsV2(List<PostDetails> featuredVideos, List<Category> userInterests,
                          List<Category> trendingCategories, Map<String, List<PostDetails>> myInterests) {
        this.featuredVideos = featuredVideos;
        this.userInterests = userInterests;
        this.trendingCategories = trendingCategories;
        this.myInterests = myInterests;
    }

    public LandingPostsV2(Throwable error) {
        this.error = error;
    }

    public void clearData() {
        if (featuredVideos != null)
            featuredVideos.clear();
        if (userInterests != null)
            userInterests.clear();
        if (trendingCategories != null)
            trendingCategories.clear();
        if (myInterests != null) {
            myInterests.clear();
        }
    }

//            public boolean areListsEmpty() {
//                return featuredVideos.isEmpty() && userInterests.isEmpty() && trendingCategories.isEmpty() &&
//                        Collections.frequency(myInterests.values(), Collections.EMPTY_LIST) == myInterests.size();
//            }

    public List<PostDetails> getFeaturedVideos() {
        return featuredVideos;
    }

    public List<Category> getUserInterests() {
        return userInterests;
    }

    public List<Category> getTrendingCategories() {
        return trendingCategories;
    }

    public Map<String, List<PostDetails>> getMyInterests() {
        return myInterests;
    }

    public boolean isEmpty() {
        return getFeaturedVideos().isEmpty() &&
                (getMyInterests().isEmpty() ||
                        Collections.frequency(getMyInterests().values(), Collections.EMPTY_LIST) == getMyInterests().size());
    }
}