package com.cncoding.teazer.customViews;

import android.content.Context;
import android.graphics.Typeface;

/**
 *
 * Created by Prem $ on 11/16/2017.
 */

public class TypeFactory {

    public Typeface bold;
    public Typeface condensed;
    public Typeface regular;
    public Typeface semiBold;
    public Typeface signPainter;

    public TypeFactory(Context context){
        bold = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_bold.ttf");
        condensed = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_condensed.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_regular.ttf");
        semiBold = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_semibold.ttf");
        signPainter = Typeface.createFromAsset(context.getAssets(), "fonts/sign_painter_house_script.ttf");
    }
}