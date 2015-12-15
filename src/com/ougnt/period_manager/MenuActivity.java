package com.ougnt.period_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * * # Created by wacharint on 12/13/15.
 */
public class MenuActivity extends Activity {

    public static final int SelectNothing = 0;
    public static final int SelectDisplayHelp = 1;
    public static final int SelectDisplaySetting = 2;
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

        LinearLayout close1 = (LinearLayout)findViewById(R.id.close1);
        LinearLayout close2 = (LinearLayout)findViewById(R.id.close2);
        close1.setOnClickListener(new CloseActivity());
        close2.setOnClickListener(new CloseActivity());
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
}
