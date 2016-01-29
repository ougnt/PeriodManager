package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ougnt.period_manager.*;
import com.ougnt.period_manager.repository.DateRepository;
import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 1/19/16.
 */
public class MonthViewActivity extends Activity {

    public static final String MonthExtra = "MonthExtra";
    public static final String YearExtra = "TearExtra";

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

        loadDateTiView( calendar.dateRepositories[0][0], (TextView)findViewById(R.id.monthdate_11));
        loadDateTiView( calendar.dateRepositories[0][1], (TextView)findViewById(R.id.monthdate_12));
        loadDateTiView( calendar.dateRepositories[0][2], (TextView)findViewById(R.id.monthdate_13));
        loadDateTiView( calendar.dateRepositories[0][3], (TextView)findViewById(R.id.monthdate_14));
        loadDateTiView( calendar.dateRepositories[0][4], (TextView)findViewById(R.id.monthdate_15));
        loadDateTiView( calendar.dateRepositories[0][5], (TextView)findViewById(R.id.monthdate_16));
        loadDateTiView( calendar.dateRepositories[0][6], (TextView)findViewById(R.id.monthdate_17));

        loadDateTiView( calendar.dateRepositories[1][0], (TextView)findViewById(R.id.monthdate_21));
        loadDateTiView( calendar.dateRepositories[1][1], (TextView)findViewById(R.id.monthdate_22));
        loadDateTiView( calendar.dateRepositories[1][2], (TextView)findViewById(R.id.monthdate_23));
        loadDateTiView( calendar.dateRepositories[1][3], (TextView)findViewById(R.id.monthdate_24));
        loadDateTiView( calendar.dateRepositories[1][4], (TextView)findViewById(R.id.monthdate_25));
        loadDateTiView( calendar.dateRepositories[1][5], (TextView)findViewById(R.id.monthdate_26));
        loadDateTiView( calendar.dateRepositories[1][6], (TextView)findViewById(R.id.monthdate_27));

        loadDateTiView( calendar.dateRepositories[2][0], (TextView)findViewById(R.id.monthdate_31));
        loadDateTiView( calendar.dateRepositories[2][1], (TextView)findViewById(R.id.monthdate_32));
        loadDateTiView( calendar.dateRepositories[2][2], (TextView)findViewById(R.id.monthdate_33));
        loadDateTiView( calendar.dateRepositories[2][3], (TextView)findViewById(R.id.monthdate_34));
        loadDateTiView( calendar.dateRepositories[2][4], (TextView)findViewById(R.id.monthdate_35));
        loadDateTiView( calendar.dateRepositories[2][5], (TextView)findViewById(R.id.monthdate_36));
        loadDateTiView( calendar.dateRepositories[2][6], (TextView)findViewById(R.id.monthdate_37));

        loadDateTiView( calendar.dateRepositories[3][0], (TextView)findViewById(R.id.monthdate_41));
        loadDateTiView( calendar.dateRepositories[3][1], (TextView)findViewById(R.id.monthdate_42));
        loadDateTiView( calendar.dateRepositories[3][2], (TextView)findViewById(R.id.monthdate_43));
        loadDateTiView( calendar.dateRepositories[3][3], (TextView)findViewById(R.id.monthdate_44));
        loadDateTiView( calendar.dateRepositories[3][4], (TextView)findViewById(R.id.monthdate_45));
        loadDateTiView( calendar.dateRepositories[3][5], (TextView)findViewById(R.id.monthdate_46));
        loadDateTiView( calendar.dateRepositories[3][6], (TextView)findViewById(R.id.monthdate_47));

        loadDateTiView( calendar.dateRepositories[4][0], (TextView)findViewById(R.id.monthdate_51));
        loadDateTiView( calendar.dateRepositories[4][1], (TextView)findViewById(R.id.monthdate_52));
        loadDateTiView( calendar.dateRepositories[4][2], (TextView)findViewById(R.id.monthdate_53));
        loadDateTiView( calendar.dateRepositories[4][3], (TextView)findViewById(R.id.monthdate_54));
        loadDateTiView( calendar.dateRepositories[4][4], (TextView)findViewById(R.id.monthdate_55));
        loadDateTiView( calendar.dateRepositories[4][5], (TextView)findViewById(R.id.monthdate_56));
        loadDateTiView( calendar.dateRepositories[4][6], (TextView)findViewById(R.id.monthdate_57));

        loadDateTiView( calendar.dateRepositories[5][0], (TextView)findViewById(R.id.monthdate_61));
        loadDateTiView( calendar.dateRepositories[5][1], (TextView)findViewById(R.id.monthdate_62));
        loadDateTiView( calendar.dateRepositories[5][2], (TextView)findViewById(R.id.monthdate_63));
        loadDateTiView( calendar.dateRepositories[5][3], (TextView)findViewById(R.id.monthdate_64));
        loadDateTiView( calendar.dateRepositories[5][4], (TextView)findViewById(R.id.monthdate_65));
        loadDateTiView( calendar.dateRepositories[5][5], (TextView)findViewById(R.id.monthdate_66));
        loadDateTiView( calendar.dateRepositories[5][6], (TextView)findViewById(R.id.monthdate_67));

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
