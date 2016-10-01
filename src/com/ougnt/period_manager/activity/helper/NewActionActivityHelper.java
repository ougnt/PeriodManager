package com.ougnt.period_manager.activity.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.activity.NewActionActivity;
import com.ougnt.period_manager.activity.TemperatureHelpActivity;
import com.ougnt.period_manager.activity.extra.ActionActivityExtra;
import com.ougnt.period_manager.google.Log;

import static com.ougnt.period_manager.DateMeter.Menstrual;
import static com.ougnt.period_manager.DateMeter.Nothing;
import static com.ougnt.period_manager.DateMeter.OvulationDate;
import static com.ougnt.period_manager.DateMeter.PossiblyOvulation;


public class NewActionActivityHelper implements View.OnClickListener {

    public NewActionActivityHelper(NewActionActivity activity) {
        this.activity = activity;
        extra = parseExtras();
        log = new Log(activity);
        log.setScreenType(Log.Screen.ActionPanel);
        setUpDisplay();
    }

    @Override
    public void onClick(View v) {
        log.setCategory(Log.Category.Button);

        switch (v.getId()) {
            case R.id.action_action_button_image: {

                if (isActionButtonPushed) {
                    activity.actionButton.setBackground(ContextCompat.getDrawable(activity, R.drawable.blue_button));
                    isActionButtonPushed = false;

                    log.setAction(Log.Action.ActionButtonUnClick);
                } else {
                    activity.actionButton.setBackground(ContextCompat.getDrawable(activity, R.drawable.blue_button_push));
                    isActionButtonPushed = true;

                    log.setAction(Log.Action.ActionButtonClick);
                }
                break;
            }
            case R.id.action_temperature_help: {
                Intent intent = new Intent(activity, TemperatureHelpActivity.class);
                log.setAction(Log.Action.ActionClickTemperatureHelp);
                activity.startActivity(intent);
                break;
            }
            case R.id.action_save_button: {
                Intent retIntent = new Intent();

                extra.temperature = Double.parseDouble(activity.temperatureInput.getText().toString());
                extra.comment = String.valueOf(activity.commentInput.getText());
                extra.isButtonPush = isActionButtonPushed;
                extra.isCancel = false;
                extra.flags = getNewFlags();
                retIntent.putExtra(NewActionActivity.ExtraKey, extra.toJson());
                activity.setResult(Activity.RESULT_OK, retIntent);

                log.setAction(Log.Action.ActionClickSaveButton);
                InitialActivity.sendTrafficMessage(log);

                activity.finish();
                break;
            }
            case R.id.emotion_icon_angry:{
                paintEmotionIconClick((ImageButton) v);
                emotionIconFlag = FlagHelper.EmotionAngryIcon;
                log.setAction(Log.Action.ClickEmotionIcon);
                break;
            }
            case R.id.emotion_icon_happy:{
                paintEmotionIconClick((ImageButton) v);
                emotionIconFlag = FlagHelper.EmotionHappyIcon;
                log.setAction(Log.Action.ClickEmotionIcon);
                break;
            }
            case R.id.emotion_icon_nothing:{
                paintEmotionIconClick((ImageButton) v);
                emotionIconFlag = FlagHelper.EmotionNothingIcon;
                log.setAction(Log.Action.ClickEmotionIcon);
                break;
            }
            case R.id.emotion_icon_sad: {
                paintEmotionIconClick((ImageButton) v);
                emotionIconFlag = FlagHelper.EmotionSadIcon;
                log.setAction(Log.Action.ClickEmotionIcon);
                break;
            }
        }

        InitialActivity.sendTrafficMessage(log);
    }

    private long getNewFlags() {

        return (extra.flags & ~FlagHelper.EmotionFlag) | emotionIconFlag;
    }

    public void setUpDisplay() {

        activity.dateTextView.setText(extra.date.toString(activity.getResources().getString(R.string.short_date_format)));
        activity.actionButtonLabel.setText(getButtonText(extra.dateType));
        String temperatureDisplay = extra.temperature + "";
        activity.temperatureInput.setText(temperatureDisplay);
        activity.commentInput.setText(extra.comment);
        isActionButtonPushed = false;
        formatIcon();

        log.setCategory(Log.Category.Screen);
        log.setAction(Log.Action.Land);
        InitialActivity.sendTrafficMessage(log);
    }

    public String getButtonText(int dateType) {
        switch (dateType) {
            case Menstrual:
                return activity.getResources().getString(R.string.action_activity_click_me_if_this_day_is_not_your_period);
            case Nothing:
            case OvulationDate:
            case PossiblyOvulation:
            default:
                return activity.getResources().getString(R.string.action_activity_click_me_if_your_period_start_this_day);
        }
    }

    private ActionActivityExtra parseExtras() {
        String key = activity.getIntent().getExtras().getString(NewActionActivity.ExtraKey);
        return ActionActivityExtra.fromJsonString(key);
    }

    private void formatIcon() {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x == 0 ? 2400 : size.x;

        int iconSize = width / 4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iconSize, iconSize);
        activity.emotionAngryIcon.setLayoutParams(params);
        activity.emotionHappyIcon.setLayoutParams(params);
        activity.emotionNothingIcon.setLayoutParams(params);
        activity.emotionSadIcon.setLayoutParams(params);

        switch(FlagHelper.GetEmotionFlag(extra.flags)) {
            case FlagHelper.EmotionAngryIcon: {
                paintEmotionIconClick(activity.emotionAngryIcon);
                break;
            }
            case FlagHelper.EmotionHappyIcon: {
                paintEmotionIconClick(activity.emotionHappyIcon);
                break;
            }
            case FlagHelper.EmotionNothingIcon: {
                paintEmotionIconClick(activity.emotionNothingIcon);
                break;
            }
            case FlagHelper.EmotionSadIcon: {
                paintEmotionIconClick(activity.emotionSadIcon);
                break;
            }
        }
    }

    private void paintEmotionIconClick(ImageButton clickedIcon) {
        activity.emotionAngryIcon.setBackgroundColor(0);
        activity.emotionHappyIcon.setBackgroundColor(0);
        activity.emotionSadIcon.setBackgroundColor(0);
        activity.emotionNothingIcon.setBackgroundColor(0);
        clickedIcon.setBackgroundColor(ContextCompat.getColor(activity, R.color.on_select_zone_bg));
    }

    public NewActionActivity activity;
    public ActionActivityExtra extra;
    public boolean isActionButtonPushed;
    public Log log;
    private long emotionIconFlag;
}
