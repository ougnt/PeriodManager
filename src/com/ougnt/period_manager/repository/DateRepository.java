package com.ougnt.period_manager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

/**
 * * # Created by wacharint on 11/1/15.
 */
public class DateRepository extends IDateRepository {

    private DateRepository(DateTime date, int dateType, String comment, float temperature, long flags) {

        this.date = date;
        this.dateType = dateType;
        this.comment = comment;
        this.temperature = temperature;
        this.flags = flags;
    }

    public static List<DateRepository> getDateRepositories(Context context, DateTime startDate, DateTime endDate) {

        if(dbHelper == null) {
            dbHelper = new DatabaseRepositoryHelper(context);
        }

        String[] columns = {"date", "date_type", "comment", "temperature_value", "flags"};
        Cursor cursor = dbHelper.getWritableDatabase().query(
                "DATE_REPOSITORY",
                columns,
                String.format(
                        "date BETWEEN '%s' AND '%s'",
                        startDate.toString("yyyy-MM-dd"),
                        endDate.toString("yyyy-MM-dd")),
                null,
                null,
                null,
                null);

        LinkedList<DateRepository> dates = new LinkedList<>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {

            dates.add(new DateRepository(DateTime.parse(cursor.getString(0)), cursor.getInt(1), cursor.getString(2), cursor.getFloat(3), cursor.getLong(4)));
            cursor.moveToNext();
        }

        cursor.close();
        return dates;
    }

    public static void updateDateRepositorySetDateType(Context context, DateTime targetDate, int newDateType) {

        if(dbHelper == null) {
            dbHelper = new DatabaseRepositoryHelper(context);
        }
        ContentValues values = new ContentValues();
        values.put("date_type", newDateType);

        dbHelper.getWritableDatabase().update(
                "DATE_REPOSITORY",
                values,
                "date = '" + targetDate.toString("yyyy-MM-dd") + "'",
                null);
    }

    public static void updateDateRepositorySetComment(Context context, DateTime targetDate, String comment) {

        if(dbHelper == null) {
            dbHelper = new DatabaseRepositoryHelper(context);
        }
        ContentValues values = new ContentValues();
        values.put("comment", comment);

        dbHelper.getWritableDatabase().update(
                "DATE_REPOSITORY",
                values,
                "date = '" + targetDate.toString("yyyy-MM-dd") + "'",
                null);

    }

    public static void updateDateRepositorySetTemperature(Context context, DateTime targetDate, float temperature) {

        if(dbHelper == null) {
            dbHelper = new DatabaseRepositoryHelper(context);
        }
        ContentValues values = new ContentValues();
        values.put("temperature_value", temperature);

        dbHelper.getWritableDatabase().update(
                "DATE_REPOSITORY",
                values,
                "date = '" + targetDate.toString("yyyy-MM-dd") + "'",
                null);

    }

    public static void updateDateRepositorySetFlags(Context context, DateTime targetDate, long flags) {

        if(dbHelper == null) {
            dbHelper = new DatabaseRepositoryHelper(context);
        }
        ContentValues values = new ContentValues();
        values.put("flags", flags);

        dbHelper.getWritableDatabase().update(
                "DATE_REPOSITORY",
                values,
                "date = '" + targetDate.toString("yyyy-MM-dd") + "'",
                null);

    }

    private static DatabaseRepositoryHelper dbHelper = null;
}
