package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import com.ougnt.period_manager.*;
import com.ougnt.period_manager.repository.SettingRepository;

/**
 * * # Created by wacharint on 12/15/15.
 */
public class SettingActivity extends Activity {

    public static final int CancelAction = 0;
    public static final int SaveAction = 1;
    public static final String ActionExtra = "Action";
    public static final String PeriodCycleExtra = "PeriodCycle";
    public static final String PeriodLengthExtra = "PeriodLength";
    public static final String AverageLengthExtra = "AversionLength";
    public static final String AverageCycleExtra = "AverageCycle";
    public static final String CountExtra = "Count";
    public static final String FlagExtra = "Flag";

    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setting);

        Intent intent = getIntent();

        EditText periodCycle = (EditText)findViewById(R.id.period_cycle_input);
        EditText periodLength = (EditText)findViewById(R.id.period_length_input);

        periodCycle.setText(intent.getExtras().getFloat(PeriodCycleExtra) + "");
        periodLength.setText(intent.getExtras().getFloat(PeriodLengthExtra) + "");

        flag = (getIntent().getExtras().getInt(FlagExtra));

        if( (flag & SettingRepository.FlagCalendarMonthView) == SettingRepository.FlagCalendarMonthView) {

            ((RadioButton)findViewById(R.id.month_view_selector)).setChecked(true);
        } else {

            ((RadioButton)findViewById(R.id.day_view_selector)).setChecked(true);
        }
    }

    public void onCancelButtonClick(View view){

        Intent retIntent = new Intent();
        retIntent.putExtra(ActionExtra, CancelAction);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    public void onOkButtonClick(View view) {

        EditText periodLength = (EditText)findViewById(R.id.period_length_input);
        EditText periodCycle = (EditText)findViewById(R.id.period_cycle_input);

        String length = periodLength.getText().toString();
        String cycle = periodCycle.getText().toString();

        if(length.isEmpty()) {
            length = "7";
        }

        if(cycle.isEmpty()) {
            cycle = "28";
        }

        if(((RadioButton)findViewById(R.id.month_view_selector)).isChecked()) {

            flag = flag | SettingRepository.FlagCalendarMonthView;
        } else {

            flag = flag & (SettingRepository.MaxIndicator - SettingRepository.FlagCalendarMonthView);
        }

        Intent retIntent = new Intent();
        retIntent.putExtra(ActionExtra, SaveAction);
        retIntent.putExtra(PeriodCycleExtra, Float.parseFloat(cycle));
        retIntent.putExtra(PeriodLengthExtra, Float.parseFloat(length));
        retIntent.putExtra(FlagExtra, flag);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        onCancelButtonClick(null);
    }
}
