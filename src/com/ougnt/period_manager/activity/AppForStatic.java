package com.ougnt.period_manager.activity;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ougnt.period_manager.R;

public class AppForStatic extends Application {
    private static Context mContext;
    private Tracker appTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    synchronized public Tracker getTracker() {
        if (appTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            appTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return appTracker;
    }
}
