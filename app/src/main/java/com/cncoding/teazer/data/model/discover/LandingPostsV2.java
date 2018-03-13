package com.cncoding.teazer.data.model.discover;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class LandingPostsV2 extends BaseModel {

    @SerializedName("featured_videos") @Expose private ArrayList<PostDetails> featuredVideos;
    @SerializedName("user_interests") @Expose private ArrayList<Category> userInterests;
    @SerializedName("trending_categories") @Expose private ArrayList<Category> trendingCategories;
    @SerializedName("my_interests") @Expose private Map<String, ArrayList<PostDetails>> myInterests;

    public LandingPostsV2(ArrayList<PostDetails> featuredVideos, ArrayList<Category> userInterests,
                          ArrayList<Category> trendingCategories, Map<String, ArrayList<PostDetails>> myInterests) {
        this.featuredVideos = featuredVideos;
        this.userInterests = userInterests;
        this.trendingCategories = trendingCategories;
        this.myInterests = myInterests;
    }

    public LandingPostsV2(Throwable error) {
        this.error = error;
    }

    public LandingPostsV2 setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public ArrayList<PostDetails> getFeaturedVideos() {
        return featuredVideos;
    }

    public ArrayList<Category> getUserInterests() {
        return userInterests;
    }

    public ArrayList<Category> getTrendingCategories() {
        return trendingCategories;
    }

    public Map<String, ArrayList<PostDetails>> getMyInterests() {
        return myInterests;
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

    public void setUserInterests(ArrayList<Category> userInterests) {
        this.userInterests = userInterests;
    }

    public boolean isEmpty() {
        return getFeaturedVideos().isEmpty() &&
                (getMyInterests().isEmpty() ||
                        Collections.frequency(getMyInterests().values(), Collections.EMPTY_LIST) == getMyInterests().size());
    }
}