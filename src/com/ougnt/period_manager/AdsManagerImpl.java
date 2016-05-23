package com.ougnt.period_manager;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.event.OnAdsRequestReturnEventListener;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * * # Created by wacharint on 4/23/16.
 */
public class AdsManagerImpl extends IAdsManager {

    public AdsManagerImpl(UUID deviceId) {
        super(deviceId);
    }

    @Override
    public void requestAds(final OnAdsRequestReturnEventListener listener, String adsLanguage) {
        client = client == null ? new DefaultHttpClient() : client;
        get = new HttpGet(String.format("%s?deviceId=%s&language=%s",InitialActivity.AdsRequestUri, deviceId.toString(), adsLanguage));
        get.setHeader("Content-Type","application/Json");

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    HttpResponse response = client.execute(get);
                    InputStream inputStream = (response.getEntity().getContent());

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
                    String stringResult = "";
                    String line = "";
                    while((line = bReader.readLine()) != null) {

                        stringResult += line;
                    }
                    inputStream.close();

                    Gson gson = new GsonBuilder().create();
                    adsInfo = gson.fromJson(stringResult, AdsInfo.class);
                    listener.onAdsInfoReturn(adsInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.execute();
    }

    @Override
    public void submitAndResetAdsClick() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(
                    String.format("%s?experimentRunId=%s&user=%s",
                            InitialActivity.AdsClickUri,
                            adsInfo.experimentRunId,
                            adsInfo.experimentUser)
                );
                try {
                    client.execute(get);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute();
    }

    @Override
    public void addCounter() {

    }

    @Override
    public boolean shouldDisplayAds() {
        return !adsInfo.AdsUrl.isEmpty();
    }

    @Override
    public float calculateAdsRatio() {
        return 0.1f;
    }

    private HttpClient client;
    private HttpGet get;
}
