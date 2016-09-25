package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.helper.NewActionActivityHelper;

public class NewActionActivity extends Activity {

    public static final String ExtraKey = "ExtraKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.new_action);

        getAllViews();
        helper = new NewActionActivityHelper(this);
        registerOnClick();
    }

    private void registerOnClick() {

        actionButton.setOnClickListener(helper);
        temperatureHelp.setOnClickListener(helper);
        saveButton.setOnClickListener(helper);
    }

    public void getAllViews() {
        dateTextView = (TextView) findViewById(R.id.action_date_label);
        actionButton = (ImageButton) findViewById(R.id.action_action_button_image);
        actionButtonLabel = (TextView) findViewById(R.id.action_action_button_label);
        temperatureHelp = (TextView) findViewById(R.id.action_temperature_help);
        temperatureInput = (EditText) findViewById(R.id.action_temperature_edittext);
        commentInput = (EditText) findViewById(R.id.action_comment);
        saveButton = (Button) findViewById(R.id.action_save_button);
    }

    public TextView dateTextView;
    public ImageButton actionButton;
    public TextView actionButtonLabel;
    public TextView temperatureHelp;
    public EditText temperatureInput;
    public EditText commentInput;
    public Button saveButton;
    public NewActionActivityHelper helper;
}
