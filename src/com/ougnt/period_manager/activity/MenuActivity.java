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

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.helper.MenuActivityHelper;
import com.ougnt.period_manager.google.Log;

import java.util.LinkedList;
import java.util.Locale;

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

    public Log log;
    public MenuActivityHelper helper;
    public LinkedList<View> menuClickAbles;
    public LinearLayout close1;
    public LinearLayout close2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log = new Log(this);
        log.setScreenType(Log.Screen.MenuScreen);
        log.setCategory(Log.Category.Screen);
        log.setAction(Log.Action.LandToMenu);
        InitialActivity.sendTrafficMessage(log);

        getAllViews();

        SharedPreferences pref = getSharedPreferences(InitialActivity.PName, MODE_PRIVATE);
        String language = pref.getString(InitialActivity.PSettingDisplayedLanguage, Locale.getDefault().getLanguage());
        LinearLayout flagLayout = (LinearLayout) findViewById(R.id.flag_layout);
        switch (language) {
            case "en": {
                flagLayout.setBackgroundResource(R.drawable.flag_english);
                break;
            }
            case "ja": {
                flagLayout.setBackgroundResource(R.drawable.flag_japanese);
                break;
            }
            case "th": {
                flagLayout.setBackgroundResource(R.drawable.flag_thai);
                break;
            }
            case "vi": {
                flagLayout.setBackgroundResource(R.drawable.flag_vietnamese);
                break;
            }
            case "ru": {
                flagLayout.setBackgroundResource(R.drawable.flag_russian);
                break;
            }
        }

        helper = new MenuActivityHelper(this);
        registerOnclickListener();
    }

    @Override
    public void onBackPressed() {

        Intent retIntent = new Intent();
        retIntent.putExtra(SelectedMenuExtra, SelectNothing);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    public void registerOnclickListener() {

        for (int i = 0; i < menuClickAbles.size(); i++) {

            menuClickAbles.get(i).setOnClickListener(helper);
        }

        close1.setOnClickListener(helper);
        close2.setOnClickListener(helper);
    }

    public void getAllViews() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu);

        menuClickAbles = new LinkedList<>();

        TextView helpItem = (TextView) findViewById(R.id.help_menu);
        menuClickAbles.add(helpItem);

        TextView settingItem = (TextView) findViewById(R.id.setting_menu);
        menuClickAbles.add(settingItem);

        TextView reviewItem = (TextView) findViewById(R.id.submit_review_menu);
        menuClickAbles.add(reviewItem);

        TextView summary = (TextView) findViewById(R.id.summary_menu);
        menuClickAbles.add(summary);

        LinearLayout languageLayout = (LinearLayout) findViewById(R.id.setting_language_selecter);
        menuClickAbles.add(languageLayout);

        TextView lockLayout = (TextView) findViewById(R.id.lock_screen_menu);
        lockLayout.setVisibility(View.GONE);
        menuClickAbles.add(lockLayout);

        close1 = (LinearLayout) findViewById(R.id.close1);
        close2 = (LinearLayout) findViewById(R.id.close2);
    }
}
