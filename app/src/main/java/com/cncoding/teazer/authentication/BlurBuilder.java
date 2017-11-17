package com.cncoding.teazer.authentication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class BlurBuilder {
//    private static final float BITMAP_SCALE = 0.1f;
//    private static final float BLUR_RADIUS = 25f;

    public static Bitmap blur(Context context, Bitmap bitmap) {
        return blurImage(context, bitmap);
    }

//    private static Bitmap blur(Context ctx, Bitmap image) {
//        int width = Math.round(image.getWidth() * BITMAP_SCALE);
//        int height = Math.round(image.getHeight() * BITMAP_SCALE);
//
//        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
//        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
//
//        RenderScript rs = RenderScript.create(ctx);
//        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//        theIntrinsic.setRadius(BLUR_RADIUS);
//        theIntrinsic.setInput(tmpIn);
//        theIntrinsic.forEach(tmpOut);
//        tmpOut.copyTo(outputBitmap);
//
//        return outputBitmap;
//    }

    private static Bitmap blurImage (Context context, Bitmap input) {
        try {
            RenderScript  rsScript = RenderScript.create(context);
            Allocation alloc = Allocation.createFromBitmap(rsScript, input);

            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript,   Element.U8_4(rsScript));
            blur.setRadius(15);
            blur.setInput(alloc);

            Bitmap result = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
            Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);

            blur.forEach(outAlloc);
            outAlloc.copyTo(result);
            result = tintImage(result);

            rsScript.destroy();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }

    private static Bitmap tintImage(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(Color.argb(140, 33, 33, 33), PorterDuff.Mode.SRC_OVER));
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapResult);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapResult;
    }

//    private static Bitmap getScreenshot(View view) {
//        view.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//        view.setDrawingCacheEnabled(false);
//        return bitmap;
//    }
}