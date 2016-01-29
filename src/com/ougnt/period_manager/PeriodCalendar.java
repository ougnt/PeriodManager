package com.ougnt.period_manager;

import android.content.Context;
import com.ougnt.period_manager.repository.DateRepository;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * * # Created by wacharint on 12/20/15.
 */
public class PeriodCalendar {

    public DateRepository[][] dateRepositories = new DateRepository[6][7];
    public int firstCellOfTheMonth, lengthOfMonth;
    public DateTime firstDateOfTheMonth, lastDateOfTheMonth;

    private Context _context;

    public PeriodCalendar(Context context, int displayedMonth, int displayedYear) {

        _context = context;
        getFirstDateLastDate(displayedMonth, displayedYear);
        load();
    }

    private void getFirstDateLastDate(int displayedMonth, int displayedYear) {

        firstDateOfTheMonth = DateTime.parse(String.format("%s-%s-01", displayedYear, displayedMonth));
        lastDateOfTheMonth = firstDateOfTheMonth.plusMonths(1).minusDays(1);
        firstCellOfTheMonth = firstDateOfTheMonth.getDayOfWeek();
        lengthOfMonth = lastDateOfTheMonth.getDayOfMonth() - firstDateOfTheMonth.getDayOfMonth() + 1;
    }

    private void load() {

        List<DateRepository> dates = DateRepository.getDateRepositories(_context, firstDateOfTheMonth.minusDays(firstCellOfTheMonth), lastDateOfTheMonth.plusDays(42-firstCellOfTheMonth-lengthOfMonth));
        int dateIndex = 0;

        while(dateIndex < lengthOfMonth) {

            for(int i = 0 ; i <= 5 ; i++) {

                for(int j = 0; j <= 6; j++) {

                    dateRepositories[i][j] = dates.get(dateIndex++);
                }
            }
        }
    }
}
