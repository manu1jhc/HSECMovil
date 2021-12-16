package com.pango.hsec.hsec.util;

import android.app.Activity;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.pango.hsec.hsec.model.GaleriaModel;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by BOB on 18/04/2018.
 */

public class ProgressRequestBody extends RequestBody {

    private GaleriaModel mFileup;
    private UploadCallbacks mListener;
    private Activity mactivity;

    private static final int DEFAULT_BUFFER_SIZE = 4 * 1024;

    public interface UploadCallbacks {
        void onProgressUpdate(long percentage);
        void onError();
        void onFinish();
    }

    public ProgressRequestBody( GaleriaModel file,   UploadCallbacks listener, Activity activity) {
        mFileup=file;
        mListener = listener;
        mactivity=activity;
    }
    @Override
    public MediaType contentType() {
        // i want to upload only images
        //return MediaType.parse("image/*");
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(mFileup.Url);
        if (!StringUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        else {
            if(mFileup.Url.contains("imagenCompress")) type="image/jpeg";
            else if (mFileup.Url.contains(".mp4")) type="video/mp4";
            else if (mFileup.Url.contains(".pdf")) type="application/pdf";
            else type="application/octet-stream";
        }
        return MediaType.parse(type);
    }

    @Override
    public long contentLength() throws IOException {
        return Long.parseLong(mFileup.Tamanio);
    }

    @Override
    @NonNull
    public void writeTo(BufferedSink sink) throws IOException {

        long uploaded = 0;
        int bytesRead, bytesAvailable, bufferSize;
        // create a buffer of  maximum size
        if(mFileup.uri!= null){
            long fileLength =Long.parseLong(mFileup.Tamanio);
            Cursor returnCursor =mactivity.getContentResolver().query(mFileup.uri, null, null, null, null);
            try {
                InputStream inputStream = mactivity.getContentResolver().openInputStream(mFileup.uri);
                try {
                    bytesAvailable = inputStream.available();
                    bufferSize = Math.min(bytesAvailable, DEFAULT_BUFFER_SIZE);

                    byte[] buffer = new byte[bufferSize]; // or other buffer size
                    int read;

                    Handler handler = new Handler(Looper.getMainLooper());
                    while ((read = inputStream.read(buffer)) != -1) {
                        handler.post(new ProgressUpdater(uploaded,fileLength));
                        uploaded += read;
                        sink.write(buffer, 0, read);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                returnCursor.close();
            }
        }
        else{
            File mfile= new File(mFileup.Url);
            FileInputStream in = new FileInputStream(mfile);
            long fileLength =mfile.length();
            bytesAvailable = in.available();
            bufferSize = Math.min(bytesAvailable, DEFAULT_BUFFER_SIZE);
            byte[] buffer = new byte[bufferSize];

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded,fileLength)); //(GlobalVariables.progresbarPercent,GlobalVariables.progresbarSize));

                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate(mUploaded);
        }
    }
}