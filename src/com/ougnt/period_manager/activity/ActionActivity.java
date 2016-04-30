package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ougnt.period_manager.*;
import org.joda.time.Period;

import java.util.Locale;

/**
 * * # Created by wacharint on 12/13/15.
 */
public class ActionActivity extends Activity {

    public static final int ActionNothing = 0;
    public static final int ActionPeriodButton = 2;
    public static final int ActionNonPeriodButton = 4;
    public static final String ActionExtra = "ActionExtra";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.action);

        LinearLayout emptyLayout = (LinearLayout) findViewById(R.id.empty_action);
        ImageButton periodButton = (ImageButton) findViewById(R.id.period_button);
        ImageButton nonPeriodButton = (ImageButton) findViewById(R.id.non_period_button);

        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultAndFinish(ActionPeriodButton);
            }
        });

        nonPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultAndFinish(ActionNonPeriodButton);
            }
        });
    }

    @Override
    public void onBackPressed() {

        setResultAndFinish(ActionNothing);
    }

    private void setResultAndFinish(int resultCode) {

        Intent retIntent = new Intent();
        retIntent.putExtra(ActionExtra, resultCode);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    public void noAction(View view) {

        onBackPressed();
    }
}
