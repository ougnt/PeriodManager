package com.ougnt.period_manager;

import android.content.Context;
import android.widget.GridLayout;
import com.ougnt.period_manager.repository.MonthDate;

import java.util.HashMap;
import java.util.Map;

/**
 * * # Created by wacharint on 12/20/15.
 */
public class PeriodCalendar extends GridLayout {

    public HashMap<MonthDate, CalendarDate> dates;

    public PeriodCalendar(Context context, int displayedMonth, int selectedDate) {
        super(context);
        setColumnCount(7);
        setRowCount(6);
        dates.put(new MonthDate(1,1),null);
    }
}
