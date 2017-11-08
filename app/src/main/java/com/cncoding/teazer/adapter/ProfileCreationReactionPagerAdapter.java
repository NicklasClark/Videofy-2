package com.cncoding.teazer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cncoding.teazer.ui.fragment.fragment.FragmentProfileMyCreations;
import com.cncoding.teazer.ui.fragment.fragment.FragmentProfileMyReactions;

/**
 * Created by MOHD ARIF on 07-11-2017.
 */

public class ProfileCreationReactionPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"My Creations", "My Reactions"};
    private Context context;

    public ProfileCreationReactionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:


                return FragmentProfileMyCreations.newInstance(position);

            case 1:

                return FragmentProfileMyReactions.newInstance(position);

            default:
                return FragmentProfileMyCreations.newInstance(position);

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];
    }
}
