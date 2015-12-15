package com.ougnt.period_manager.repository;

import android.content.Context;
import android.database.Cursor;

/**
 * * # Created by wacharint on 12/15/15.
 */
public class SettingRepository {
    public float periodLength;
    public float periodCycle;
    public float averageLength;
    public float averageCycle;
    public int count;
    public boolean isFirstTime;

    private SettingRepository() {}

    public static SettingRepository getSettingRepository(Context context) {

        SettingRepository setting = new SettingRepository();
        DatabaseSettingRepositoryHelper db = new DatabaseSettingRepositoryHelper(context, 1);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT period_length, period_cycle, average_length, average_cycle, data_count, is_first_time FROM setting", null);
        cursor.moveToFirst();
        setting.periodLength = cursor.getFloat(0);
        setting.periodCycle = cursor.getFloat(1);
        setting.averageLength = cursor.getFloat(2);
        setting.averageCycle = cursor.getFloat(3);
        setting.count = cursor.getInt(4);
        setting.isFirstTime = cursor.getInt(5) == 1;

        return setting;
    }

    public void saveSetting(Context context) {
        DatabaseSettingRepositoryHelper db = new DatabaseSettingRepositoryHelper(context, 1);
        String updateString = String.format("UPDATE setting SET period_length = %s, period_cycle = %s, average_length = %s, average_cycle = %s, data_count = %s, is_first_time = %s",
                periodLength,
                periodCycle,
                averageLength,
                averageCycle,
                count,
                0);
        db.getWritableDatabase().execSQL(updateString);
    }
}
