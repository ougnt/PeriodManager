package com.ougnt.period_manager.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by wacharint on 9/3/2016 AD.
 */
public class AppForStatic extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
