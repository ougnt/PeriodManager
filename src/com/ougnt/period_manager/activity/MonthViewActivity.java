package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ougnt.period_manager.*;
import com.ougnt.period_manager.repository.DateRepository;
import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 1/19/16.
 */
public class MonthViewActivity extends Activity {

    public static final String MonthExtra = "MonthExtra";
    public static final String YearExtra = "YearExtra";

    public PeriodCalendar calendar;

    private int currentMonth;
    private int currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.month_view);

        Intent intent = getIntent();
        currentMonth = intent.getExtras().getInt(MonthExtra);
        currentYear = intent.getExtras().getInt(YearExtra);

        setupCalendar(currentMonth, currentYear);

        loadDatesToView();
    }

    public void nextMonth(View view) {

        if(currentMonth == 12) {

            currentMonth = 1;
            setupCalendar(currentMonth, ++currentYear);
        } else {

            setupCalendar(++currentMonth, currentYear);
        }

        loadDatesToView();

    }

    public void peviousMonth(View view) {

        if(currentMonth == 1) {

            currentMonth = 12;
            setupCalendar(currentMonth, --currentYear);
        } else {

            setupCalendar(--currentMonth, currentYear);
        }

        loadDatesToView();

    }

    private void setupCalendar(int month, int year) {

        calendar = new PeriodCalendar(this,
                currentMonth,
                currentYear);

        TextView monthText = (TextView)findViewById(R.id.calendar_month_text);
        monthText.setText(DateTime.parse(String.format("%s-%s-01", year, month)).toString("MMMM"));
    }

    private void loadDatesToView(){

        for(int row = 0; row < calendar.dateRepositories.length; row++) {

            for(int col = 0; col < calendar.dateRepositories[row].length; col++) {

                TextView targetLayout = (TextView)findViewById(getResources().getIdentifier(
                        String.format("monthdate_%s%s", row + 1, col + 1),
                        "id",
                        getPackageName()));

                if(calendar.dateRepositories[row][col].date.toString("yyyy-MM-dd").equals(DateTime.now().toString("yyyy-MM-dd"))) {

                    targetLayout.setBackgroundColor(getResources().getColor(R.color.today_text_color));
                } else {

                    targetLayout.setBackgroundColor(0);
                }

                loadDateTiView(
                        calendar.dateRepositories[row][col],
                        (TextView)findViewById(getResources().getIdentifier(
                                String.format("monthdate_%s%s", row + 1, col + 1),
                                "id",
                                getPackageName()
                        )));
            }
        }
    }

    private void loadDateTiView(DateRepository date, TextView view) {

        int color = 0;

        if(date.date.getMonthOfYear() != currentMonth) {

            color = getResources().getColor(R.color.calendar_other_month_text);
        } else {

            switch(date.dateType) {

                case DateMeter.Menstrual : color = getResources().getColor(R.color.calendar_period_text); break;
                case DateMeter.Ovulation : color = getResources().getColor(R.color.calendar_ovulation_text); break;
                default : color = getResources().getColor(R.color.calendar_text_color);
            }
        }

        view.setTextColor(color);
        view.setText(date.date.getDayOfMonth() + "");
    }

    public void close(View view) {

        finish();
    }
}
