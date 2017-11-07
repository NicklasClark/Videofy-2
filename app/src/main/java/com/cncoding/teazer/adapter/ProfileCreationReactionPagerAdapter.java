package com.cncoding.teazer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.cncoding.teazer.ui.fragment.FragmentProfileMyCreations;
import com.cncoding.teazer.ui.fragment.FragmentProfileMyReactions;

/**
 * Created by MOHD ARIF on 07-11-2017.
 */

public class ProfileCreationReactionPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"MyCreations", "MyReactions"};
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
                Toast.makeText(context, "Fragment Creation", Toast.LENGTH_SHORT).show();

                return FragmentProfileMyCreations.newInstance(position);

            case 1:
                Toast.makeText(context, "Fragment reaction", Toast.LENGTH_SHORT).show();
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
