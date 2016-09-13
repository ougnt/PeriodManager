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
import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 12/22/15.
 */
public class SummaryActivity extends Activity {

    public static final String NextMenstrualFromExtra = "NextMenstrualFromExtra";
    public static final String NextMenstrualToExtra = "NextMenstrualToExtra";
    public static final String NextOvulationFromExtra = "NextOvulationFromExtra";
    public static final String NextOvulationToExtra = "NextOvulationToExtra";
    public static final String NextOvulationExtra = "NextOvulationExtra";

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

        menstrualFrom = DateTime.parse(intent.getExtras().getString(NextMenstrualFromExtra));
        menstrualTo   = DateTime.parse(intent.getExtras().getString(NextMenstrualToExtra));
        ovulationFrom = DateTime.parse(intent.getExtras().getString(NextOvulationFromExtra));
        ovulationTo   = DateTime.parse(intent.getExtras().getString(NextOvulationToExtra));
        ovulationDate = ovulationFrom.plusMillis((int) (ovulationTo.getMillis() - ovulationFrom.getMillis()) / 2);

        String menstrualFromText = DateUtils.formatDateTime(this, menstrualFrom.getMillis(), dateFormatFlag);
        String menstrualToText = DateUtils.formatDateTime(this, menstrualTo.getMillis(), dateFormatFlag);
        String ovulationFromText = DateUtils.formatDateTime(this, ovulationFrom.getMillis(), dateFormatFlag);
        String ovulationToText = DateUtils.formatDateTime(this, ovulationTo.getMillis(), dateFormatFlag);

        TextView expectedMenstrual = (TextView)findViewById(R.id.expected_menstrual);
        TextView expectedOvulation = (TextView)findViewById(R.id.expected_ovulation);

        expectedMenstrual.setText(String.format(expectedMenstrual.getText().toString(), menstrualFromText, menstrualToText));
        expectedOvulation.setText(String.format(expectedOvulation.getText().toString(), ovulationFromText, ovulationToText));
    }

    public void close(View view) {
        Intent retIntent = new Intent();
        retIntent.putExtra(NextMenstrualFromExtra, menstrualFrom.toString("yyyy-MM-dd"));
        retIntent.putExtra(NextMenstrualToExtra,   menstrualTo.toString("yyyy-MM-dd"));
        retIntent.putExtra(NextOvulationFromExtra, ovulationFrom.toString("yyyy-MM-dd"));
        retIntent.putExtra(NextOvulationToExtra,   ovulationTo.toString("yyyy-MM-dd"));
        retIntent.putExtra(NextOvulationExtra, ovulationDate.toString("yyyy-MM-dd"));
        setResult(RESULT_OK, retIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        close(null);
    }
}
