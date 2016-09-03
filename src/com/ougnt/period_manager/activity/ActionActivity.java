package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ougnt.period_manager.*;
import com.ougnt.period_manager.handler.ActionActivityHelper;

import org.joda.time.Period;

import java.util.Locale;

/**
 * * # Created by wacharint on 12/13/15.
 */
public class ActionActivity extends Activity {

    public static final int ActionNothing = 0;
    public static final int ActionPeriodButton = 2;
    public static final int ActionNonPeriodButton = 4;
    public static final int ActionSaveButton = 8;
    public static final String ActionExtra = "ActionExtra";
    public static final String ActionTemperatureExtra = "ActionTemperatureExtra";
    public static final String ActionCommentExtra = "ActionCommentExtra";

    private ActionActivityHelper helper = new ActionActivityHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.action);

        populateContent();

        findViewById(R.id.empty_action).setOnClickListener(helper);
        findViewById(R.id.period_button).setOnClickListener(helper);
        findViewById(R.id.non_period_button).setOnClickListener(helper);
        findViewById(R.id.temperature_help).setOnClickListener(helper);
        findViewById(R.id.action_save_button).setOnClickListener(helper);
    }

    @Override
    public void onBackPressed() {

        helper.onBackPressed();
    }

    private void populateContent() {

        ((EditText)findViewById(R.id.action_comment)).setText(getIntent().getExtras().getString(ActionCommentExtra));
        ((EditText)findViewById(R.id.temperature_edittext)).setText(getIntent().getExtras().getFloat(ActionTemperatureExtra) + "");
    }
}

