package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
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

    public DateTime menstrualFrom;
    public DateTime menstrualTo;
    public DateTime ovulationFrom;
    public DateTime ovulationTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.summary);

        Intent intent = getIntent();
        menstrualFrom = DateTime.parse(intent.getExtras().getString(NextMenstrualFromExtra));
        menstrualTo   = DateTime.parse(intent.getExtras().getString(NextMenstrualToExtra));
        ovulationFrom = DateTime.parse(intent.getExtras().getString(NextOvulationFromExtra));
        ovulationTo   = DateTime.parse(intent.getExtras().getString(NextOvulationToExtra));

        TextView expectedMenstrualFrom = (TextView)findViewById(R.id.expected_menstrual_from);
        TextView expectedOvulationFrom = (TextView)findViewById(R.id.expected_ovulation_from);
        TextView expectedMenstrualTo = (TextView)findViewById(R.id.expected_menstrual_to);
        TextView expectedOvulationTo = (TextView)findViewById(R.id.expected_ovulation_to);

        int dateFormatFlag = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR;

        expectedMenstrualFrom.setText(DateUtils.formatDateTime(this, menstrualFrom.getMillis(), dateFormatFlag));
        expectedMenstrualTo.setText(DateUtils.formatDateTime(this, menstrualTo.getMillis(), dateFormatFlag));
        expectedOvulationFrom.setText(DateUtils.formatDateTime(this, ovulationFrom.getMillis(), dateFormatFlag));
        expectedOvulationTo.setText(DateUtils.formatDateTime(this, ovulationTo.getMillis(), dateFormatFlag));

    }

    public void close(View view) {
        Intent retIntent = new Intent();
        retIntent.putExtra(NextMenstrualFromExtra, menstrualFrom.toString("yyyy-MM-dd"));
        retIntent.putExtra(NextMenstrualToExtra,   menstrualTo.toString("yyyy-MM-dd"));
        retIntent.putExtra(NextOvulationFromExtra, ovulationFrom.toString("yyyy-MM-dd"));
        retIntent.putExtra(NextOvulationToExtra,   ovulationTo.toString("yyyy-MM-dd"));
        setResult(RESULT_OK, retIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        close(null);
    }
}
