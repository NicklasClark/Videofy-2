package com.cncoding.teazer.model.friends;

import com.cncoding.teazer.model.base.MiniProfile;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class CircleList {
    private boolean next_page;
    private ArrayList<MiniProfile> circles;
    private Throwable error;

    public CircleList(boolean next_page, ArrayList<MiniProfile> circles) {
        this.next_page = next_page;
        this.circles = circles;
    }

    public CircleList(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<MiniProfile> getCircles() {
        return circles;
    }
}