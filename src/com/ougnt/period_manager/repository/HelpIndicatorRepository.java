package com.ougnt.period_manager.repository;

import android.content.Context;

/**
 * * # Created by wacharint on 12/12/15.
 */
public class HelpIndicatorRepository {

//    public static int getIndicator(Context context) {
//
//        DatabaseHelpRepositoryHelper helper = new DatabaseHelpRepositoryHelper(context);
//        Cursor c = helper.getReadableDatabase().rawQuery("SELECT indicator FROM help_indicator", null);
//        c.moveToFirst();
//        int ret = c.getInt(0);
//        c.close();
//        return ret;
//    }

    public static void setIndicator(Context context, int value) {

        DatabaseHelpRepositoryHelper helper = new DatabaseHelpRepositoryHelper(context);
        helper.getWritableDatabase().execSQL("UPDATE help_indicator SET indicator = " + value);
    }
}
