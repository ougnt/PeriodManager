package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ougnt.period_manager.*;
import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 12/22/15.
 */
public class SummaryActivity extends Activity {

    public static final String NextMenstrualFromExtra = "NextMenstrualFromExtra";
    public static final String NextMenstrualToExtra = "NextMenstrualToExtra";
    public static final String NextOvulationFromExtra = "NextOvulationFromExtra";
    public static final String NextOvulationToExtra = "NextOvulationToExtra";

    public String menstrualFrom;
    public String menstrualTo;
    public String ovulationFrom;
    public String ovulationTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.summary);

        Intent intent = getIntent();
        menstrualFrom = intent.getExtras().getString(NextMenstrualFromExtra);
        menstrualTo = intent.getExtras().getString(NextMenstrualToExtra);
        ovulationFrom = intent.getExtras().getString(NextOvulationFromExtra);
        ovulationTo = intent.getExtras().getString(NextOvulationToExtra);

        TextView expectedMenstrual = (TextView)findViewById(R.id.expected_menstrual);
        TextView expectedOvulation = (TextView)findViewById(R.id.expected_ovulation);
        expectedMenstrual.setText(menstrualFrom + " - " + menstrualTo);
        expectedOvulation.setText(ovulationFrom + " - " + ovulationTo);
    }

    public void close(View view) {
        finish();
    }
}
