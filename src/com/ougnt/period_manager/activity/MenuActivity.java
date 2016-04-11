package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ougnt.period_manager.*;

import java.util.Locale;

/**
 * * # Created by wacharint on 12/13/15.
 */
public class MenuActivity extends Activity {

    public static final int SelectNothing = 0;
    public static final int SelectDisplayHelp = 1;
    public static final int SelectDisplaySetting = 2;
    public static final int SelectSummary = 4;
    public static final int SelectMonthView = 8;
    public static final int SelectReview = 16;
    public static final int SelectLanguageSelecter = 32;
    public static final String SelectedMenuExtra = "SELECTED_MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu);

        TextView helpItem = (TextView)findViewById(R.id.help_menu);
        helpItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent = new Intent();
                retIntent.putExtra(SelectedMenuExtra, SelectDisplayHelp);
                setResult(RESULT_OK, retIntent);
                finish();
            }
        });

        TextView settingItem = (TextView)findViewById(R.id.setting_menu);
        settingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent = new Intent();
                retIntent.putExtra(SelectedMenuExtra, SelectDisplaySetting);
                setResult(RESULT_OK, retIntent);
                finish();
            }
        });

        TextView reviewItem = (TextView)findViewById(R.id.submit_review_menu);
        reviewItem.setOnClickListener(new OpenReview());

        TextView summary = (TextView)findViewById(R.id.summary_menu);
        summary.setOnClickListener(new ShowSummary());

        TextView monthView = (TextView)findViewById(R.id.month_view_menu);
        monthView.setOnClickListener(new ShowMonthView());

        LinearLayout languageLayout = (LinearLayout)findViewById(R.id.setting_language_selecter);
        languageLayout.setOnClickListener(new ShowLanguageSelecter());


        LinearLayout close1 = (LinearLayout)findViewById(R.id.close1);
        LinearLayout close2 = (LinearLayout)findViewById(R.id.close2);
        close1.setOnClickListener(new CloseActivity());
        close2.setOnClickListener(new CloseActivity());

        SharedPreferences pref = getSharedPreferences(InitialActivity.PName, MODE_PRIVATE);
        String language = pref.getString(InitialActivity.PSettingDisplayedLanguage, Locale.getDefault().getLanguage());
        if(language.equals("en")) {

            LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
            flagLayout.setBackgroundResource(R.drawable.flag_english);
        } else if (language.equals("ja")) {

            LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
            flagLayout.setBackgroundResource(R.drawable.flag_japanese);
        } else if (language.equals("th")) {

            LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
            flagLayout.setBackgroundResource(R.drawable.flag_thai);
        }
    }

    @Override
    public void onBackPressed() {

        Intent retIntent = new Intent();
        retIntent.putExtra(SelectedMenuExtra, SelectNothing);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    private class CloseActivity implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent retIntent = new Intent();
            retIntent.putExtra(SelectedMenuExtra, SelectNothing);
            setResult(RESULT_OK, retIntent);
            finish();
        }
    }

    private class ShowSummary implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent retIntent = new Intent();
            retIntent.putExtra(SelectedMenuExtra, SelectSummary);
            setResult(RESULT_OK, retIntent);
            finish();
        }
    }

    private class ShowLanguageSelecter implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent retIntent = new Intent();
            retIntent.putExtra(SelectedMenuExtra, SelectLanguageSelecter);
            setResult(RESULT_OK, retIntent);
            finish();
        }
    }

    private class ShowMonthView implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent retIntent = new Intent();
            retIntent.putExtra(SelectedMenuExtra, SelectMonthView);
            setResult(RESULT_OK, retIntent);
            finish();
        }
    }

    private class OpenReview implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent retIntent = new Intent();
            retIntent.putExtra(SelectedMenuExtra, SelectReview);
            setResult(RESULT_OK, retIntent);
            finish();
        }
    }
}
