//package com.cncoding.teazer.asynctasks;
//
//import android.content.Context;
//import android.content.res.AssetManager;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.util.Log;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import VideoHandle.EpEditor;
//import VideoHandle.OnEditorListener;
//
///**
// * Created by amit on 10/1/18.
// */
//
//public class GifConvertAsyncTask extends AsyncTask<String, Void, String> {
//
//    private final Context context;
//    public WatermarkAsyncResponse delegate = null;
//    private String filePath;
//    private AssetManager assetManager;
//
//    public GifConvertAsyncTask(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected String doInBackground(String... sourceVideoPath) {
//        File sourceFile = new File(sourceVideoPath[0]);
//
//        String dirPath = context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + File.separator + "teazerTemp";
//        File projDir = new File(dirPath);
//        if (!projDir.exists())
//            projDir.mkdirs();
//
//        String mPath = null;
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        final String destinationDir = dirPath + "/teazer_" + timeStamp + ".gif";
//        "-i " + video_320x180.mp4 + "-r 10 -vf scale=160:90 agif_r10_d20_160x90.gif -hide_banner
//        String cmd =  "-i "+ sourceFile.getAbsolutePath() +" -i "+ mPath +" -filter_complex " + "overlay=W-w-5:H-h-5 " +
//                "-codec:a copy "+ destinationDir;
//
//        EpEditor epEditor =  new EpEditor(context);
//        epEditor.execCmd(cmd, 0, new OnEditorListener(){
//            @Override
//            public void onSuccess(){
//                Log.d("GifConvert", "Success");
//                delegate.waterMarkProcessFinish(destinationDir);
//            }
//
//            @Override
//            public void onFailure(){
//                Log.d("GifConvert", "Failed");
//            }
//
//            @Override
//            public void onProgress(float progress){
//                Log.d("GifConvert", "Running");
//            }
//        });
//        return destinationDir;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
////        delegate.waterMarkProcessFinish(result);
//    }
//
//
//    public interface WatermarkAsyncResponse {
//        void waterMarkProcessFinish(String output);
//    }
//
//}