package com.example.serviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyService extends Service {

    public static final String NOTIFICATION = "receiver";
    public boolean success;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        String file = intent.getStringExtra("file");
        new DoBackgroundTask().execute(url,file);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private class DoBackgroundTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            int count;
            long total = 0L;
            File output = new File(getExternalFilesDir(null),params[1]);
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(output.getPath());
                byte data[] = new byte[1024];
                while ((count = inputStream.read(data)) != -1) {
                    total = total + count;
                    outputStream.write(data, 0, count);
                }
                success = true;
                onProgressUpdate(params[1]);
            }
            catch (Exception e) {
            }
            return null;
        }

        protected void onProgressUpdate(String file) {
            Intent intent = new Intent(NOTIFICATION);
            intent.putExtra("file", file);
            sendBroadcast(intent);
        }

        protected void onPostExecute(String file) {
            stopSelf();
        }
    }
}