package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.ougnt.period_manager.repository.HelpIndicatorRepository;
import com.ougnt.period_manager.*;

/**
 * * # Created by wacharint on 12/11/15.
 */
public class HelpActivity extends Activity {

    private int indicator = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        Intent inIntent = getIntent();
        indicator = inIntent.getExtras().getInt("INDICATOR");
        boolean isCheckBoxCheck = (indicator & 1) == 1;
        CheckBox noShowCheckbox = (CheckBox) findViewById(R.id.no_more_help_checkbox);
        noShowCheckbox.setChecked(isCheckBoxCheck);
    }

    @Override
    public void onBackPressed() {

        saveAndExit(null);
    }

    public void saveAndExit(View view) {

        CheckBox noShowCheckbox = (CheckBox) findViewById(R.id.no_more_help_checkbox);
        if(noShowCheckbox.isChecked()) {
            indicator = indicator | 1;
        } else {
            indicator = (HelpIndicatorRepository.MaxIndicator - 1) & indicator;
        }

        Intent retIntent = new Intent();
        retIntent.putExtra("INDICATOR", indicator);
        setResult(Activity.RESULT_OK, retIntent);
        finish();
    }
}
