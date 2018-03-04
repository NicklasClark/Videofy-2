package com.cncoding.teazer.data.apiCalls;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 *
 * Created by Prem $ on 10/3/2017.
 */

public class ProgressRequestBody extends RequestBody {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private File file;
    private UploadCallbacks uploadCallbacks;

    public ProgressRequestBody(File file, UploadCallbacks uploadCallbacks) {
        this.file = file;
        this.uploadCallbacks = uploadCallbacks;
//        this = RequestBody.create(MediaType.parse("video/*"), file);
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("video/*");
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = contentLength();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long uploaded = 0;

        try (FileInputStream inputStream = new FileInputStream(file)) {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = inputStream.read(buffer)) != -1) {

                uploaded += read;
                sink.write(buffer, 0, read);

//            UPDATE PROGRESS ON UI THREAD
                handler.postDelayed(new ProgressUpdater(fileLength, uploaded), 2000);
//                uploadCallbacks.onProgressUpdate((int) (100 * uploaded / fileLength));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);
        void onUploadError(String throwable);
        void onUploadFinish(String videoPath, boolean gallery);
    }

    private class ProgressUpdater implements Runnable {

        private long total;
        private long uploaded;

        ProgressUpdater(long total, long uploaded) {
            this.total = total;
            this.uploaded = uploaded;
        }

        @Override
        public void run() {
            uploadCallbacks.onProgressUpdate((int) (100 * uploaded / total));
        }
    }
}
