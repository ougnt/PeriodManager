package com.ougnt.period_manager.google;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ougnt.period_manager.R;

public class AnalyticsApplication extends Application {

    private Tracker appTracker;

    synchronized public Tracker getTracker() {
        if (appTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            appTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return appTracker;
    }
}
