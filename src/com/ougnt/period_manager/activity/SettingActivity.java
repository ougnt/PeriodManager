package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import com.ougnt.period_manager.*;

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
    public static final String IsNotifyPeriodCheckExtra = "IsNotifyPeriodCheckExtra";
    public static final String IsNotifyOvulationCheckExtra = "IsNotifyOvulationCheckExtra";
    public static final String NotifyPeriodDaysExtra = "NotifyPeriodDaysExtra";
    public static final String NotifyOvulationDaysExtra = "NotifyOvulationDaysExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setting);

        Intent intent = getIntent();

        EditText periodCycle = (EditText)findViewById(R.id.period_cycle_input);
        EditText periodLength = (EditText)findViewById(R.id.period_length_input);

        CheckBox notifyPeriodCheckbox = (CheckBox)findViewById(R.id.notify_period_checkbox);
        CheckBox notifyOvulationCheckbox = (CheckBox)findViewById(R.id.notify_ovulation_checkbox);
        EditText notifyPeriodDay = (EditText)findViewById(R.id.period_warn_date_edittext);
        EditText notifyOvulationDay = (EditText)findViewById(R.id.ovulation_warn_date_edittext);

        notifyPeriodCheckbox.setChecked(intent.getExtras().getInt(IsNotifyPeriodCheckExtra, 0) == 1);
        notifyOvulationCheckbox.setChecked(intent.getExtras().getInt(IsNotifyOvulationCheckExtra, 0) == 1);

        notifyPeriodDay.setText(String.valueOf(intent.getExtras().getInt(NotifyPeriodDaysExtra, 1)));
        notifyOvulationDay.setText(String.valueOf(intent.getExtras().getInt(NotifyOvulationDaysExtra, 1)));

        periodCycle.setText(String.valueOf(intent.getExtras().getFloat(PeriodCycleExtra)));
        periodLength.setText(String.valueOf(intent.getExtras().getFloat(PeriodLengthExtra)));
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
        CheckBox notifyPeriodCheckbox = (CheckBox)findViewById(R.id.notify_period_checkbox);
        CheckBox notifyOvulationCheckBox = (CheckBox)findViewById(R.id.notify_ovulation_checkbox);
        EditText notifyPeriodDays = (EditText)findViewById(R.id.period_warn_date_edittext);
        EditText notifyOvulationDays = (EditText)findViewById(R.id.ovulation_warn_date_edittext);

        String length = periodLength.getText().toString();
        String cycle = periodCycle.getText().toString();
        Boolean isPeriodNotify = notifyPeriodCheckbox.isChecked();
        Boolean isOvulationNotify = notifyOvulationCheckBox.isChecked();
        int periodNotifyDays = Integer.parseInt(notifyPeriodDays.getText().toString());
        int ovulationNotifyDays = Integer.parseInt(notifyOvulationDays.getText().toString());

        if(length.isEmpty()) {
            length = "7";
        }

        if(cycle.isEmpty()) {
            cycle = "28";
        }

        Intent retIntent = new Intent();
        retIntent.putExtra(ActionExtra, SaveAction);
        retIntent.putExtra(PeriodCycleExtra, Float.parseFloat(cycle));
        retIntent.putExtra(PeriodLengthExtra, Float.parseFloat(length));
        retIntent.putExtra(IsNotifyPeriodCheckExtra,isPeriodNotify);
        retIntent.putExtra(IsNotifyOvulationCheckExtra, isOvulationNotify);
        retIntent.putExtra(NotifyPeriodDaysExtra, periodNotifyDays);
        retIntent.putExtra(NotifyOvulationDaysExtra, ovulationNotifyDays);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        onCancelButtonClick(null);
    }
}
