package com.ougnt.period_manager.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * * # Created by wacharint on 12/12/15.
 */
public class DatabaseHelpRepositoryHelper extends SQLiteOpenHelper {

    public DatabaseHelpRepositoryHelper(Context context) {
        super(context, "period_manager_help.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String helpQuery = "CREATE TABLE help_indicator (indicator INTEGER DEFAULT 1)";
        db.execSQL(helpQuery);

        String insert = "INSERT INTO help_indicator VALUES (1)";
        db.execSQL(insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String helpQuery = "DROP TABLE IF EXISTS help_indicator";
        db.execSQL(helpQuery);
    }
}
