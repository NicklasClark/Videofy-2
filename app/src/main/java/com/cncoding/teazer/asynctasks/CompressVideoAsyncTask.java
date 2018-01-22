package com.cncoding.teazer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.github.tcking.giraffecompressor.GiraffeCompressor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;

/**
 *
 * Created by amit on 6/12/17.
 */

public class CompressVideoAsyncTask extends AsyncTask<String, Void, Void> {

    private final Context context;
    public AsyncResponse delegate = null;
    private String filePath;

    public CompressVideoAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... sourceVideoPath) {
        GiraffeCompressor.init(context);

        File sourceFile = new File(sourceVideoPath[0]);

        String dirPath = context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + File.separator + "teazerTemp";
        File projDir = new File(dirPath);
        if (!projDir.exists())
            projDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        final String destinationDir = dirPath + "/teazer_" + timeStamp + ".mp4";

//        String[] command = {"-y", "-i", sourceFile.getAbsolutePath(), "-s", "160x120", "-r", "25", "-vcodec", "mp4", "-b:v", "150k", "-b:a", "48000", "-ac", "2", "-ar", "22050", destFile.getAbsolutePath()};
//        String cmd =  "-y "+"-i "+sourceFile.getAbsolutePath()+" -s "+"160x120 "+"-r "+"25 "+"-vcodec "+"mpeg4 "+"-b:v "+"150k "+"-b:a "+"48000 "+"-ac "+"2 "+"-ar "+"22050 " +"-pix_fmt yuv420p "+ destFile.getAbsolutePath();
//        String cmd =  "-i "+ sourceFile.getAbsolutePath()+ " -vcodec h264 -acodec mp2" + destFile.getAbsolutePath();
//        String cmd =  "-y -i "+sourceFile.getAbsolutePath()+" -c:v libx264 -preset ultrafast -tune fastdecode "+destFile.getAbsolutePath();;
//        String cmd =  "-y -i "+sourceFile.getAbsolutePath() +" -strict experimental -vcodec libx264 -preset ultrafast -crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 320x240 -aspect 4:3 "+ destFile.getAbsolutePath();
//        String cmd =  "-y -i "+sourceFile.getAbsolutePath() +" -profile:v baseline -vcodec h264 -acodec aac -movflags +faststart -c:v libx264 -crf 28 -preset ultrafast -strict -2 -b:a 128k "+ destinationDir;
        String cmd =  "-y -i "+sourceFile.getAbsolutePath() +" -profile:v baseline -vcodec h264 -movflags +faststart -r 15 -crf 27 -preset ultrafast -strict -2 -b:a 128k -c:a copy "+ destinationDir;
//
//
        EpEditor epEditor =  new EpEditor(context);
        epEditor.execCmd(cmd, 0, new OnEditorListener(){
            @Override
            public void onSuccess(){
                Log.d("Compress", "Success");
                delegate.compressionProcessFinish(destinationDir);
            }

            @Override
            public void onFailure(){
                Log.d("Compress", "Failed");
            }

            @Override
            public void onProgress(float progress){
                Log.d("Compress", "Running");
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
//        delegate.compressionProcessFinish(result);
    }


    public interface AsyncResponse {
        void compressionProcessFinish(String output);
    }

}
