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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * * # Created by wacharint on 12/13/15.
 */
public class MenuActivity extends Activity {

    public static final int SelectNothing = 0;
    public static final int SelectDisplayHelp = 1;
    public static final int SelectDisplaySetting = 2;
    public static final int SelectSummary = 4;
//    public static final int SelectMonthView = 8;
    public static final int SelectReview = 16;
    public static final int SelectLanguageSelecter = 32;
    public static final int SelectLockScreen = 64;
    public static final String SelectedMenuExtra = "SELECTED_MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinkedList menuClickAbles = new LinkedList();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu);

        TextView helpItem = (TextView)findViewById(R.id.help_menu);
        menuClickAbles.add(helpItem);

        TextView settingItem = (TextView)findViewById(R.id.setting_menu);
        menuClickAbles.add(settingItem);

        TextView reviewItem = (TextView)findViewById(R.id.submit_review_menu);
        menuClickAbles.add(reviewItem);

        TextView summary = (TextView)findViewById(R.id.summary_menu);
        menuClickAbles.add(summary);

        LinearLayout languageLayout = (LinearLayout)findViewById(R.id.setting_language_selecter);
        menuClickAbles.add(languageLayout);

        TextView lockLayout = (TextView)findViewById(R.id.lock_screen_menu);
        lockLayout.setVisibility(View.GONE);
        menuClickAbles.add(lockLayout);

        for(int i = 0 ; i < menuClickAbles.size(); i++) {

            ((View)menuClickAbles.get(i)).setOnClickListener(new MenuOnClickActionListener());
        }

        LinearLayout close1 = (LinearLayout)findViewById(R.id.close1);
        LinearLayout close2 = (LinearLayout)findViewById(R.id.close2);
        close1.setOnClickListener(new CloseActivity());
        close2.setOnClickListener(new CloseActivity());

        SharedPreferences pref = getSharedPreferences(InitialActivity.PName, MODE_PRIVATE);
        String language = pref.getString(InitialActivity.PSettingDisplayedLanguage, Locale.getDefault().getLanguage());
        switch (language) {
            case "en": {

                LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
                flagLayout.setBackgroundResource(R.drawable.flag_english);
                break;
            }
            case "ja": {

                LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
                flagLayout.setBackgroundResource(R.drawable.flag_japanese);
                break;
            }
            case "th": {

                LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
                flagLayout.setBackgroundResource(R.drawable.flag_thai);
                break;
            }
            case "vi": {

                LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
                flagLayout.setBackgroundResource(R.drawable.flag_vietnamese);
                break;
            }
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

    private class MenuOnClickActionListener implements View.OnClickListener {

        @Override
        public void onClick(View clickedMenu) {

            Intent retIntent = new Intent();

            switch(clickedMenu.getId()) {
                case R.id.lock_screen_menu: retIntent.putExtra(SelectedMenuExtra, SelectLockScreen); break;
                case R.id.submit_review_menu: retIntent.putExtra(SelectedMenuExtra, SelectReview); break;
                case R.id.setting_language_selecter: retIntent.putExtra(SelectedMenuExtra, SelectLanguageSelecter); break;
                case R.id.summary_menu: retIntent.putExtra(SelectedMenuExtra, SelectSummary); break;
                case R.id.help_menu: retIntent.putExtra(SelectedMenuExtra, SelectDisplayHelp); break;
                case R.id.setting_menu: retIntent.putExtra(SelectedMenuExtra, SelectDisplaySetting); break;
            }

            setResult(RESULT_OK, retIntent);
            finish();
        }
    }
}
