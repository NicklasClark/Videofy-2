package com.cncoding.teazer.ui.base;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


/**
 *
 * Created by Prem $ on 11/02/17.
 */

@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

    public abstract void pushFragment(Fragment fragment);
    public abstract void pushFragmentOnto(Fragment fragment);
    public abstract void popFragment();

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
        void pushFragmentOnto(Fragment fragment);
        void popFragment();
    }
}