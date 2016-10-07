package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.helper.NewActionActivityHelper;

public class NewActionActivity extends Activity {

    public static final String ExtraKey = "ExtraKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        emotionSadIcon.setOnClickListener(helper);
        emotionNothingIcon.setOnClickListener(helper);
        emotionHappyIcon.setOnClickListener(helper);
        emotionAngryIcon.setOnClickListener(helper);
        intercourseIcon.setOnClickListener(helper);
        noIntercourseIcon.setOnClickListener(helper);
    }

    public void getAllViews() {
        dateTextView = (TextView) findViewById(R.id.action_date_label);
        actionButton = (ImageButton) findViewById(R.id.action_action_button_image);
        actionButtonLabel = (TextView) findViewById(R.id.action_action_button_label);
        temperatureHelp = (TextView) findViewById(R.id.action_temperature_help);
        temperatureInput = (EditText) findViewById(R.id.action_temperature_edittext);
        commentInput = (EditText) findViewById(R.id.action_comment);
        saveButton = (Button) findViewById(R.id.action_save_button);
        emotionNothingIcon = (ImageButton) findViewById(R.id.emotion_icon_nothing);
        emotionHappyIcon = (ImageButton) findViewById(R.id.emotion_icon_happy);
        emotionSadIcon = (ImageButton) findViewById(R.id.emotion_icon_sad);
        emotionAngryIcon = (ImageButton) findViewById(R.id.emotion_icon_angry);
        noIntercourseIcon = (ImageButton) findViewById(R.id.action_panel_no_intercourse);
        intercourseIcon = (ImageButton) findViewById(R.id.action_panel_intercourse);
//        adView = (AdView) findViewById(R.id.action_panel_ads_view);
        adViewContainer = (LinearLayout) findViewById(R.id.action_panel_ad_view_container);
    }

    public TextView dateTextView;
    public ImageButton actionButton;
    public TextView actionButtonLabel;
    public TextView temperatureHelp;
    public EditText temperatureInput;
    public EditText commentInput;
    public Button saveButton;
    public ImageButton emotionNothingIcon;
    public ImageButton emotionHappyIcon;
    public ImageButton emotionSadIcon;
    public ImageButton emotionAngryIcon;
    public ImageButton noIntercourseIcon;
    public ImageButton intercourseIcon;
    public LinearLayout adViewContainer;
    public AdView adView;

    public NewActionActivityHelper helper;
}
