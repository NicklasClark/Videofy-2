package com.cncoding.teazer.data.model.post;

import com.cncoding.teazer.data.model.base.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class LandingPosts {
    private ArrayList<PostDetails> most_popular;
    private ArrayList<Category> user_interests;
    private ArrayList<Category> trending_categories;
    @SerializedName("my_interests")
    @Expose
    private Map<String, ArrayList<PostDetails>> my_interests;

    public LandingPosts(ArrayList<PostDetails> most_popular, ArrayList<Category> user_interests,
                        ArrayList<Category> trending_categories, Map<String, ArrayList<PostDetails>> my_interests) {
        this.most_popular = most_popular;
        this.user_interests = user_interests;
        this.trending_categories = trending_categories;
        this.my_interests = my_interests;
    }

    public void clearData() {
        if (most_popular != null)
            most_popular.clear();
        if (user_interests != null)
            user_interests.clear();
        if (trending_categories != null)
            trending_categories.clear();
        if (my_interests != null) {
            my_interests.clear();
        }
    }

//            public boolean areListsEmpty() {
//                return most_popular.isEmpty() && user_interests.isEmpty() && trending_categories.isEmpty() &&
//                        Collections.frequency(my_interests.values(), Collections.EMPTY_LIST) == my_interests.size();
//            }

    public ArrayList<PostDetails> getMostPopular() {
        return most_popular;
    }

    public ArrayList<Category> getUserInterests() {
        return user_interests;
    }

    public ArrayList<Category> getTrendingCategories() {
        return trending_categories;
    }

    public Map<String, ArrayList<PostDetails>> getMyInterests() {
        return my_interests;
    }

    public boolean isEmpty() {
        return getMostPopular().isEmpty() &&
                (getMyInterests().isEmpty() ||
                        Collections.frequency(getMyInterests().values(), Collections.EMPTY_LIST) == getMyInterests().size());
    }
}