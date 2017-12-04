package com.cncoding.teazer.videoTrim.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.cncoding.teazer.videoTrim.interfaces.OnTrimVideoListener;
import com.cncoding.teazer.videoTrim.models.VideoInfo;
import com.googlecode.mp4parser.authoring.Track;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import iknow.android.utils.DeviceUtil;
import iknow.android.utils.UnitConverter;
import iknow.android.utils.callback.SingleCallback;
import iknow.android.utils.thread.BackgroundExecutor;

public class TrimVideoUtil {

    private static String savePath;
    private static final String TAG = TrimVideoUtil.class.getSimpleName();
    public static final int VIDEO_MAX_DURATION = 60;
    public static final int MIN_TIME_FRAME = 3;
    public static final int VIDEO_MAX_ALLOWED_DURATION = 60;
    private static final int thumb_Width = (DeviceUtil.getDeviceWidth() - UnitConverter.dpToPx(20)) / VIDEO_MAX_DURATION;
    private static final int thumb_Height = UnitConverter.dpToPx(60);
    private static final long one_frame_time = 1000000;

    public static void trimVideo(final Context context, String inputFile, String outputFile, final long startMs, final long endMs, final OnTrimVideoListener callback) throws IOException {
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        final String outputName = "trimmedVideo_" + timeStamp + ".mp4";
//
//        String start = convertSecondsToTime(startMs / 1000);
//        String duration = convertSecondsToTime((endMs - startMs) / 1000);

//        String cmd = "-ss " + start + " -t " + duration + " -i " + inputFile + " -vcodec copy -acodec copy " + outputFile + "/" + outputName;
//        String[] command = {"-ss", "" + startMs / 1000,
//                "-y", "-i", inputFile, "-t", "" + (endMs - startMs) / 1000, "-s", "320x240", "-r", "15", "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputFile+"/"+outputName};
//        String[] command = cmd.split(" ");
//        try {
//            FFmpeg.getInstance(context).execute(command, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onFailure(String s) {
//                }
//
//                @Override
//                public void onSuccess(String s) {
////                    genVideoUsingMp4Parser(src, file, startMs, endMs, callback);
//                    callback.onFinishTrim(outputName);
//                }
//
//                @Override
//                public void onStart() {
//                    callback.onStartTrim();
//                }
//
//                @Override
//                public void onFinish() {
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            e.printStackTrace();
//        }
        final String outPath = getSavePath(context) + "out.mp4";
        EpVideo epVideo =  new  EpVideo (inputFile);
        epVideo.clip(startMs / 1000,(endMs - startMs) / 1000);
        new EpEditor(context).exec(epVideo, new EpEditor.OutputOption(outPath), new OnEditorListener() {
            @Override
            public void onSuccess() {
                callback.onFinishTrim(outPath);
            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(float v) {
                Toast.makeText(context, "Processing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static String getSavePath(Context context) {
        choseSavePath();
        copyFilesFassets(context, "Ress", savePath);
        return savePath;
    }
    private static void choseSavePath() {
        savePath = Environment.getExternalStorageDirectory().getPath() + "/teazerTrim/";
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {
                InputStream is = context.getAssets().open(oldPath);
                File ff = new File(newPath);
                if (!ff.exists()) {
                    FileOutputStream fos = new FileOutputStream(ff);
                    byte[] buffer = new byte[1024];
                    int byteCount = 0;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                    is.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static double correctTimeToSyncSample(@NonNull Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];

            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                // samples always start with 1 but we start with zero therefore +1
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;

        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }

    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        Formatter mFormatter = new Formatter();
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static void backgroundShootVideoThumb(final Context context, final Uri videoUri, final SingleCallback<ArrayList<Bitmap>, Integer> callback) {
        final ArrayList<Bitmap> thumbnailList = new ArrayList<>();
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0L, "") {
               @Override
               public void execute() {
                   try {
                       MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                       mediaMetadataRetriever.setDataSource(context, videoUri);
                       // Retrieve media data use microsecond
                       long videoLengthInMs = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;
                       long numThumbs = videoLengthInMs < one_frame_time ? 1 : (videoLengthInMs / one_frame_time);
                       final long interval = videoLengthInMs / numThumbs;

                       for (long i = 0; i < numThumbs; ++i) {
                           Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(i * interval, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                           try {
                               bitmap = Bitmap.createScaledBitmap(bitmap, thumb_Width, thumb_Height, false);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                           thumbnailList.add(bitmap);
                           if (thumbnailList.size() == 3) {
                               callback.onSingleCallback((ArrayList<Bitmap>) thumbnailList.clone(), (int) interval);
                               thumbnailList.clear();
                           }
                       }
                       if (thumbnailList.size() > 0) {
                           callback.onSingleCallback((ArrayList<Bitmap>) thumbnailList.clone(), (int) interval);
                           thumbnailList.clear();
                       }
                       mediaMetadataRetriever.release();
                   } catch (final Throwable e) {
                       Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                   }
               }
           }
        );

    }

    /**
     * 需要设计成异步的
     */
    public static ArrayList<VideoInfo> getAllVideoFiles(Context mContext) {
        VideoInfo video;
        ArrayList<VideoInfo> videos = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        try {
            Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                            null, null, MediaStore.Video.Media.DATE_MODIFIED + " desc");
            if(cursor != null) {
                while (cursor.moveToNext()) {
                    video = new VideoInfo();
                    if (cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)) != 0) {
                        video.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
                        video.setVideoPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                        video.setCreateTime(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
                        video.setVideoName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                        videos.add(video);
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }

    public static String getVideoFilePath(String url) {

        if (TextUtils.isEmpty(url) || url.length() < 5)
            return "";

        if (url.substring(0, 4).equalsIgnoreCase("http")) {
        } else
            url = "file://" + url;

        return url;
    }

    private static String convertSecondsToTime(long seconds) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (seconds <= 0)
            return "00:00";
        else {
            minute = (int) seconds / 60;
            if (minute < 60) {
                second = (int) seconds % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = (int) (seconds - hour * 3600 - minute * 60);
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
