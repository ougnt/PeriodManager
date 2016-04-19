package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ougnt.period_manager.*;

/**
 * * # Created by wacharint on 4/11/16.
 */
public class LanguageSelectorActivity extends Activity {

    public static final String LanguageExtra = "LanguageExtra";
    public static final String EnglishLanguage = "en";
    public static final String ThaiLanguage = "th";
    public static final String JapaneseLanguage = "ja";
    public static final String VietnameseLanguage = "vi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_selecter);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void englishOnclick(View view) {

        Intent retIntent = new Intent();
        retIntent.putExtra(LanguageExtra, EnglishLanguage);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    public void thaiOnclick(View view) {

        Intent retIntent = new Intent();
        retIntent.putExtra(LanguageExtra, ThaiLanguage);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    public void japaneseOnclick(View view) {

        Intent retIntent = new Intent();
        retIntent.putExtra(LanguageExtra, JapaneseLanguage);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    public void vietnameseOnclick(View view) {

        Intent retIntent = new Intent();
        retIntent.putExtra(LanguageExtra, VietnameseLanguage);
        setResult(RESULT_OK, retIntent);
        finish();
    }
}
