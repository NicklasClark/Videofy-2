package com.cncoding.teazer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

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
        File destFile = new File("/storage/emulated/0/comp.mp4");
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String destinationDir = context.getApplicationInfo().dataDir + "/trimmedVideo_" + timeStamp + ".mp4";
////        String destinationDir = sourceFile.getParent() + "/trimmedVideo";
//        try {
//            filePath = SiliCompressor.with(context).compressVideo(sourceVideoPath[0], "/storage/emulated/0/");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return filePath;
        String[] command = {"-y", "-i", sourceFile.getAbsolutePath(), "-s", "160x120", "-r", "25", "-vcodec", "mp4", "-b:v", "150k", "-b:a", "48000", "-ac", "2", "-ar", "22050", destFile.getAbsolutePath()};
//        String cmd =  "-y "+"-i "+sourceFile.getAbsolutePath()+" -s "+"160x120 "+"-r "+"25 "+"-vcodec "+"mpeg4 "+"-b:v "+"150k "+"-b:a "+"48000 "+"-ac "+"2 "+"-ar "+"22050 " +"-pix_fmt yuv420p "+ destFile.getAbsolutePath();
//        String cmd =  "-i "+ sourceFile.getAbsolutePath()+ " -vcodec h264 -acodec mp2" + destFile.getAbsolutePath();
//        String cmd =  "-y -i "+sourceFile.getAbsolutePath()+" -c:v libx264 -preset ultrafast -tune fastdecode "+destFile.getAbsolutePath();;
//        String cmd =  "-y -i "+sourceFile.getAbsolutePath() +" -strict experimental -vcodec libx264 -preset ultrafast -crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 320x240 -aspect 4:3 "+ destFile.getAbsolutePath();
//
//
//        EpEditor epEditor =  new EpEditor(context);
//        epEditor.execCmd(cmd, 0, new OnEditorListener(){
//            @Override
//            public void onSuccess(){
//                Log.d("Compress", "Success");
//            }
//
//            @Override
//            public void onFailure(){
//                Log.d("Compress", "Failed");
//            }
//
//            @Override
//            public void onProgress(float progress){
//                Log.d("Compress", "Running");
//            }
//        });
        return sourceVideoPath[0];
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }


    public interface AsyncResponse {
        void processFinish(String output);
    }

}
