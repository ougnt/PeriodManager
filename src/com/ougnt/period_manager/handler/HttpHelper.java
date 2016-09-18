package com.ougnt.period_manager.handler;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class HttpHelper {

    public static String get(final String url) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL targetUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "Application/Json");
                    connection.setUseCaches(false);

                    connection.connect();

                    DataInputStream is = new DataInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    String temp;
                    String ret = "";

                    while( (temp = br.readLine()) != null){
                        ret += temp;
                    }
                    return ret;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };

        try {
            return task.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String post(final String url, final String message) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL targetUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "Application/Json");
                    connection.setUseCaches(false);
                    connection.setDoOutput(true);

                    DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                    os.writeBytes(message);
                    os.close();

                    InputStream is = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    String temp;
                    String ret = "";

                    while( (temp = br.readLine()) != null){
                        ret += temp;
                    }
                    return ret;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };

        try {
            return task.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "";
    }
}
