package com.cncoding.teazer.utilities;

import android.support.annotation.AnimRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */


public class NavigationTransactionOptions {
    List<Pair<View, String>> sharedElements;
    @NavigationController.Transit
    int transition = FragmentTransaction.TRANSIT_NONE;
    @AnimRes
    int enterAnimation = 0;
    @AnimRes
    int exitAnimation = 0;
    @AnimRes
    int popEnterAnimation = 0;
    @AnimRes
    int popExitAnimation = 0;
    @StyleRes
    int transitionStyle = 0;
    String breadCrumbTitle;
    String breadCrumbShortTitle;

    private NavigationTransactionOptions(Builder builder) {
        sharedElements = builder.sharedElements;
        transition = builder.transition;
        enterAnimation = builder.enterAnimation;
        exitAnimation = builder.exitAnimation;
        transitionStyle = builder.transitionStyle;
        popEnterAnimation = builder.popEnterAnimation;
        popExitAnimation = builder.popExitAnimation;
        breadCrumbTitle = builder.breadCrumbTitle;
        breadCrumbShortTitle = builder.breadCrumbShortTitle;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private List<Pair<View, String>> sharedElements;
        private int transition;
        private int enterAnimation;
        private int exitAnimation;
        private int transitionStyle;
        private int popEnterAnimation;
        private int popExitAnimation;
        private String breadCrumbTitle;
        private String breadCrumbShortTitle;

        private Builder() {
        }

        public Builder addSharedElement(Pair<View, String> val) {
            if (sharedElements == null) {
                sharedElements = new ArrayList<>(3);
            }
            sharedElements.add(val);
            return this;
        }

        public Builder sharedElements(List<Pair<View, String>> val) {
            sharedElements = val;
            return this;
        }

        public Builder transition(@NavigationController.Transit int val) {
            transition = val;
            return this;
        }

        @SuppressWarnings("WeakerAccess")
        public Builder customAnimations(@AnimRes int enterAnimation, @AnimRes int exitAnimation) {
            this.enterAnimation = enterAnimation;
            this.exitAnimation = exitAnimation;
            return this;
        }

        public Builder customAnimations(@AnimRes int enterAnimation, @AnimRes int exitAnimation, @AnimRes int popEnterAnimation, @AnimRes int popExitAnimation) {
            this.popEnterAnimation = popEnterAnimation;
            this.popExitAnimation = popExitAnimation;
            return customAnimations(enterAnimation, exitAnimation);
        }


        public Builder transitionStyle(@StyleRes int val) {
            transitionStyle = val;
            return this;
        }

        public Builder breadCrumbTitle(String val) {
            breadCrumbTitle = val;
            return this;
        }

        public Builder breadCrumbShortTitle(String val) {
            breadCrumbShortTitle = val;
            return this;
        }

        public NavigationTransactionOptions build() {
            return new NavigationTransactionOptions(this);
        }
    }
}

