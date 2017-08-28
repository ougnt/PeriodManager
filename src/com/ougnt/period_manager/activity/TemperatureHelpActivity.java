package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.ougnt.period_manager.R;

public class TemperatureHelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.temperature_help);

        WebView helpView = (WebView) findViewById(R.id.temperature_help_web_view);

        SharedPreferences pref = getSharedPreferences(InitialActivity.PName, MODE_PRIVATE);
        String displayedLanguage  = pref.getString(InitialActivity.PSettingDisplayedLanguage, "en");

        if (displayedLanguage.equals("th") ||
                displayedLanguage.equals("en") ||
                displayedLanguage.equals("ja")) {

        } else {

            displayedLanguage = "en";
        }

        String url = String.format("file:///android_asset/temperature_usage_%s.html", displayedLanguage);

        helpView.loadUrl(url);

        Button okButton = (Button) findViewById(R.id.help_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
