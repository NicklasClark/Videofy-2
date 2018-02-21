package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.model.post.PostDetails;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * Created by Prem$ on 2/21/2018.
 */

public class MyInterestsDiffCallback extends DiffUtil.Callback {

    public static final String DIFF_POST_DETAILS_LIST = "postDetailsList";

    private List<String> oldMapKeySet;
    private List<String> newMapKeySet;
    private List<List<PostDetails>> oldMapValueSet;
    private List<List<PostDetails>> newMapValueSet;

    public MyInterestsDiffCallback(Map<String, ArrayList<PostDetails>> oldMap, Map<String, ArrayList<PostDetails>> newMap) {
//        this.oldMap = oldMap;
//        this.newMap = newMap;
        oldMapKeySet = new ArrayList<>();
        newMapKeySet = new ArrayList<>();
        oldMapValueSet = new ArrayList<>();
        newMapValueSet = new ArrayList<>();
        oldMapKeySet.addAll(oldMap.keySet());
        newMapKeySet.addAll(newMap.keySet());
        for (String s : oldMap.keySet()) oldMapValueSet.add(oldMap.get(s));
        for (String s : newMap.keySet()) newMapValueSet.add(newMap.get(s));
    }

    @Override
    public int getOldListSize() {
        return oldMapKeySet.size();
    }

    @Override
    public int getNewListSize() {
        return newMapKeySet.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldMapKeySet.get(oldItemPosition), newMapKeySet.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return CollectionUtils.isEqualCollection(oldMapValueSet.get(oldItemPosition), newMapValueSet.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        ArrayList<PostDetails> postDetailsArrayList = (ArrayList<PostDetails>) newMapValueSet.get(newItemPosition);
        Bundle diffBundle = new Bundle();
        diffBundle.putParcelableArrayList(DIFF_POST_DETAILS_LIST, postDetailsArrayList);
        return diffBundle;
    }
}
