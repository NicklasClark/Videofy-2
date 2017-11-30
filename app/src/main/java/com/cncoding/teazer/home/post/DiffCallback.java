package com.cncoding.teazer.home.post;

import android.support.v7.util.DiffUtil;

/**
 *
 * Created by Prem $ on 11/30/2017.
 */

public class DiffCallback extends DiffUtil.Callback {

    @Override
    public int getOldListSize() {
        return 0;
    }

    @Override
    public int getNewListSize() {
        return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
