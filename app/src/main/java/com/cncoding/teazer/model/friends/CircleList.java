package com.cncoding.teazer.model.friends;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

import com.cncoding.teazer.model.base.MiniProfile;

import java.util.ArrayList;

public class CircleList {
    private boolean next_page;
    private ArrayList<MiniProfile> circles;

    public CircleList(boolean next_page, ArrayList<MiniProfile> circles) {
        this.next_page = next_page;
        this.circles = circles;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<MiniProfile> getCircles() {
        return circles;
    }
}