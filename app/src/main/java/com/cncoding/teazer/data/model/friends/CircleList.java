package com.cncoding.teazer.data.model.friends;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.utilities.common.Annotations.CallType;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class CircleList extends BaseModel {

    private boolean next_page;
    private ArrayList<MiniProfile> circles;

    public CircleList(boolean next_page, ArrayList<MiniProfile> circles) {
        this.next_page = next_page;
        this.circles = circles;
    }

    public CircleList(Throwable error) {
        this.error = error;
    }

    public CircleList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<MiniProfile> getCircles() {
        return circles;
    }
}