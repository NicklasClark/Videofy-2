package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cncoding.teazer.ui.home.profile.fragment.FragmentProfileMyCreations;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentProfileMyReactions;

/**
 *
 * Created by MOHD ARIF on 07-11-2017.
 */

public class ProfileCreationReactionPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"My Creations", "My Reactions"};
    private Context context;
    Fragment fragment;
    int userId;

    public ProfileCreationReactionPagerAdapter(FragmentManager fm, Context context, Fragment fragment,int userId) {
        super(fm);
        this.context = context;
        this.fragment = fragment;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return FragmentProfileMyCreations.newInstance(userId);

            case 1:

                return FragmentProfileMyReactions.newInstance(userId);

            default:

                return FragmentProfileMyCreations.newInstance(userId);


        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];
    }
}
