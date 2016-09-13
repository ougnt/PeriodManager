package com.ougnt.period_manager.repository;

import android.content.Context;
import android.database.Cursor;
import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 12/23/15.
 */
public class SummaryRepository {

    public DateTime expectedMenstrualDateFrom;
    public DateTime expectedMenstrualDateTo;
    public DateTime expectedOvulationDateFrom;
    public DateTime expectedOvulationDateTo;
    public DateTime expectedOvulationDate;

    public static SummaryRepository getSummary(Context context) {

        SummaryRepository ret = new SummaryRepository();
        DatabaseRepositoryHelper hp = new DatabaseRepositoryHelper(context);
        Cursor cursor = hp.getReadableDatabase().rawQuery(
                "SELECT exp_menstrual_from, exp_menstrual_to, exp_ovulation_from, exp_ovulation_to, exp_ovulation_date " +
                "FROM summary", null);

        if(cursor.getCount() == 0) {
            return null;
        } else {
            cursor.moveToFirst();
            ret.expectedMenstrualDateFrom = DateTime.parse(cursor.getString(0));
            ret.expectedMenstrualDateTo = DateTime.parse(cursor.getString(1));
            ret.expectedOvulationDateFrom = DateTime.parse(cursor.getString(2));
            ret.expectedOvulationDateTo = DateTime.parse(cursor.getString(3));
            if(cursor.getString(4) != null) {
                ret.expectedOvulationDate = DateTime.parse(cursor.getString(4));
            } else {
                ret.expectedOvulationDate = ret.expectedOvulationDateTo.plusMillis((int)(ret.expectedOvulationDateTo.getMillis() - ret.expectedOvulationDateFrom.getMillis())/2);
            }

            return ret;
        }
    }

    public void save(Context context) {

        DatabaseRepositoryHelper hp = new DatabaseRepositoryHelper(context);
        hp.getWritableDatabase().execSQL("DELETE FROM summary");
        hp.getWritableDatabase().execSQL(
                String.format("INSERT INTO summary VALUES ('%s', '%s', '%s', '%s', '%s')",
                        expectedMenstrualDateFrom.toString("yyyy-MM-dd"),
                        expectedMenstrualDateTo.toString("yyyy-MM-dd"),
                        expectedOvulationDateFrom.toString("yyyy-MM-dd"),
                        expectedOvulationDateTo.toString("yyyy-MM-dd"),
                        expectedOvulationDate.toString("yyyy-MM-dd")));
    }
}
