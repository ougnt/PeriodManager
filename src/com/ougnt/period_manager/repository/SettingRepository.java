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
    public int flag;

    public static final int MaxIndicator = 0xFFFF;
    public static final int FlagCalendarMonthView = 0x01;

    private SettingRepository() {}

    public static SettingRepository getSettingRepository(Context context) {

        SettingRepository setting = new SettingRepository();
        DatabaseSettingRepositoryHelper db = new DatabaseSettingRepositoryHelper(context, 2);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT period_length, period_cycle, average_length, average_cycle, data_count, is_first_time, flag FROM setting", null);
        cursor.moveToFirst();
        setting.periodLength = cursor.getFloat(0);
        setting.periodCycle = cursor.getFloat(1);
        setting.averageLength = cursor.getFloat(2);
        setting.averageCycle = cursor.getFloat(3);
        setting.count = cursor.getInt(4);
        setting.isFirstTime = cursor.getInt(5) == 1;
        setting.flag = cursor.getInt(6);

        return setting;
    }

    public void saveSetting(Context context) {
        DatabaseSettingRepositoryHelper db = new DatabaseSettingRepositoryHelper(context, 2);
        String updateString = String.format("UPDATE setting SET period_length = %s, period_cycle = %s, average_length = %s, average_cycle = %s, data_count = %s, is_first_time = %s, flag = %s",
                periodLength,
                periodCycle,
                averageLength,
                averageCycle,
                count,
                0,
                flag);
        db.getWritableDatabase().execSQL(updateString);
    }
}
