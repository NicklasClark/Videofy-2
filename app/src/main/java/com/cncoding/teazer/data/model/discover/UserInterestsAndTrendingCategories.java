package com.cncoding.teazer.data.model.discover;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by Prem$ on 3/12/2018.
 */

public class UserInterestsAndTrendingCategories extends BaseModel {

    @SerializedName("user_interests") @Expose private List<Category> userInterests;
    @SerializedName("trending_categories") @Expose private List<Category> trendingCategories;

    public UserInterestsAndTrendingCategories(Throwable error) {
        this.error = error;
    }

    public UserInterestsAndTrendingCategories setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public List<Category> getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(List<Category> userInterests) {
        this.userInterests = userInterests;
    }

    public List<Category> getTrendingCategories() {
        return trendingCategories;
    }

    public void setTrendingCategories(List<Category> trendingCategories) {
        this.trendingCategories = trendingCategories;
    }
}