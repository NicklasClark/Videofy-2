package com.cncoding.teazer.utilities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * Created by Prem $ on 10/24/2017.
 */

public class PlaceHolderDrawableHelper {
    private static final String placeholderValues[] = {
            "#ef5350", "#EC407A", "#42A5F5",
            "#7E57C2", "#26A69A", "#66BB6A",
            "#5C6BC0", "#26C6DA", "#9CCC65",
            "#8D6E63", "#FFA726", "#78909C",
            "#FFCA28", "#26A69A", "#8D6E63",
            "#FFEE58", "#29B6F6", "#9CCC65",
            "#FFCA28", "#FF7043", "#7E57C2",
            "#42A5F5", "#66BB6A", "#8D6E63"};
    private static List<Drawable> drawableBackgroundList;

    public static Drawable getBackgroundDrawable(int position) {
        if(drawableBackgroundList==null || drawableBackgroundList.size()==0) {
            drawableBackgroundList = new ArrayList<>(placeholderValues.length);
            for (String placeholderValue : placeholderValues) {
                int color = Color.parseColor(placeholderValue);
                drawableBackgroundList.add(new ColorDrawable(color));
            }
        }

        return drawableBackgroundList.get(position % placeholderValues.length);
    }

//    public static Drawable getBackgroundDrawable() {
//        if(drawableBackgroundList==null || drawableBackgroundList.size()==0) {
//            drawableBackgroundList = new ArrayList<>(placeholderValues.length);
//            for (String placeholderValue : placeholderValues) {
//                int color = Color.parseColor(placeholderValue);
//                drawableBackgroundList.add(new ColorDrawable(color));
//            }
//        }
//
//        return drawableBackgroundList.get(new Random().nextInt(23) % placeholderValues.length);
//    }

//    public static String getBackgroundColor() {
//        return placeholderValues[new Random().nextInt(placeholderValues.length - 1)];
//    }

    public static int getColor() {
        return Color.parseColor(placeholderValues[new Random().nextInt(23)]);
    }
}