package com.cncoding.teazer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;

/**
 * Created by amit on 6/12/17.
 */


public class CompressVideoAsyncTask extends AsyncTask<String, Void, String> {

    private final Context context;
    public AsyncResponse delegate = null;
    private String filePath;

    public CompressVideoAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... sourceVideoPath) {
        File sourceFile = new File(sourceVideoPath[0]);
        String destinationDir = context.getApplicationInfo().dataDir;
//        String destinationDir = sourceFile.getParent() + "/trimmedVideo";
        try {
            filePath = SiliCompressor.with(context).compressVideo(sourceVideoPath[0], destinationDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }


    public interface AsyncResponse {
        void processFinish(String output);
    }

}
