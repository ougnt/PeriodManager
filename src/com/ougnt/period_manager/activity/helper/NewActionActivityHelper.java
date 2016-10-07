package com.ougnt.period_manager.activity.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.phenotype.Flag;
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
            case R.id.action_panel_intercourse: {
                paintIntercourseIcon((ImageButton) v);
                intercourseFlag = FlagHelper.HaveIntercourseFlag;
                log.setAction(Log.Action.ClickIntercourseIcon);
                break;
            }
            case R.id.action_panel_no_intercourse: {
                paintIntercourseIcon((ImageButton) v);
                intercourseFlag = FlagHelper.HaventIntercourseFlag;
                log.setAction(Log.Action.ClickIntercourseIcon);
                break;
            }
        }

        InitialActivity.sendTrafficMessage(log);
    }

    private long getNewFlags() {

        return (extra.flags & ~FlagHelper.EmotionFlag & ~FlagHelper.IntercourseFlag) | emotionIconFlag | (intercourseFlag<<4);
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

        Handler adHandler = new Handler();
        adHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                while(activity.adViewContainer.getWidth() == 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                loadAdsToAdsContainer();
            }
        }, 500);
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

    private void loadAdsToAdsContainer() {

        AdRequest.Builder builder = new AdRequest.Builder();
        builder.setGender(AdRequest.GENDER_FEMALE);
        builder.addTestDevice("A759BF739C3F877B045FC80B4362590C");
        builder.addTestDevice("18EE9322E82A5EC6AFD6A29FDB693971");
        AdRequest request = builder.build();
        AdView adView = new AdView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = activity.adViewContainer.getWidth() / 4;
        width = width < 280 ? 280 : width;
        adView.setAdSize(new AdSize(width, AdSize.AUTO_HEIGHT));
        adView.setLayoutParams(params);
        adView.setAdUnitId("ca-app-pub-2522554213803646/1447188214");
        adView.loadAd(request);
        activity.adViewContainer.addView(adView);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                log.setScreenType(Log.Screen.ActionPanel);
                log.setCategory(Log.Category.Ads);
                log.setAction(Log.Action.AdFailedToLoad);
                InitialActivity.sendTrafficMessage(log);
            }
        });
        adView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.setScreenType(Log.Screen.ActionPanel);
                log.setCategory(Log.Category.Ads);
                log.setAction(Log.Action.NativeAdClick);
                InitialActivity.sendTrafficMessage(log);
            }
        });
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
        activity.intercourseIcon.setLayoutParams(params);
        activity.noIntercourseIcon.setLayoutParams(params);

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

        switch (FlagHelper.GetIntercourseFlag(extra.flags)) {
            case FlagHelper.HaveIntercourseFlag: {
                paintIntercourseIcon(activity.intercourseIcon);
                break;
            }
            case FlagHelper.HaventIntercourseFlag: {
                paintIntercourseIcon(activity.noIntercourseIcon);
                break;
            }
        }
        intercourseFlag = FlagHelper.GetIntercourseFlag(extra.flags);
        emotionIconFlag = FlagHelper.GetEmotionFlag(extra.flags);
    }

    private void paintIntercourseIcon(ImageButton clickedIcon) {
        activity.noIntercourseIcon.setBackgroundColor(0);
        activity.intercourseIcon.setBackgroundColor(0);
        clickedIcon.setBackgroundColor(ContextCompat.getColor(activity, R.color.on_select_zone_bg));
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
    private long intercourseFlag;

}
