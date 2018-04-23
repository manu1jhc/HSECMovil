package com.pango.hsec.hsec.util;

import android.app.Activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;

import com.pango.hsec.hsec.model.GaleriaModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        void onProgressUpdate(int percentage);
        void onError();
        void onFinish();
    }

    public ProgressRequestBody(final GaleriaModel file, final  UploadCallbacks listener, Activity activity) {
        mFileup=file;
        mListener = listener;
        mactivity=activity;
    }

    @Override
    public MediaType contentType() {
        // i want to upload only images
        return MediaType.parse("image/*");
    }

    @Override
    public long contentLength() throws IOException {
        return Long.parseLong(mFileup.Tamanio);
    }

    @Override
    @NonNull
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength =Long.parseLong(mFileup.Tamanio);
        long uploaded = 0;
        int bytesRead, bytesAvailable, bufferSize;
        // create a buffer of  maximum size
        if(mFileup.uri!= null){
            Cursor returnCursor =mactivity.getContentResolver().query(mFileup.uri, null, null, null, null);
            try {
                InputStream inputStream = mactivity.getContentResolver().openInputStream(mFileup.uri);
                try {
                        if(mFileup.Tamanio==null)fileLength =inputStream.available();
                        bytesAvailable = inputStream.available();
                        bufferSize = Math.min(bytesAvailable, DEFAULT_BUFFER_SIZE);

                        byte[] buffer = new byte[bufferSize]; // or other buffer size
                        int read;

                        Handler handler = new Handler(Looper.getMainLooper());
                        while ((read = inputStream.read(buffer)) != -1) {
                            handler.post(new ProgressUpdater(uploaded, fileLength));
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
           if(mFileup.Tamanio==null)fileLength =mfile.length();
           bytesAvailable = in.available();
           bufferSize = Math.min(bytesAvailable, DEFAULT_BUFFER_SIZE);
           byte[] buffer = new byte[bufferSize];

           try {
               int read;
               Handler handler = new Handler(Looper.getMainLooper());
               while ((read = in.read(buffer)) != -1) {

                   // update progress on UI thread
                   handler.post(new ProgressUpdater(uploaded, fileLength));

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
            mListener.onProgressUpdate((int)(100 * mUploaded / mTotal));
        }
    }
}