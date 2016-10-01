package com.ougnt.period_manager.google;

import android.content.Context;
import android.content.SharedPreferences;

import com.ougnt.period_manager.activity.InitialActivity;

public class Log {

    private String language;
    private String deviceId;
    private String applicationVersion;
    private String screenType;
    private String category;
    private String action;

    public Log(Context context) {
        SharedPreferences sp = context.getSharedPreferences(InitialActivity.PName, Context.MODE_PRIVATE);
        language = sp.getString(InitialActivity.PSettingDisplayedLanguage, "Unknown");
        deviceId = sp.getString(InitialActivity.PUuid, "Unknown");
        applicationVersion = String.valueOf(sp.getInt(InitialActivity.PCurrentVersion, 0));
    }

    public String getLanguage() {
        return language;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getAction() {
        return action;
    }

    public String getCategory() {
        return category;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public class Category {
        public static final String Screen = "Screen";
        public static final String Button = "Button";
        public static final String Ads = "Ads";
    }

    public class Screen {
        public static final String MainScreenName = "Screen.Main";
        public static final String ActionPanel = "Screen.ActionPanel";
        public static final String MenuScreen = "Screen.Menu";
    }

    public class Action {
        public static final String Land = "Land";
        public static final String ClickDisplayToggleButtonFromDateScrollerToDateScroller = "ClickDisplayToggleButtonFromDateScrollerToDateScroller";
        public static final String ClickDisplayToggleButtonFromMonthViewToDateScroller = "ClickDisplayToggleButtonFromMonthViewToDateScroller";
        public static final String ClickDisplayToggleButtonFromTemperatureViewToDateScroller = "ClickDisplayToggleButtonFromTemperatureViewToDateScroller";
        public static final String ClickDisplayToggleFromUnknownToDateScroller = "ClickDisplayToggleFromUnknownToDateScroller";
        public static final String ClickDisplayToggleButtonFromDateScrollerToMonthView = "ClickDisplayToggleButtonFromDateScrollerToMonthView";
        public static final String ClickDisplayToggleButtonFromMonthViewToMonthView = "ClickDisplayToggleButtonFromMonthViewToMonthView";
        public static final String ClickDisplayToggleButtonFromTemperatureViewToMonthView = "ClickDisplayToggleButtonFromTemperatureViewToMonthView";
        public static final String ClickDisplayToggleFromUnknownToMonthView = "ClickDisplayToggleFromUnknownToMonthView";
        public static final String ClickDisplayToggleButtonFromDateScrollerToTemperature = "ClickDisplayToggleButtonFromDateScrollerToTemperature";
        public static final String ClickDisplayToggleButtonFromMonthViewToTemperature = "ClickDisplayToggleButtonFromMonthViewToTemperature";
        public static final String ClickDisplayToggleButtonFromTemperatureViewToTemperature = "ClickDisplayToggleButtonFromTemperatureViewToTemperature";
        public static final String ClickDisplayToggleFromUnknownToTemperature = "ClickDisplayToggleFromUnknownToTemperature";
        public static final String ActionButtonUnClick = "ActionButtonUnClick";
        public static final String ActionButtonClick = "ActionButtonClick";
        public static final String ActionClickTemperatureHelp = "ActionClickTemperatureHelp";
        public static final String ActionClickSaveButton = "ActionClickSaveButton";
        public static final String ClickAds = "ClickAds";
        public static final String ClickAddDetailToDateMeter = "ClickAddDetailToDateMeter";
        public static final String ClickAddDetailFromCalendar = "ClickAddDetailFromCalendar";
        public static final String LandToMenu = "LangToMenu";
        public static final String ClickLockScreen = "ClickLockScreen";
        public static final String ClickSubmitReview = "ClickSubmitReview";
        public static final String ClickLanguageSelector = "ClickLanguageSelector";
        public static final String ClickSummary = "ClickSummary";
        public static final String ClickHelp = "ClickHelp";
        public static final String ClickSetting = "ClickSetting";
        public static final String ClickEmotionIcon = "ClickEmotionIcon";
    }
}
