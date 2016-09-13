package com.ougnt.period_manager.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import org.joda.time.DateTime;

import java.util.LinkedList;

/**
 * * # Created by wacharint on 11/1/15.
 */
public class DatabaseRepositoryHelper extends SQLiteOpenHelper {

    // Version 3 add temperature
    // Version 5 add exect ovulation date to summary repo
    static final int CurrentVersion = 5;

    public DatabaseRepositoryHelper(Context context) {
        super(context, "period_manager_core.db", null, CurrentVersion);
        this.context = context;
    }

    public Context context;

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Temperature in degree celsius
        String creationString = "CREATE TABLE IF NOT EXISTS DATE_REPOSITORY (date DATE PRIMARY KEY, date_type INTEGER, comment VARCHAR DEFAULT '', temperature_value FLOAT DEFAULT 0)";

        db.execSQL(creationString);

        String insertString = "INSERT INTO DATE_REPOSITORY VALUES %s";
        DateTime initialDateTime = DateTime.parse("2015-01-01");
        LinkedList<DateTime> dates = new LinkedList<>();

        for(int i = 0; i<= 3650; i++) {
            dates.add(initialDateTime.minusDays(-i));
        }

        for(int i = 0; i < dates.size(); i++) {
            insertString = String.format(insertString, "('" + dates.get(i).toString("yyyy-MM-dd") + "',0,'',0), %s");
            if(i % 500 == 0) {

                insertString = insertString.replace(", %s", "");
                db.execSQL(insertString);
                insertString = "INSERT INTO DATE_REPOSITORY VALUES %s";
            }
        }

        insertString = insertString.replace(", %s", "");

        db.execSQL(insertString);

        String summaryQuery = "CREATE TABLE summary (exp_menstrual_from DATE, exp_menstrual_to DATE, exp_ovulation_from DATE, exp_ovulation_to DATE, exp_ovulation_date DATE)";
        db.execSQL(summaryQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion <= 1) {

            String summaryQuery = "CREATE TABLE summary (exp_menstrual_from DATE, exp_menstrual_to DATE, exp_ovulation_from DATE, exp_ovulation_to DATE)";
            db.execSQL(summaryQuery);
        }

        if(oldVersion <= 3) {

            String alterQuery = "ALTER TABLE DATE_REPOSITORY ADD COLUMN temperature_value FLOAT DEFAULT 0";
            try {
                db.execSQL(alterQuery);
            } catch (SQLiteException e) {}
        }

        if(oldVersion <= 4) {
            String alterQuery = "ALTER TABLE summary ADD COLUMN exp_ovulation_date DATE DEFAULT NULL";
            try {
                db.execSQL(alterQuery);
            } catch (SQLiteException e) {}
        }

    }
}
