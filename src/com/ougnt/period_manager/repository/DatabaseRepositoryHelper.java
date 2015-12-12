package com.ougnt.period_manager.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.joda.time.DateTime;

import java.util.LinkedList;

/**
 * * # Created by wacharint on 11/1/15.
 */
public class DatabaseRepositoryHelper extends SQLiteOpenHelper {

    public DatabaseRepositoryHelper(Context context) {
        super(context, "period_manager_core.db", null, 1);
        this.context = context;
    }

    public Context context;

    @Override
    public void onCreate(SQLiteDatabase db) {

        String creationString = "CREATE TABLE DATE_REPOSITORY (date DATE PRIMARY KEY, date_type INTEGER, comment VARCHAR DEFAULT '')";

        db.execSQL(creationString);

        String insertString = "INSERT INTO DATE_REPOSITORY VALUES %s";
        DateTime initialDateTime = DateTime.parse("2015-01-01");
        LinkedList<DateTime> dates = new LinkedList<>();

        for(int i = 0; i<= 3650; i++) {
            dates.add(initialDateTime.minusDays(-i));
        }

        for(int i = 0; i < dates.size(); i++) {
            insertString = String.format(insertString, "('" + dates.get(i).toString("yyyy-MM-dd") + "',0,''), %s");
            if(i % 500 == 0) {

                insertString = insertString.replace(", %s", "");
                db.execSQL(insertString);
                insertString = "INSERT INTO DATE_REPOSITORY VALUES %s";
            }
        }

        insertString = insertString.replace(", %s", "");

        db.execSQL(insertString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "DROP TABLE IF EXISTS DATE_REPOSITORY";

        db.execSQL(DROP_TABLE);

        onCreate(db);

    }
}
