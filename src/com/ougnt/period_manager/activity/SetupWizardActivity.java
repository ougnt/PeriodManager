package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.helper.SetupWizardActivityHelper;

public class SetupWizardActivity extends Activity {

    public static final String ExtraKey = "ExtraKey";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_length_setup_panel);

        getAllViews();
        helper = new SetupWizardActivityHelper(this);

        setupScreen();
        registerOnClick();
    }

    @Override
    public void onBackPressed(){
        // do nothing
    }

    private void registerOnClick() {
        nextButton.setOnClickListener(helper);
//        nextButtonText.setOnClickListener(helper);
    }

    private void getAllViews() {
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        nextButton = (ImageButton) findViewById(R.id.setting_wizard_next_button);
        iDontKnowCheckBox = (CheckBox) findViewById(R.id.setting_wizard_i_dont_know_check_box);
        instructionText = (TextView) findViewById(R.id.setting_wizard_instruction_text);
        nextButtonText = (TextView) findViewById(R.id.setting_wizard_next_button_text);
    }

    private void setupScreen() {
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(7);
    }

    public ImageButton nextButton;
    public NumberPicker numberPicker;
    public CheckBox iDontKnowCheckBox;
    public TextView instructionText;
    public TextView nextButtonText;
    private SetupWizardActivityHelper helper;
}
