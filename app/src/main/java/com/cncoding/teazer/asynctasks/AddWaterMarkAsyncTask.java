package com.cncoding.teazer.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.cncoding.teazer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;

/**
 * Created by amit on 19/12/17.
 */

public class AddWaterMarkAsyncTask extends AsyncTask<String, Void, String> {

    private final Context context;
    public WatermarkAsyncResponse delegate = null;

    public AddWaterMarkAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(final String... sourceVideoPath) {
        File sourceFile = new File(sourceVideoPath[0]);

//        String dirPath = context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + File.separator + "teazerTemp";
        File videoFolder = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                context.getString(R.string.app_name));

        if (!videoFolder.exists()) {
            videoFolder.mkdirs();
        }

        Bitmap bitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_teazer_top);

        String fileName ="watermark_logo.png";
        String mPath = null;
        try {

            FileOutputStream out1 = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            bitMap.compress(Bitmap.CompressFormat.PNG, 100, out1);

            out1.flush();

            out1.close();

            File f = context.getFileStreamPath(fileName);

            mPath=f.getAbsolutePath();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        final String destinationDir = videoFolder.getAbsolutePath() + "/teazer_" + timeStamp + ".mp4";
        String cmd =  "-i "+ sourceFile.getAbsolutePath() +" -i "+ mPath +" -vcodec h264 -movflags +faststart -preset ultrafast -c:a copy -filter_complex " + "overlay=W-w-5:H-h-5 " + destinationDir;

        EpEditor epEditor =  new EpEditor(context);
        epEditor.execCmd(cmd, 0, new OnEditorListener(){
            @Override
            public void onSuccess(){
                Log.d("Watermark", "Success");
                delegate.waterMarkProcessFinish(destinationDir, sourceVideoPath[0]);
            }

            @Override
            public void onFailure(){
                Log.d("Watermark", "Failed");
            }

            @Override
            public void onProgress(float progress){
                Log.d("Watermark", "Running");
            }
        });
        return destinationDir;
    }

    @Override
    protected void onPostExecute(String result) {
//        delegate.waterMarkProcessFinish(result);
    }


    public interface WatermarkAsyncResponse {
        void waterMarkProcessFinish(String destinationPath, String sourcePath);
    }

}
