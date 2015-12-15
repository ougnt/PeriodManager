package com.ougnt.period_manager.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * * # Created by wacharint on 12/15/15.
 */
public class DatabaseSettingRepositoryHelper extends SQLiteOpenHelper {

    public DatabaseSettingRepositoryHelper(Context context, int version) {
        super(context, "setting", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createdQuery = "CREATE TABLE setting (period_cycle FLOAT, period_length FLOAT, average_cycle FLOAT, average_length FLOAT, data_count INTEGER, is_first_time INTEGER)";
        db.execSQL(createdQuery);
        String insertQuery = "INSERT INTO setting VALUES (28,7,28,7,1,1)";
        db.execSQL(insertQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DO NOTHING
    }
}
