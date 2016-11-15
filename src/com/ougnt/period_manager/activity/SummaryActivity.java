package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.ougnt.period_manager.*;
import com.ougnt.period_manager.activity.extra.SummaryActivityExtra;

import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 12/22/15.
 */
public class SummaryActivity extends Activity {

    public DateTime menstrualFrom;
    public DateTime menstrualTo;
    public DateTime ovulationFrom;
    public DateTime ovulationTo;
    public DateTime ovulationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.summary);

        Intent intent = getIntent();
        int dateFormatFlag = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR;

        SummaryActivityExtra extra = SummaryActivityExtra.fromJson(intent.getExtras().getString(SummaryActivityExtra.SummaryActivityExtraExtra));

        menstrualFrom = extra.expectedNextMenstrualDateFrom;
        menstrualTo   = extra.expectedNextMenstrualDateTo;
        ovulationFrom = extra.expectedNextOvulationDateFrom;
        ovulationTo   = extra.expectedNextOvulationDateTo;
        ovulationDate = extra.expectedNextOvulationDate;

        String menstrualFromText = DateUtils.formatDateTime(this, menstrualFrom.getMillis(), dateFormatFlag);
        String menstrualToText = DateUtils.formatDateTime(this, menstrualTo.getMillis(), dateFormatFlag);
        String ovulationFromText = DateUtils.formatDateTime(this, ovulationFrom.getMillis(), dateFormatFlag);
        String ovulationToText = DateUtils.formatDateTime(this, ovulationTo.getMillis(), dateFormatFlag);
        String ovulationDateText = DateUtils.formatDateTime(this, ovulationDate.getMillis(), dateFormatFlag);

        TextView expectedMenstrual = (TextView)findViewById(R.id.expected_menstrual);
        TextView expectedOvulation = (TextView)findViewById(R.id.expected_ovulation);
        TextView expectedOvulationExact = (TextView) findViewById(R.id.expected_ovulation_exect);

        expectedMenstrual.setText(String.format(expectedMenstrual.getText().toString(), menstrualFromText, menstrualToText));
        expectedOvulation.setText(String.format(expectedOvulation.getText().toString(), ovulationFromText, ovulationToText));
        expectedOvulationExact.setText(String.format(expectedOvulationExact.getText().toString(), ovulationDateText));
    }

    public void close(View view) {
        Intent retIntent = new Intent();
        retIntent.putExtra(SummaryActivityExtra.SummaryActivityExtraExtra, getIntent().getExtras().getString(SummaryActivityExtra.SummaryActivityExtraExtra));
        setResult(RESULT_OK, retIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        close(null);
    }
}
