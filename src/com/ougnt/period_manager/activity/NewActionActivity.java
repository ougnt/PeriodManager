package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.extra.InstructionComponent;
import com.ougnt.period_manager.activity.extra.NewInstructionActivityExtra;
import com.ougnt.period_manager.activity.helper.NewActionActivityHelper;
import com.ougnt.period_manager.google.Log;

import org.joda.time.DateTime;

import java.util.LinkedList;

public class NewActionActivity extends Activity {

    public static final String ExtraKey = "ExtraKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DateTime entering = DateTime.now();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.new_action);

        getAllViews();
        helper = new NewActionActivityHelper(this);
        registerOnClick();

        Log log = new Log(this);
        log.setCategory(Log.Category.LoadTime);
        log.setAction(Log.Action.ClickAddDetail);
        log.setScreenType(Log.Screen.ActionPanel);
        InitialActivity.sendLoadTimeMessage(log, DateTime.now().getMillis() - entering.getMillis());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showUsageIfFirstTime();
            }
        }, 500);
    }

    private void showUsageIfFirstTime() {
        SharedPreferences pref = getSharedPreferences(InitialActivity.PName, MODE_PRIVATE);

        if(pref.getBoolean(InitialActivity.PIsFirstTimeActionUsage, true)) {

            int[] actionButtonLocation = new int[2];
            actionButton.getLocationOnScreen(actionButtonLocation);
            InstructionComponent component1 = new InstructionComponent(
                    actionButtonLocation[0],
                    actionButtonLocation[1],
                    actionButton.getWidth(),
                    actionButton.getHeight(),
                    getResources().getString(R.string.instruction_add_detail_button)
            );

            int[] saveButtonLocation = new int[2];
            saveButton.getLocationOnScreen(saveButtonLocation);
            InstructionComponent component2 = new InstructionComponent(
                    saveButtonLocation[0],
                    saveButtonLocation[1],
                    saveButton.getWidth(),
                    saveButton.getHeight(),
                    getResources().getString(R.string.instruction_save_button)
            );

            LinkedList<InstructionComponent> components = new LinkedList<>();
            components.add(component2);
            components.add(component1);

            NewInstructionActivityExtra extra = NewInstructionActivityExtra.fromComponents(components);

            Intent intent = new Intent(this, NewInstructionActivity.class);
            intent.putExtra(NewInstructionActivity.InstructionExtra, extra.toJson());
            startActivity(intent);
        } else {
            return;
        }

        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(InitialActivity.PIsFirstTimeActionUsage, false);
        edit.apply();
    }

    private void registerOnClick() {

        actionButton.setOnClickListener(helper);
        temperatureHelp.setOnClickListener(helper);
        saveButton.setOnClickListener(helper);
        emotionSadIcon.setOnClickListener(helper);
        emotionNothingIcon.setOnClickListener(helper);
        emotionHappyIcon.setOnClickListener(helper);
        emotionAngryIcon.setOnClickListener(helper);
        emotionStressfulIcon.setOnClickListener(helper);
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
        emotionStressfulIcon = (ImageButton) findViewById(R.id.emotion_icon_stressful);
        noIntercourseIcon = (ImageButton) findViewById(R.id.action_panel_no_intercourse);
        intercourseIcon = (ImageButton) findViewById(R.id.action_panel_intercourse);
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
    public ImageButton emotionStressfulIcon;
    public ImageButton noIntercourseIcon;
    public ImageButton intercourseIcon;
    public LinearLayout adViewContainer;

    public NewActionActivityHelper helper;
}
