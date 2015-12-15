package com.ougnt.period_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting);

        Intent intent = getIntent();

        EditText periodCycle = (EditText)findViewById(R.id.period_cycle_input);
        EditText periodLength = (EditText)findViewById(R.id.period_length_input);

        periodCycle.setText(intent.getExtras().getFloat(PeriodCycleExtra) + "");
        periodLength.setText(intent.getExtras().getFloat(PeriodLengthExtra) + "");
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

        Intent retIntent = new Intent();
        retIntent.putExtra(ActionExtra, SaveAction);
        retIntent.putExtra(PeriodCycleExtra, Float.parseFloat(cycle));
        retIntent.putExtra(PeriodLengthExtra, Float.parseFloat(length));
        setResult(RESULT_OK, retIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        onCancelButtonClick(null);
    }
}
