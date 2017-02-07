package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ougnt.period_manager.R;

/**
 * Created by wacharint on 2/5/2017 AD.
 * To make the usage easier
 */

public class InstructionActivity extends Activity {

    private static int step = 1;
    private LinearLayout dateMeterArea;
    private LinearLayout dateDetailArea;
    private TextView instructionText;
    private TextView instructionText2;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        if(step > 2) {
            finish();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.instruction_layout);
        getAllView();
    }

    public void getAllView() {
        dateMeterArea = (LinearLayout) findViewById(R.id.instruction_main_date_meter_scroller);
        dateDetailArea = (LinearLayout) findViewById(R.id.instruction_main_date_detail);
        instructionText = (TextView) findViewById(R.id.instruction_main_text);
        instructionText2 = (TextView) findViewById(R.id.instruction_click_add_detail);
    }

    public void onNextClick(View v) {

        switch(step++) {
            case 1 : {
                dateMeterArea.setBackgroundColor(ContextCompat.getColor(this, R.color.instruction_grey));
                dateDetailArea.setBackgroundColor(ContextCompat.getColor(this, R.color.instruction_hollow));
                instructionText.setVisibility(View.INVISIBLE);
                instructionText2.setVisibility(View.VISIBLE);
                break;
            }
            case 2 : {
                finish();
                break;
            }
            default: {
                finish();
            }
        }
    }
}
