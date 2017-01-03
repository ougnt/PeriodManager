package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.PeriodCalendar;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.extra.ActionActivityExtra;
import com.ougnt.period_manager.activity.extra.SetupWizardActivityExtra;
import com.ougnt.period_manager.activity.extra.SummaryActivityExtra;
import com.ougnt.period_manager.event.BroadcastNotificationPublisher;
import com.ougnt.period_manager.event.ChartFetchingOnclickHandler;
import com.ougnt.period_manager.event.OnDateMeterFocusListener;
import com.ougnt.period_manager.google.Log;
import com.ougnt.period_manager.handler.ChartHandler;
import com.ougnt.period_manager.handler.HttpHelper;
import com.ougnt.period_manager.repository.DatabaseRepositoryHelper;
import com.ougnt.period_manager.repository.DateRepository;
import com.ougnt.period_manager.repository.FetchingButton;
import com.ougnt.period_manager.repository.HelpIndicatorRepository;
import com.ougnt.period_manager.repository.SettingRepository;
import com.ougnt.period_manager.repository.SummaryRepository;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.joda.time.DateTime.now;

public class InitialActivity extends Activity {

    //    final int EditComment = 0x01;
    final int DisplayHelp = 0x02;
    final int DisplayMenu = 0x04;
    final int DisplaySetting = 0x08;
    final int DisplaySummary = 0x10;
    final int DisplayLanguageSelector = 0x20;
    final int DisplayNewActionPanel = 0x80;
    final int DisplaySettingWizard = 0x100;

    public static final int ApplicationVersion = 71;

    // TODO : Change this to the real one
    // Live Env
    public static final String StatServer = "27.254.81.190:5555";
    // Dev env
//    public static final String StatServer = "192.168.56.1:9000";
    public static final String StatUri = String.format("http://%s/usageStat", StatServer);
    public static final String AdsRequestUri = String.format("http://%s/adsAsk", StatServer);
    public static final String AdsClickUri = String.format("http://%s/adsClick", StatServer);
    public static final String ErrorLogUri = String.format("http://%s/errorLog", StatServer);
    public static final String ReviewUrl = String.format("http://%s/sendReview", StatServer);

    public static final String PName = "period_manager_preference";
    public static final String PUuid = "period_manager_preference_uuid";
    public static final String PUsageCounter = "period_manager_preference_usage_counter";
    public static final String PTimeOfUsageBeforeReview = "period_manager_preference_time_of_usage_before_review";
    public static final String PPeriodButtonUsageCounter = "period_manager_preference_period_button_usage_counter";
    public static final String PNonPeriodButtonUsageCounter = "period_manager_preference_non_period_button_usage_counter";
    public static final String PCommentButtonUsageCounter = "period_manager_preference_comment_button_usage_counter";
    public static final String PCommentTextUsageCounter = "period_manager_preference_comment_text_usage_counter";
    public static final String PMenuButtonUsageCounter = "period_manager_preference_menu_button_usage_counter";
    public static final String PCurrentVersion = "period_manager_preference_current_version";

    public static final String PReviewNow = "period_manager_preference_review_now";
    public static final String PReviewLater = "period_manager_preference_review_later";
    public static final String PNoReview = "period_manager_preference_review_non";

    public static final String PFetchNextMonthUsageCounter = "period_manager_preference_fetch_next_usage_counter";
    public static final String PFetchPreviousMonthUsageCounter = "period_manager_preference_fetch_previous_usage_counter";

    public static final String PMenuSettingClickCounter = "period_manager_preference_menu_setting_click_counter";
    public static final String PMenuSummaryClickCounter = "period_manager_preference_menu_summary_click_counter";
    public static final String PMenuMonthViewClickCounter = "period_manager_preference_menu_month_view_click_counter";
    public static final String PMenuHelpClickCounter = "period_manager_preference_menu_help_click_counter";
    public static final String PMenuReviewClickCounter = "period_manager_preference_menu_review_click_counter";

    // Available in version 26
    public static final String PSettingIsNotifyPeriod = "period_manager_preference_setting_is_notify_period";
    public static final String PSettingIsNotifyOvulation = "period_manager_preference_setting_is_notify_ovulation";
    public static final String PSettingNotifyPeriodDay = "period_manager_preference_setting_notify_period_day";
    public static final String PSettingNotifyOvulationDay = "period_manager_preference_setting_notify_ovulation_day";
    public static final String PSettingNotifyPeriodCounter = "period_manager_preference_setting_notify_period_counter";
    public static final String PSettingNotifyOvulationCounter = "period_manager_preference_setting_notify_ovulation_counter";
    public static final String PSettingNotificationClickCounter = "period_manager_preference_setting_notification_click_counter";

    // Available in version 29
    public static final String PSettingDisplayedLanguage = "period_manager_preference_setting_displayed_language";
    public static final String PSettingDisplayedLanguageUsageCounter = "period_manager_preference_setting_displayed_language_usage_counter";

    // Available in version 36
    // TODO: Submit this usage counter
    public static final String PMainDisplayMode = "period_manager_preference_display_mode";

    // Available in version 47
    // TODO: Submit this usage counter
    public static final String PMenuLockScreenUsageCounter = "period_manager_preference_lock_screen_usage_counter";

    public static final int DisplayModeDateScroller = 0;
    public static final int DisplayModeMonthView = 1;
    public static final int DisplayModeChartView = 2;
    public static final int DisplayModeConclusionView = 4;
    public static DateTime startTime = now();

    // Available in version 69
    public static final int ILikeTheApplication = 0x01;
    public static final int IDontLikeTheApplication = 0x02;
    public static final int IDontLikeTheApplicationBecauseItIsSlow = 0x04;
    public static final int IDontLikeTheApplicationBecauseItIsHardToUse = 0x08;
    public static final int IDontLikeTheApplicationBecauseTheDesignIsNotLookProfessional = 0x10;
    public static final int IDontLikeTheApplicationBecauseTheLanguageIsHardToUnderstand = 0x20;
    public static final int IDontLikeTheApplicationBecauseTheLanguageDoesNotLookProfessional = 0x40;
    public static final int IDontLikeTheApplicationBecauseOtherApplicationIsBetter = 0x80;
    public static final int IDontLikeTheApplicationBecauseOfOtherReason = 0x100;
    public static final int ILikeTheApplicationAndIWantToReview = 0x200;
    public static final int ILikeTheApplicationButIDontLikeToReviewNow = 0x400;
    public static final int ILikeTheApplicationButIDontLikeToReview = 0x800;

    SettingRepository setting;

    public InitialActivity() {
        dateTouchListener = null;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            startTime = now();
            analyticsApplication = (AppForStatic) getApplication();
            tracker = tracker == null ? analyticsApplication.getTracker() : tracker;

            if (getIntent().getExtras() == null ||
                    getIntent().getExtras().size() > 0 ||
                    getIntent().getExtras().get(BroadcastNotificationPublisher.ExtraOpenFromNotification) != null) {

                addUsageCounter(PSettingNotificationClickCounter);
            }

            initUsageToReview();
            setApplicationVersion();
            setDeviceId();
            addUsageCounter(PUsageCounter);

            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
            String language = pref.getString(PSettingDisplayedLanguage, Locale.getDefault().getLanguage());
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            setSharedPreference(PSettingDisplayedLanguage, Locale.getDefault().getLanguage());

            log = new Log(this);
            log.setAction(Log.Action.Land);
            log.setCategory(Log.Category.Screen);
            log.setScreenType(Log.Screen.MainScreenName);
            sendTrafficMessage(log);

            initialApplication();
            long loadTime = DateTime.now().getMillis() - startTime.getMillis();

            log.setCategory(Log.Category.LoadTime);
            log.setAction("InitialActivity.OnCreate");
            sendLoadTimeMessage(log, loadTime);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            isAdjusted = false;
            startTime = now();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    @Override
    protected void onRestart() {
        try {
            super.onRestart();
            isAdjusted = false;
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void initialApplication() {

        try {
            setContentView(R.layout.main);

            DateTime latest = DateTime.now();

            getAllViews();

            log.setCategory(Log.Category.LoadTime);
            log.setAction("InitialActivity.getAllView");
            sendLoadTimeMessage(log, DateTime.now().getMillis() - latest.getMillis());

            latest = DateTime.now();

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.date_detail, newActionPanel);

            log.setAction("InitialActivity.inflater");
            sendLoadTimeMessage(log, DateTime.now().getMillis() - latest.getMillis());
            latest = DateTime.now();

            addDateMeterView();

            log.setAction("InitialActivity.addDateMeterView");
            sendLoadTimeMessage(log, DateTime.now().getMillis() - latest.getMillis());
            latest = DateTime.now();

            addMonthView();

            log.setAction("InitialActivity.addMonthView");
            sendLoadTimeMessage(log, DateTime.now().getMillis() - latest.getMillis());

            adjustLayoutForDisplayModeAccordingToPDisplayMode();

            getAllViews();
            dateDetailActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    log.setCategory(Log.Category.Button);
                    log.setAction(Log.Action.ClickAddDetailToDateMeter);
                    log.setScreenType(Log.Screen.MainScreenName);
                    sendTrafficMessage(log);

                    ActionActivityExtra extra = new ActionActivityExtra();
                    extra.date = selectedDate.getDate();
                    extra.dateType = selectedDate.dateType;
                    extra.temperature = selectedDate.temperature;
                    extra.comment = selectedDate.comment;
                    extra.flags = selectedDate.getFlags();
                    Intent intent = new Intent(getBaseContext(), NewActionActivity.class);
                    intent.putExtra(NewActionActivity.ExtraKey, extra.toJson());
                    startActivityForResult(intent, DisplayNewActionPanel);
                }
            });

            dateMeterScroller.setOnTouchListener(new View.OnTouchListener() {

                private ViewTreeObserver observer;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    observer = observer == null ? dateMeterScroller.getViewTreeObserver() : observer;

                    observer.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                        @Override
                        public void onScrollChanged() {

                            setSelectedDateToAlignWithFingerIndex();
                        }
                    });
                    return false;
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        AdRequest.Builder adBuilder = new AdRequest.Builder();
                        adBuilder.setGender(AdRequest.GENDER_FEMALE);
                        final AdRequest adRequest = adBuilder.build();
                        adMobLayout.setVisibility(View.GONE);

                        Handler adHandler = new Handler();
                        adHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adView = new AdView(getBaseContext());

                                int width = getResources().getConfiguration().screenWidthDp;
                                adView.setAdSize(new AdSize(width, 80));
                                adView.setAdUnitId("ca-app-pub-2522554213803646/4225526617");

                                adView.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdLoaded() {
                                        adMobLayout.removeAllViews();
                                        adMobLayout.addView(adView);
                                        adMobLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        adMobLayout.setVisibility(View.VISIBLE);
                                        super.onAdLoaded();
                                    }

                                    @Override
                                    public void onAdOpened() {
                                        log.setAction(Log.Action.ClickAds);
                                        log.setCategory(Log.Category.Ads);
                                        sendTrafficMessage(log);
                                    }
                                });
                                adView.loadAd(adRequest);
                            }
                        }, 500);

                    } catch (Exception e) {
                        HttpHelper.sendErrorLog(e);
                    }
                }
            }, 1000);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private int getExperimentVariance() {

        if (getDeviceId() == null) {
            setDeviceId();
        }

        return (int) (getDeviceId().getMostSignificantBits() % 2);
    }

    private void setSelectedDateToAlignWithFingerIndex() {
        try {
            int[] fingerIndexLocator = new int[2];
            int[] dateMeterLocator = new int[2];

            fingerIndex.getLocationOnScreen(fingerIndexLocator);

            for (int i = 1; i < dateMeterContainer.getChildCount() - 2; i++) {
                DateMeter targetDateMeter = (DateMeter) dateMeterContainer.getChildAt(i);
                targetDateMeter.getLocationOnScreen(dateMeterLocator);
                int fingerIndexPointerX = fingerIndexLocator[0] + fingerIndex.getWidth() / 3;

                if (fingerIndexPointerX > dateMeterLocator[0] &&
                        fingerIndexPointerX < dateMeterLocator[0] + targetDateMeter.getWidth()) {

                    if (selectedDate == null) {
                        selectedDate = targetDateMeter;
                    }
                    if (selectedDate.getDate() == targetDateMeter.getDate()) return;

                    selectedDate = targetDateMeter;
                    targetDateMeter.makeSelectedFormat();
                    targetDateMeter.setSelected(true);
                } else {
                    if (targetDateMeter.isSelected()) {
                        targetDateMeter.resetFormat();
                        targetDateMeter.setSelected(false);
                    }
                }
            }
            TextView headerText = (TextView) findViewById(R.id.main_header_text);
            if (selectedDate == null) {
                selectedDate = (DateMeter) dateMeterContainer.getChildAt(16);
            }
            headerText.setText(selectedDate.getDate().toString(getResources().getText(R.string.short_date_format).toString()));
        } catch (Resources.NotFoundException e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void addDateMeterView() {

        try {
            setOnDateMeterTouchEventListener(new OnDateMeterFocusListener() {
                @Override
                public void onFocusMoveIn(DateMeter touchDate) {

                    ImageView fireImage = (ImageView) findViewById(R.id.fire_image);
                    ImageView grassImage = (ImageView) findViewById(R.id.grass_image);
                    ImageView sunImage = (ImageView) findViewById(R.id.sun_image);
                    ImageView beachImage = (ImageView) findViewById(R.id.beach_image);

                    switch (touchDate.dateType) {
                        case DateMeter.Menstrual: {
                            fireImage.setVisibility(View.VISIBLE);
                            grassImage.setVisibility(View.GONE);
                            sunImage.setVisibility(View.GONE);
                            beachImage.setVisibility(View.GONE);
                            break;
                        }
                        case DateMeter.PossiblyOvulation: {
                            fireImage.setVisibility(View.GONE);
                            grassImage.setVisibility(View.GONE);
                            sunImage.setVisibility(View.VISIBLE);
                            beachImage.setVisibility(View.VISIBLE);
                            break;
                        }
                        case DateMeter.Nothing: {
                            fireImage.setVisibility(View.GONE);
                            grassImage.setVisibility(View.VISIBLE);
                            sunImage.setVisibility(View.VISIBLE);
                            beachImage.setVisibility(View.GONE);
                            break;
                        }
                    }

                    setDateDetailText(touchDate);
                }
            });

            addDateMeter(dateMeterContainer, now().minusDays(15), now().plusDays(15), true);

            dateMeterContainer.addView(generateEndLayout(dateMeterContainer, true));
            dateMeterContainer.addView(generateEndLayout(dateMeterContainer, false), 0);

            setting = SettingRepository.getSettingRepository(this);

            moveDateMeterToCurrentDate();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void setDateDetailText(DateMeter touchDate) {

        try {
            final TextView todayText = (TextView) findViewById(R.id.date_detail_text);
            SummaryRepository summary = SummaryRepository.getSummary(getBaseContext());
            if (summary == null) {
                todayText.setText(getResources().getString(R.string.date_detail_no_detail_yet));
                return;
            }
            int nextOvulationIn = (int) ((summary.expectedOvulationDate.getMillis() - touchDate.getDate().getMillis()) / 1000 / 60 / 60 / 24);
            int nextPeriodIn = (int) ((summary.expectedMenstrualDateFrom.getMillis() - touchDate.getDate().getMillis()) / 1000 / 60 / 60 / 24);
            String explainationText =
                    touchDate.dateType == DateMeter.Menstrual ?
                            getResources().getString(R.string.date_detail_small_chance) :
                            touchDate.dateType == DateMeter.PossiblyOvulation ?
                                    getResources().getString(R.string.date_detail_have_some_change) :
                                    touchDate.dateType == DateMeter.OvulationDate ?
                                            getResources().getString(R.string.date_detail_ovulation_date) :
                                            "";

            String estNextOvu = nextOvulationIn > 0 ?
                    String.format(getResources().getString(R.string.date_detail_est_next_ovulation), nextOvulationIn) :
                    "";
            String estNextMens = nextPeriodIn > 0 ?
                    String.format(getResources().getString(R.string.date_detail_est_next_period), nextPeriodIn) :
                    "";

            String displayText = explainationText + estNextOvu + estNextMens;
            todayText.setText(displayText);
            final String[] lines = todayText.getText().toString().split("\n");

            todayText.post(new Runnable() {
                @Override
                public void run() {
                    if (todayText.getLineCount() > 4) {
                        todayText.setTag(1);
                        todayText.setText(Html.fromHtml(String.format("%s<p><font color='#0000EE'>%s</font></p>", lines[0], getResources().getString(R.string.date_detail_click_to_see_more))));
                        todayText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((int) v.getTag() == 1) {
                                    todayText.setText(Html.fromHtml(String.format("%s<p><font color='#0000EE'>%s</font></p>", lines[0], getResources().getString(R.string.date_detail_click_to_see_more))));
                                    v.setTag(2);
                                } else if ((int) v.getTag() == 2) {
                                    todayText.setText(Html.fromHtml(String.format("%s<p><font color='#0000EE'>%s</font></p>", lines[1], getResources().getString(R.string.date_detail_click_to_see_more))));
                                    v.setTag(3);
                                } else {
                                    todayText.setText(Html.fromHtml(String.format("%s<p><font color='#0000EE'>%s</font></p>", lines[2], getResources().getString(R.string.date_detail_click_to_see_more))));
                                    v.setTag(1);
                                }
                            }
                        });
                    }
                }
            });
        } catch (Resources.NotFoundException e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void addMonthView() {

        try {
            DateTime calendarDate = selectedDate == null ? now() : selectedDate.getDate();

            setupCalendar(calendarDate.getMonthOfYear(), calendarDate.getYear());
            loadDatesToMonthView();

            TextView nextMonth = (TextView) findViewById(R.id.next_month);
            TextView previousMonth = (TextView) findViewById(R.id.previous_month);

            nextMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCalendarToNextMonth();
                }
            });

            previousMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCalendarToPreviousMonth();
                }
            });
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void setupCalendar(int month, int year) {

        try {
            calendarCurrentMonth = month;
            calendarCurrentYear = year;
            calendar = new PeriodCalendar(this,
                    calendarCurrentMonth,
                    calendarCurrentYear);

            TextView monthText = (TextView) findViewById(R.id.calendar_view_month_text);
            monthText.setText(DateTime.parse(String.format("%s-%s-01", year, month)).toString("MMMM"));
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void loadDatesToMonthView() {

        try {
            for (int row = 0; row < calendar.dateRepositories.length; row++) {

                for (int col = 0; col < calendar.dateRepositories[row].length; col++) {

                    final TextView targetLayout = (TextView) findViewById(getResources().getIdentifier(
                            String.format("calendar_monthdate_%s%s", row + 1, col + 1),
                            "id",
                            getPackageName()));

                    if (calendar.dateRepositories[row][col].date.toString("yyyy-MM-dd").equals(now().toString("yyyy-MM-dd"))) {

                        Drawable bg = ContextCompat.getDrawable(this, R.drawable.circle_ink);
                        targetLayout.setBackground(bg);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics().widthPixels / 8);
                        targetLayout.setLayoutParams(params);

                    } else {

                        targetLayout.setBackgroundColor(0);
                    }

                    loadDateToView(
                            calendar.dateRepositories[row][col],
                            (TextView) findViewById(getResources().getIdentifier(
                                    String.format("calendar_monthdate_%s%s", row + 1, col + 1),
                                    "id",
                                    getPackageName()
                            )));

                    final DateTime tempDate = calendar.dateRepositories[row][col].date;
                    targetLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DateRepository date = DateRepository.getDateRepositories(getBaseContext(), tempDate, tempDate).get(0);

                            for (int i = 1; i < dateMeterContainer.getChildCount() - 1; i++) {

                                // TODO : Bug : Handle null selectedDate since the start time is before the first datemeter
                                if (((DateMeter) dateMeterContainer.getChildAt(i)).getDate().toString("yyyy-MM-dd").equals(date.date.toString("yyyy-MM-dd"))) {
                                    selectedDate = (DateMeter) dateMeterContainer.getChildAt(i);
                                    break;
                                }
                            }

                            log.setCategory(Log.Category.Button);
                            log.setScreenType(Log.Screen.MainScreenName);
                            log.setAction(Log.Action.ClickAddDetailFromCalendar);
                            sendTrafficMessage(log);

                            ActionActivityExtra extra = new ActionActivityExtra();
                            extra.date = selectedDate.getDate();
                            extra.dateType = selectedDate.dateType;
                            extra.temperature = selectedDate.temperature;
                            extra.comment = selectedDate.comment;
                            Intent intent = new Intent(getBaseContext(), NewActionActivity.class);
                            intent.putExtra(NewActionActivity.ExtraKey, extra.toJson());
                            startActivityForResult(intent, DisplayNewActionPanel);
                        }
                    });
                }
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void loadDateToView(DateRepository date, TextView view) {

        try {
            int color;

            if (date.date.getMonthOfYear() != calendarCurrentMonth) {

                color = ContextCompat.getColor(this, R.color.calendar_other_month_text);
            } else {

                switch (date.dateType) {

                    case DateMeter.Menstrual:
                        color = ContextCompat.getColor(this, R.color.calendar_period_text);
                        break;
                    case DateMeter.PossiblyOvulation:
                        color = ContextCompat.getColor(this, R.color.calendar_possibly_ovulation_text);
                        break;
                    case DateMeter.OvulationDate:
                        color = ContextCompat.getColor(this, R.color.calendar_ovulation_text);
                        break;
                    default:
                        color = ContextCompat.getColor(this, R.color.calendar_text_color);
                }
            }

            view.setTextColor(color);
            String displayText = date.date.getDayOfMonth() + "";
            view.setText(displayText);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            switch (requestCode) {
                case DisplayHelp: {
                    HelpIndicatorRepository.setIndicator(this, data.getIntExtra("INDICATOR", 1));
                    break;
                }
                case DisplayMenu: {
                    onDisplayMenuResult(data);
                    break;
                }
                case DisplaySetting: {

                    onDisplaySettingResult(data);
                    break;
                }
                case DisplaySettingWizard: {
                    onDisplaySettingWizardResult(data);
                    break;
                }
                case DisplaySummary: {

                    String jsonSummary = data.getExtras().getString(SummaryActivityExtra.SummaryActivityExtraExtra);
                    SummaryActivityExtra extra = SummaryActivityExtra.fromJson(jsonSummary);
                    assert extra != null;
                    SummaryRepository summary = extra.toSummaryRepository();

                    summary.save(this);
                    break;
                }
                case DisplayLanguageSelector: {

                    if (resultCode == RESULT_CANCELED) {
                        break;
                    }

                    String language = data.getExtras().getString(LanguageSelectorActivity.LanguageExtra);
                    Locale locale = new Locale(language);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    initialApplication();
                    setSharedPreference(PSettingDisplayedLanguage, language);
                    break;
                }
                case DisplayNewActionPanel: {

                    DateTime startProcessDisplayNewActionPanel = DateTime.now();
                    if (data == null) return;
                    ActionActivityExtra extra = ActionActivityExtra.fromJsonString(data.getExtras().getString(NewActionActivity.ExtraKey));

                    if (extra.isButtonPush) {
                        if (extra.dateType == DateMeter.Menstrual) {
                            removePeriodAndOvulationFlagToDateMeter();
                        } else {
                            addPeriodAndOvulationFlagToDateMeters();
                        }
                    }

                    if(extra.isTemperatureChange) {
                        log.setAction(Log.Action.ChangeTemperature);
                        log.setCategory(Log.Category.Button);
                        log.setScreenType(Log.Screen.ActionPanel);
                        sendTrafficMessage(log);
                    }

                    for (int i = 1; i < dateMeterContainer.getChildCount() - 1; i++) {

                        if (((DateMeter) dateMeterContainer.getChildAt(i)).getDate().toString("yyyy-MM-dd").equals(extra.date.toString("yyyy-MM-dd"))) {
                            selectedDate = (DateMeter) dateMeterContainer.getChildAt(i);
                            selectedDate.comment = extra.comment;
                            selectedDate.temperature = (float) extra.temperature;
                            selectedDate.setFlags(extra.flags);

                            DateRepository.updateDateRepositorySetComment(this, selectedDate.getDate(), selectedDate.comment);
                            DateRepository.updateDateRepositorySetTemperature(this, selectedDate.getDate(), selectedDate.temperature);
                            DateRepository.updateDateRepositorySetFlags(this, selectedDate.getDate(), selectedDate.getFlags());

                            selectedDate = new DateMeter(this,
                                    DateRepository.getDateRepositories(this, selectedDate.getDate(), selectedDate.getDate()).get(0),
                                    dateTouchListener);
                            dateMeterContainer.removeViewAt(i);
                            dateMeterContainer.addView(selectedDate, i);
                            break;
                        }
                    }

                    addMonthView();

                    log.setAction(Log.Action.ActionClickSaveButton);
                    log.setScreenType(Log.Screen.ActionPanel);
                    log.setCategory(Log.Category.LoadTime);
                    sendLoadTimeMessage(log, DateTime.now().getMillis() - startProcessDisplayNewActionPanel.getMillis());
                }
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }


    static Boolean isReviewing = false;

    @Override
    public void onBackPressed() {

        try {
            final SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
            final SharedPreferences.Editor edit = pref.edit();
            final int[] survayFlags = new int[1];

            if (getUsageCounter(PUsageCounter) == getUsageCounter(PTimeOfUsageBeforeReview) && !isReviewing && ApplicationVersion < 1000) {

                isReviewing = true;
                setContentView(R.layout.do_you_like_app);
                Button iLikeThisAppButton = (Button) findViewById(R.id.i_like_this_app_button);
                Button iDontLikeThisAppButton = (Button) findViewById(R.id.i_dont_like_this_app_button);

                iLikeThisAppButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        log.setAction(Log.Action.ReviewILikeThisApplication);
                        log.setScreenType(Log.Screen.ReviewPanel);
                        log.setCategory(Log.Category.Button);
                        sendTrafficMessage(log);

                        setContentView(R.layout.review);
                        survayFlags[0] |= ILikeTheApplication;

                        Button reviewNowButton = (Button) findViewById(R.id.review_open);
                        Button laterButton = (Button) findViewById(R.id.review_later);
                        Button noShowButton = (Button) findViewById(R.id.review_no_show);

                        reviewNowButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                log.setAction(Log.Action.ReviewILikeThisApplicationAndReviewNow);
                                log.setScreenType(Log.Screen.ReviewPanel);
                                log.setCategory(Log.Category.Button);
                                sendTrafficMessage(log);

                                addUsageCounter(PReviewNow);
                                survayFlags[0] |= ILikeTheApplicationAndIWantToReview;
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ougnt.period_manager")));
                                submitStat();
                                finish();
                            }
                        });

                        laterButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                log.setAction(Log.Action.ReviewILikeThisApplicationButReviewLater);
                                log.setScreenType(Log.Screen.ReviewPanel);
                                log.setCategory(Log.Category.Button);
                                sendTrafficMessage(log);

                                addUsageCounter(PReviewLater);
                                survayFlags[0] |= ILikeTheApplicationButIDontLikeToReviewNow;
                                edit.putInt(PTimeOfUsageBeforeReview, pref.getInt(PTimeOfUsageBeforeReview, 0) + 10);
                                edit.apply();
                                submitStat();
                                finish();
                            }
                        });

                        noShowButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                log.setAction(Log.Action.ReviewILikeThisApplicationButIDontWantToReview);
                                log.setScreenType(Log.Screen.ReviewPanel);
                                log.setCategory(Log.Category.Button);
                                sendTrafficMessage(log);

                                addUsageCounter(PNoReview);
                                survayFlags[0] |= ILikeTheApplicationButIDontLikeToReview;
                                edit.putInt(PTimeOfUsageBeforeReview, -1);
                                edit.apply();
                                submitStat();
                                finish();
                            }
                        });
                    }
                });

                iDontLikeThisAppButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        log.setAction(Log.Action.ReviewIDontLikeThisApplication);
                        log.setScreenType(Log.Screen.ReviewPanel);
                        log.setCategory(Log.Category.Button);
                        sendTrafficMessage(log);

                        setContentView(R.layout.we_also_love_a_negative_feedback);
                        survayFlags[0] |= IDontLikeTheApplication;
                        Button sendReviewButton = (Button) findViewById(R.id.review_send_review_button);
                        sendReviewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CheckBox hardToUseCheckBox = (CheckBox) findViewById(R.id.review_it_is_hard_to_use);
                                CheckBox slowCheckBox = (CheckBox) findViewById(R.id.review_it_is_slow);
                                CheckBox otherApplicationIsBetterCheckBox = (CheckBox) findViewById(R.id.review_other_app_is_better);
                                CheckBox designDoesNotLookProfCheckBox = (CheckBox) findViewById(R.id.review_the_design_does_not_look_prof);
                                CheckBox languageDoesNotLookProfCheckBox = (CheckBox) findViewById(R.id.review_the_language_does_not_look_prof);
                                CheckBox languageIsHardToUnderstandCheckBox = (CheckBox) findViewById(R.id.review_the_language_is_hard_to_understand);
                                CheckBox otherReasonCheckBox = (CheckBox) findViewById(R.id.review_other_reason);

                                survayFlags[0] |= hardToUseCheckBox.isChecked() ? IDontLikeTheApplicationBecauseItIsHardToUse : 0;
                                survayFlags[0] |= slowCheckBox.isChecked() ? IDontLikeTheApplicationBecauseItIsSlow : 0;
                                survayFlags[0] |= otherApplicationIsBetterCheckBox.isChecked() ? IDontLikeTheApplicationBecauseOtherApplicationIsBetter : 0;
                                survayFlags[0] |= designDoesNotLookProfCheckBox.isChecked() ? IDontLikeTheApplicationBecauseTheDesignIsNotLookProfessional : 0;
                                survayFlags[0] |= languageDoesNotLookProfCheckBox.isChecked() ? IDontLikeTheApplicationBecauseTheLanguageDoesNotLookProfessional : 0;
                                survayFlags[0] |= languageIsHardToUnderstandCheckBox.isChecked() ? IDontLikeTheApplicationBecauseTheLanguageIsHardToUnderstand : 0;
                                survayFlags[0] |= otherReasonCheckBox.isChecked() ? IDontLikeTheApplicationBecauseOfOtherReason : 0;

                                if( (survayFlags[0] & IDontLikeTheApplicationBecauseItIsHardToUse) > 0) {

                                    log.setAction(Log.Action.ReviewIDontLikeThisApplicationBecauseItIsHardToUse);
                                    log.setCategory(Log.Category.Button);
                                    log.setScreenType(Log.Screen.ReviewPanel);
                                    sendTrafficMessage(log);
                                } else if( (survayFlags[0] & IDontLikeTheApplicationBecauseItIsSlow) > 0) {

                                    log.setAction(Log.Action.ReviewIDontLikeThisApplicationBecauseItIsSlow);
                                    log.setCategory(Log.Category.Button);
                                    log.setScreenType(Log.Screen.ReviewPanel);
                                    sendTrafficMessage(log);

                                } else if( (survayFlags[0] & IDontLikeTheApplicationBecauseOtherApplicationIsBetter) > 0) {

                                    log.setAction(Log.Action.ReviewIDontLikeThisApplicationBecauseOtherApplicationIsBetter);
                                    log.setCategory(Log.Category.Button);
                                    log.setScreenType(Log.Screen.ReviewPanel);
                                    sendTrafficMessage(log);

                                } else if( (survayFlags[0] & IDontLikeTheApplicationBecauseTheDesignIsNotLookProfessional) > 0) {

                                    log.setAction(Log.Action.ReviewIDontLikeThisApplicationBecauseTheDesignIsNotLookProfessional);
                                    log.setCategory(Log.Category.Button);
                                    log.setScreenType(Log.Screen.ReviewPanel);
                                    sendTrafficMessage(log);

                                } else if( (survayFlags[0] & IDontLikeTheApplicationBecauseTheLanguageDoesNotLookProfessional) > 0) {

                                    log.setAction(Log.Action.ReviewIDontLikeThisApplicationBecauseTheLanguageDoesNotLookProfessional);
                                    log.setCategory(Log.Category.Button);
                                    log.setScreenType(Log.Screen.ReviewPanel);
                                    sendTrafficMessage(log);

                                } else if( (survayFlags[0] & IDontLikeTheApplicationBecauseTheLanguageIsHardToUnderstand) > 0) {

                                    log.setAction(Log.Action.ReviewIDontLikeThisApplicationBecauseTheLanguageIsHardToUnderstand);
                                    log.setCategory(Log.Category.Button);
                                    log.setScreenType(Log.Screen.ReviewPanel);
                                    sendTrafficMessage(log);

                                } else if( (survayFlags[0] & IDontLikeTheApplicationBecauseOfOtherReason) > 0) {

                                    log.setAction(Log.Action.ReviewIDontLikeThisApplicationBecauseOfOtherReason);
                                    log.setCategory(Log.Category.Button);
                                    log.setScreenType(Log.Screen.ReviewPanel);
                                    sendTrafficMessage(log);

                                }

                                sendReview(survayFlags[0]);
                                submitStat();
                                finish();
                            }
                        });
                    }
                });

                } else {
                submitStat();
                finish();
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    public void moveCalendarToPreviousMonth() {

        try {
            if (calendarCurrentMonth == 1) {

                calendarCurrentMonth = 12;
                setupCalendar(calendarCurrentMonth, --calendarCurrentYear);
            } else {

                setupCalendar(--calendarCurrentMonth, calendarCurrentYear);
            }

            loadDatesToMonthView();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    public void moveCalendarToNextMonth() {

        try {
            if (calendarCurrentMonth == 12) {

                calendarCurrentMonth = 1;
                setupCalendar(calendarCurrentMonth, ++calendarCurrentYear);
            } else {

                setupCalendar(++calendarCurrentMonth, calendarCurrentYear);
            }

            loadDatesToMonthView();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }

    }

    public void initialHelpActivity(View view) {

    }

    public void hamburgerMenuClick(View view) {

        try {
            addUsageCounter(PMenuButtonUsageCounter);
            Intent intent = new Intent(this, MenuActivity.class);
            startActivityForResult(intent, DisplayMenu);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    public void setOnDateMeterTouchEventListener(OnDateMeterFocusListener listener) {
        try {
            dateTouchListener = listener;
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    public void toggleView(View clickedButton) {

        try {
            int newDisplayMode;
            Log log = new Log(this);
            log.setScreenType(Log.Screen.MainScreenName);
            log.setCategory(Log.Category.Button);

            switch (clickedButton.getId()) {

                case R.id.conclusion_view_toggle: {
                    switch (getUsageCounter(PMainDisplayMode)) {
                        case DisplayModeConclusionView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromConclusionToConclusion);
                        }
                        case DisplayModeDateScroller: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromDateScrollerToConclusion);
                            break;
                        }
                        case DisplayModeMonthView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromMonthViewToConclusion);
                            break;
                        }
                        case DisplayModeChartView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromTemperatureViewToConclusion);
                            break;
                        }
                        default: {
                            log.setAction(Log.Action.ClickDisplayToggleFromUnknownToMonthView);
                        }
                    }
                    newDisplayMode = DisplayModeConclusionView;
                    break;
                }
                case R.id.date_view_toggle: {

                    switch (getUsageCounter(PMainDisplayMode)) {
                        case DisplayModeConclusionView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromConclusionToDateScroller);
                        }
                        case DisplayModeDateScroller: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromDateScrollerToDateScroller);
                            break;
                        }
                        case DisplayModeMonthView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromMonthViewToDateScroller);
                            break;
                        }
                        case DisplayModeChartView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromTemperatureViewToDateScroller);
                            break;
                        }
                        default: {
                            log.setAction(Log.Action.ClickDisplayToggleFromUnknownToDateScroller);
                        }
                    }
                    newDisplayMode = DisplayModeDateScroller;
                    isAdjusted = false;
                    break;
                }
                case R.id.month_view_toggle: {
                    switch (getUsageCounter(PMainDisplayMode)) {
                        case DisplayModeConclusionView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromConclusionToMonthView);
                        }
                        case DisplayModeDateScroller: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromDateScrollerToMonthView);
                            break;
                        }
                        case DisplayModeMonthView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromMonthViewToMonthView);
                            break;
                        }
                        case DisplayModeChartView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromTemperatureViewToMonthView);
                            break;
                        }
                        default: {
                            log.setAction(Log.Action.ClickDisplayToggleFromUnknownToMonthView);
                        }
                    }
                    newDisplayMode = DisplayModeMonthView;
                    break;
                }
                case R.id.chart_view_toggle: {
                    switch (getUsageCounter(PMainDisplayMode)) {
                        case DisplayModeConclusionView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromConclusionToTemperature);
                        }
                        case DisplayModeDateScroller: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromDateScrollerToTemperature);
                            break;
                        }
                        case DisplayModeMonthView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromMonthViewToTemperature);
                            break;
                        }
                        case DisplayModeChartView: {
                            log.setAction(Log.Action.ClickDisplayToggleButtonFromTemperatureViewToTemperature);
                            break;
                        }
                        default: {
                            log.setAction(Log.Action.ClickDisplayToggleFromUnknownToTemperature);
                        }
                    }
                    newDisplayMode = DisplayModeChartView;
                    break;
                }
                default:
                    newDisplayMode = DisplayModeDateScroller;
            }

            setSharedPreference(PMainDisplayMode, newDisplayMode);

            adjustLayoutForDisplayModeAccordingToPDisplayMode();

            sendTrafficMessage(log);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void adjustLayoutForDisplayModeAccordingToPDisplayMode() {

        try {
            LinearLayout conclusionView = (LinearLayout) findViewById(R.id.conclusion_panel);
            LinearLayout dateScrollerView = (LinearLayout) findViewById(R.id.date_scroller);
            LinearLayout monthView = (LinearLayout) findViewById(R.id.month_view_panel);
            LinearLayout chartView = (LinearLayout) findViewById(R.id.chart_view_panel);

            switch (getUsageCounter(PMainDisplayMode)) {

                case DisplayModeConclusionView: {

                    // Show only month view hide other
                    conclusionView.setVisibility(View.VISIBLE);
                    monthView.setVisibility(View.GONE);
                    dateScrollerView.setVisibility(View.GONE);
                    chartView.setVisibility(View.GONE);
                    loadConclusionData();
                    break;
                }
                case DisplayModeMonthView: {

                    // Show only month view hide other
                    conclusionView.setVisibility(View.GONE);
                    monthView.setVisibility(View.VISIBLE);
                    dateScrollerView.setVisibility(View.GONE);
                    chartView.setVisibility(View.GONE);
                    break;
                }
                case DisplayModeChartView: {

                    // Show only chart view, hide other
                    conclusionView.setVisibility(View.GONE);
                    chartView.setVisibility(View.VISIBLE);
                    dateScrollerView.setVisibility(View.GONE);
                    monthView.setVisibility(View.GONE);
                    loadChartData();
                    break;
                }
                case DisplayModeDateScroller:
                default: {

                    // The default is show only date scroller view, hide other
                    conclusionView.setVisibility(View.GONE);
                    dateScrollerView.setVisibility(View.VISIBLE);
                    monthView.setVisibility(View.GONE);
                    chartView.setVisibility(View.GONE);
                    setSelectedDateToAlignWithFingerIndex();
                    break;
                }
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void loadConclusionData() {
        try {
            SummaryRepository summary = SummaryRepository.getSummary(this);
            int daysToNextMenstrual = dateDiff(summary.expectedMenstrualDateFrom, DateTime.now()) + 1;
            int daysToNextMenstrualTo = dateDiff(summary.expectedMenstrualDateTo, DateTime.now());
            int daysToNextOvulation = dateDiff(summary.expectedOvulationDate, DateTime.now());
            String daysToNextOvulationText;
            String daysToNextMenstrualText;
            String dateFormat = getResources().getString(R.string.short_date_format);

            conclusionSuggestDate.setText(Html.fromHtml(String.format(getResources().getText(R.string.conclusion_suggestion_date_string).toString(),
                    "<b>" + summary.expectedOvulationDate.minusDays(1).toString(dateFormat) + "</b>",
                    "<b>" + summary.expectedOvulationDate.plusDays(1).toString(dateFormat) + "</b>")));

            if (daysToNextOvulation == 0 || daysToNextOvulation == -1 || daysToNextOvulation == 1) {
                daysToNextOvulationText = getResources().getString(R.string.conclusion_days_to_the_date_option_today_string);

                daysToNextOvulationText = String.format(getResources().getText(R.string.conclusion_days_to_the_date_string).toString(),
                        daysToNextOvulationText);

            } else if (daysToNextOvulation < -1) {
                daysToNextOvulationText = "";
            } else {
                daysToNextOvulationText = String.format(
                        getResources().getString(R.string.conclusion_days_to_the_date_option_next_xxx_days_string),
                        daysToNextOvulation);
                daysToNextOvulationText = String.format(getResources().getText(R.string.conclusion_days_to_the_date_string).toString(),
                        daysToNextOvulationText);
            }

            conclusionSuggestionDaysToDate.setText(daysToNextOvulationText);

            conclusionEstimatedNextMenstrualDate.setText(Html.fromHtml(String.format(
                    getResources().getString(R.string.conclusion_estimated_next_menstrual_date_string),
                    "<b>" + summary.expectedMenstrualDateFrom.toString(dateFormat) + "</b>",
                    "<b>" + summary.expectedMenstrualDateTo.toString(dateFormat) + "</b>")));

            if (daysToNextMenstrual < 0 && daysToNextMenstrualTo < 0) {

                daysToNextMenstrualText = "";
            } else if (daysToNextMenstrual <= 0 && daysToNextMenstrualTo <= 0) {
                daysToNextMenstrualText = getResources().getString(R.string.conclusion_days_to_the_date_option_today_string);
            } else {
                daysToNextMenstrualText = String.format(
                        getResources().getString(R.string.conclusion_days_to_the_date_option_next_xxx_days_string),
                        daysToNextMenstrual);
            }
            conclusionEstimatedNextMenstrualDaysToDate.setText(daysToNextMenstrualText);
            // TODO: Complete this
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private int dateDiff(DateTime dateTo, DateTime dateFrom) {
        return (int) ((dateTo.getMillis() - dateFrom.getMillis()) / 1000 / 60 / 60 / 24);
    }

    private void loadChartData() {

        try {
            LineChart temperatureChart = (LineChart) findViewById(R.id.temperature_chart);
            final int initialDaysOffset = 20;
            ChartHandler _chartHandler = new ChartHandler(this, temperatureChart);

            _chartHandler.initialChart(initialDaysOffset);

            Button fetchPreviousButton = (Button) findViewById(R.id.chart_fetch_previous_data_button);
            Button fetchNextButton = (Button) findViewById(R.id.chart_fetch_next_data_button);

            ChartFetchingOnclickHandler _chartButtonHandler = new ChartFetchingOnclickHandler(_chartHandler);

            fetchNextButton.setOnClickListener(_chartButtonHandler);
            fetchPreviousButton.setOnClickListener(_chartButtonHandler);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void sendReview(int review) {
        String rewJson = String.format("{\"deviceId\":\"%s\",\"review\":\"%s\"}", getDeviceId().toString(), review);
        HttpHelper.post(ReviewUrl, rewJson);
    }

    private void submitStat() {

        try {
            final JSONObject json = new JSONObject();

            try {
                json.put("deviceId", getDeviceId());
                json.put("applicationVersion", getUsageCounter(PCurrentVersion));
                json.put("usageCounter", getUsageCounter(PUsageCounter));
                json.put("periodButtonUsageCounter", getUsageCounter(PPeriodButtonUsageCounter));
                json.put("nonPeriodButtonUsageCounter", getUsageCounter(PNonPeriodButtonUsageCounter));
                json.put("comment_button_usage_counter", getUsageCounter(PCommentButtonUsageCounter));
                json.put("comment_text_usage_counter", getUsageCounter(PCommentTextUsageCounter));
                json.put("menu_button_usage_counter", getUsageCounter(PMenuButtonUsageCounter));
                json.put("review_now", getUsageCounter(PReviewNow));
                json.put("review_later", getUsageCounter(PReviewLater));
                json.put("review_non", getUsageCounter(PNoReview));
                json.put("fetch_next_usage_counter", getUsageCounter(PFetchNextMonthUsageCounter));
                json.put("fetch_previous_usage_counter", getUsageCounter(PFetchPreviousMonthUsageCounter));
                json.put("menu_setting_click_counter", getUsageCounter(PMenuSettingClickCounter));
                json.put("menu_summary_click_counter", getUsageCounter(PMenuSummaryClickCounter));
                json.put("menu_month_view_click_counter", getUsageCounter(PMenuMonthViewClickCounter));
                json.put("menu_help_click_counter", getUsageCounter(PMenuHelpClickCounter));
                json.put("menu_review_click_counter", getUsageCounter(PMenuReviewClickCounter));

                // Available in version 26 or above
                json.put("setting_notify_period_usage_counter", getUsageCounter(PSettingNotifyPeriodCounter));
                json.put("setting_notify_ovulation_usage_counter", getUsageCounter(PSettingNotifyOvulationCounter));
                json.put("setting_notify_period_days", getUsageCounter(PSettingNotifyPeriodDay));
                json.put("setting_notify_ovulation_days", getUsageCounter(PSettingNotifyOvulationDay));
                json.put("setting_notify_notification_click_counter", getUsageCounter(PSettingNotificationClickCounter));

                // Available in version 29 or above
                json.put("setting_language_change_usage_counter", getUsageCounter(PSettingDisplayedLanguageUsageCounter));
                json.put("setting_displayed_language", getStringPreference(PSettingDisplayedLanguage));

                // Available in version 36 or above
                json.put("setting_display_mode", getUsageCounter(PMainDisplayMode));

                // Available in version 38 or above
                json.put("duration", now().getMillis() - startTime.getMillis());

                HttpHelper.post(StatUri, json.toString());
            } catch (JSONException e) {
                HttpHelper.sendErrorLog(e);
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }

    }

    private void initUsageToReview() {

        try {
            SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            int numberOfUsageBeforeAskingForReview = getResources().getInteger(R.integer.number_of_usage_before_ask_for_review);
            edit.putInt(PTimeOfUsageBeforeReview, pref.getInt(PTimeOfUsageBeforeReview, numberOfUsageBeforeAskingForReview));
            edit.apply();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void setDeviceId() {

        try {
            SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(PUuid, pref.getString(PUuid, UUID.randomUUID().toString()));
            edit.apply();
            DeviceId = pref.getString(PUuid, UUID.randomUUID().toString());
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private UUID getDeviceId() {

        try {
            SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
            UUID id = UUID.fromString(pref.getString(PUuid, ""));
            DeviceId = id.toString();
            return id;
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
            return null;
        }
    }

    private void setApplicationVersion() {

        try {
            SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putInt(PCurrentVersion, ApplicationVersion);
            edit.apply();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void addUsageCounter(String key) {

        try {
            SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putInt(key, pref.getInt(key, 0) + 1);
            edit.apply();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private String getStringPreference(String key) {

        try {
            SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
            return pref.getString(key, "");
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
            return "";
        }
    }

    private int getUsageCounter(String key) {

        try {
            SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
            return pref.getInt(key, 0);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
            return 0;
        }
    }

    private void setSharedPreference(String key, int value) {

        try {
            SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putInt(key, value);
            edit.apply();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void setSharedPreference(String key, String value) {

        try {
            SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(key, value);
            edit.apply();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void onDisplayMenuResult(Intent data) {
        try {
            int selectedMenu = data.getIntExtra(MenuActivity.SelectedMenuExtra, 0);
            switch (selectedMenu) {
                case MenuActivity.SelectDisplayHelp: {
                    addUsageCounter(PMenuHelpClickCounter);
                    initialHelpActivity(null);
                    break;
                }
                case MenuActivity.SelectSummary: {
                    addUsageCounter(PMenuSummaryClickCounter);
                    SummaryRepository summary = SummaryRepository.getSummary(this);
                    if (summary != null) {
                        Intent summaryIntent = new Intent(this, SummaryActivity.class);
                        SummaryActivityExtra extra = SummaryActivityExtra.fromSummaryRepository(summary);
                        summaryIntent.putExtra(SummaryActivityExtra.SummaryActivityExtraExtra, extra.toJson());

                        startActivityForResult(summaryIntent, DisplaySummary);
                    }
                    break;
                }
                case MenuActivity.SelectDisplaySetting: {
                    addUsageCounter(PMenuSettingClickCounter);
                    Intent settingIntent = new Intent(this, SettingActivity.class);
                    settingIntent.putExtra(SettingActivity.PeriodLengthExtra, setting.periodLength);
                    settingIntent.putExtra(SettingActivity.PeriodCycleExtra, setting.periodCycle);
                    settingIntent.putExtra(SettingActivity.AverageCycleExtra, setting.averageCycle);
                    settingIntent.putExtra(SettingActivity.AverageLengthExtra, setting.averageLength);
                    settingIntent.putExtra(SettingActivity.CountExtra, setting.count);
                    settingIntent.putExtra(SettingActivity.IsNotifyPeriodCheckExtra, getUsageCounter(PSettingIsNotifyPeriod));
                    settingIntent.putExtra(SettingActivity.IsNotifyOvulationCheckExtra, getUsageCounter(PSettingIsNotifyOvulation));
                    settingIntent.putExtra(SettingActivity.NotifyPeriodDaysExtra, getUsageCounter(PSettingNotifyPeriodDay));
                    settingIntent.putExtra(SettingActivity.NotifyOvulationDaysExtra, getUsageCounter(PSettingNotifyOvulationDay));
                    startActivityForResult(settingIntent, DisplaySetting);
                    break;
                }
                case MenuActivity.SelectReview: {

                    addUsageCounter(PMenuReviewClickCounter);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ougnt.period_manager")));
                    break;
                }
                case MenuActivity.SelectLanguageSelecter: {

                    addUsageCounter(PSettingDisplayedLanguageUsageCounter);
                    Intent intent = new Intent(this, LanguageSelectorActivity.class);
                    startActivityForResult(intent, DisplayLanguageSelector);
                    break;
                }
                case MenuActivity.SelectLockScreen: {
                    addUsageCounter(PMenuLockScreenUsageCounter);
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void onDisplaySettingResult(Intent data) {
        try {
            switch (data.getIntExtra(SettingActivity.ActionExtra, 0)) {

                case SettingActivity.SaveAction: {

                    setting.periodCycle = data.getFloatExtra(SettingActivity.PeriodCycleExtra, 0f);
                    setting.periodLength = data.getFloatExtra(SettingActivity.PeriodLengthExtra, 0f);
                    setting.saveSetting(this);

                    Bundle extras = data.getExtras();
                    boolean isNotifyPeriod = extras.getBoolean(SettingActivity.IsNotifyPeriodCheckExtra);
                    boolean isNotifyOvulation = extras.getBoolean(SettingActivity.IsNotifyOvulationCheckExtra);
                    int notifyPeriodDay = extras.getInt(SettingActivity.NotifyPeriodDaysExtra);
                    int notifyOvulationDay = extras.getInt(SettingActivity.NotifyOvulationDaysExtra);

                    setSharedPreference(PSettingIsNotifyPeriod, isNotifyPeriod ? 1 : 0);
                    setSharedPreference(PSettingIsNotifyOvulation, isNotifyOvulation ? 1 : 0);
                    setSharedPreference(PSettingNotifyPeriodDay, notifyPeriodDay);
                    setSharedPreference(PSettingNotifyOvulationDay, notifyOvulationDay);

                    if (isNotifyOvulation || isNotifyPeriod) {

                        SummaryRepository summary = SummaryRepository.getSummary(this);
                        if (summary == null) {

                            Toast.makeText(this, getResources().getText(R.string.notify_summary_not_set), Toast.LENGTH_LONG).show();

                            break;
                        }

                        setBroadcastNotification(summary);
                    }

                    break;
                }
                case SettingActivity.CancelAction: {


                    break;
                }
            }
        } catch (Resources.NotFoundException e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void onDisplaySettingWizardResult(Intent data) {

        try {
            String json = data.getExtras().getString(SetupWizardActivity.ExtraKey);
            SetupWizardActivityExtra extra = SetupWizardActivityExtra.fromJson(json);

            setting.periodCycle = extra.cycleLength;
            setting.periodLength = extra.periodLength;
            setting.saveSetting(this);

            boolean isNotifyPeriod = extra.notifyBeforeMenstrual != SetupWizardActivityExtra.NotNotifyMe;
            boolean isNotifyOvulation = extra.notifyBeforeOvulation != SetupWizardActivityExtra.NotNotifyMe;
            int notifyPeriodDay = extra.notifyBeforeMenstrual;
            int notifyOvulationDay = extra.notifyBeforeOvulation;

            setSharedPreference(PSettingIsNotifyPeriod, isNotifyPeriod ? 1 : 0);
            setSharedPreference(PSettingIsNotifyOvulation, isNotifyOvulation ? 1 : 0);
            setSharedPreference(PSettingNotifyPeriodDay, notifyPeriodDay);
            setSharedPreference(PSettingNotifyOvulationDay, notifyOvulationDay);

            if (isNotifyOvulation || isNotifyPeriod) {

                SummaryRepository summary = SummaryRepository.getSummary(this);
                if (summary == null) {

                    Toast.makeText(this, getResources().getText(R.string.notify_summary_not_set), Toast.LENGTH_LONG).show();

                    return;
                }

                setBroadcastNotification(summary);
            }
        } catch (Resources.NotFoundException e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void paintDateMeter(DateTime startDate, DateTime endDate, int type) {

        try {
            for (int i = 1; i < dateMeterContainer.getChildCount() - 1; i++) {

                DateMeter targetDateMeter = ((DateMeter) dateMeterContainer.getChildAt(i));
                if (targetDateMeter.getDate().compareTo(endDate) <= 0 && targetDateMeter.getDate().compareTo(startDate) >= 0) {
                    ((DateMeter) dateMeterContainer.getChildAt(i)).changeColor(type);
                }
            }

            for (int i = 0; i <= (endDate.getMillis() - startDate.getMillis()) / 86400000; i++) {

                DateRepository.updateDateRepositorySetDateType(this, startDate.plusDays(i), type);
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private LinearLayout generateEndLayout(LinearLayout callbackLayout) {

        return generateEndLayout(callbackLayout, true);
    }

    private void endLayoutAction(final LinearLayout callbackLayout, boolean isRightEnd) {

        try {
            if (isRightEnd) {

                addUsageCounter(PFetchNextMonthUsageCounter);

                DateMeter lastDateMeter = (DateMeter) callbackLayout.getChildAt(callbackLayout.getChildCount() - 2);
                callbackLayout.removeViewAt(callbackLayout.getChildCount() - 1);
                addDateMeter(callbackLayout, lastDateMeter.getDate().plusDays(1), lastDateMeter.getDate().plusDays(15), true);
                callbackLayout.addView(generateEndLayout(callbackLayout));
            } else {

                addUsageCounter(PFetchPreviousMonthUsageCounter);

                DateMeter lastDateMeter = (DateMeter) callbackLayout.getChildAt(1);
                callbackLayout.removeViewAt(0);
                addDateMeter(callbackLayout, lastDateMeter.getDate().minusDays(15), lastDateMeter.getDate().minusDays(1), false);
                callbackLayout.addView(generateEndLayout(callbackLayout, false), 0);

                Handler h = new Handler();
                h.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        HorizontalScrollView parant = (HorizontalScrollView) callbackLayout.getParent();
                        parant.scrollTo(callbackLayout.getChildAt(1).getWidth() * 15, 0);
                    }
                }, 50);
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private LinearLayout generateEndLayout(final LinearLayout callbackLayout, final boolean isRightEnd) {

        try {
            FetchingButton retLayout = new FetchingButton(this, isRightEnd);

            retLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    endLayoutAction(callbackLayout, isRightEnd);
                }
            });

            return retLayout;
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
            return new FetchingButton(this, isRightEnd);
        }
    }

    private void addDateMeter(LinearLayout targetLayout, DateTime startDate, DateTime endDate, boolean isRight) {

        try {
            List<DateRepository> dates = DateRepository.getDateRepositories(this, startDate, endDate);

            Cursor c = new DatabaseRepositoryHelper(this).getReadableDatabase().rawQuery("SELECT * FROM DATE_REPOSITORY WHERE date = '2015-11-15'", null);
            c.moveToFirst();

            if (isRight) {
                for (int i = 0; i < dates.size(); i++) {
                    targetLayout.addView(new DateMeter(this, dates.get(i), dateTouchListener));
                }
            } else {
                for (int i = dates.size() - 1; i >= 0; i--) {
                    targetLayout.addView(new DateMeter(this, dates.get(i), dateTouchListener), 0);
                }
            }
            c.close();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private boolean selectedDateIsBeforeTheFirstDateMeter(LinearLayout dateMeterHolder) {

        return ((DateMeter) (dateMeterHolder.getChildAt(1))).getDate().isAfter(selectedDate.getDate().getMillis());
    }

    private boolean selectedDateIsAfterTheLastDateMeter(LinearLayout dateMeterHolder) {

        return ((DateMeter) (dateMeterHolder.getChildAt(dateMeterHolder.getChildCount() - 2))).getDate().isBefore(selectedDate.getDate().getMillis());
    }

    private void addPeriodAndOvulationFlagToDateMeters() {

        try {
            addUsageCounter(PPeriodButtonUsageCounter);

            int counter = 50;

            while (selectedDateIsBeforeTheFirstDateMeter(dateMeterContainer) && counter-- > 0) {

                endLayoutAction(dateMeterContainer, false);
            }
            counter = 50;
            while (selectedDateIsAfterTheLastDateMeter(dateMeterContainer) && counter-- > 0) {

                endLayoutAction(dateMeterContainer, true);
            }

            int index = getSelectedDateMeterIndex();

            ((DateMeter) (dateMeterContainer.getChildAt(index))).changeColor(DateMeter.Menstrual);
            DateTime dateToBePainted = ((DateMeter) (dateMeterContainer.getChildAt(index))).getDate();

            DateTime endOfMenstrualPeriod = dateToBePainted.plusDays((int) setting.periodLength - 1);
            DateTime startOfOvulationPeriod = dateToBePainted.plusDays(7);
            DateTime endOfOvulationPeriod = dateToBePainted.plusDays((int) setting.periodCycle - 8);
            DateTime ovulationDate = startOfOvulationPeriod.plusDays(((int) (endOfOvulationPeriod.getMillis() - startOfOvulationPeriod.getMillis()) / 2) / 1000 / 60 / 60 / 24);
            DateTime startNonOvulationPeriod = dateToBePainted.plusDays((int) setting.periodCycle - 7);
            DateTime endNonOvulationPeriod = dateToBePainted.plusDays((int) setting.periodCycle);
            DateTime estimatedNextMenstrualFrom = dateToBePainted.plusDays((int) setting.periodCycle - 1);
            DateTime estimatedNextMenstrualTo = dateToBePainted.plusDays((int) setting.periodCycle + 2);

            paintDateMeter(dateToBePainted, endOfMenstrualPeriod, DateMeter.Menstrual);
            paintDateMeter(startOfOvulationPeriod, endOfOvulationPeriod, DateMeter.PossiblyOvulation);
            paintDateMeter(ovulationDate, ovulationDate, DateMeter.OvulationDate);
            paintDateMeter(startNonOvulationPeriod, endNonOvulationPeriod, DateMeter.Nothing);
            paintDateMeter(estimatedNextMenstrualFrom, estimatedNextMenstrualTo, DateMeter.ExpectedMenstrual);

            DateTime nextMenstrualFrom = dateToBePainted.plusDays((int) setting.periodCycle - 1);
            DateTime nextMenstrualTo = dateToBePainted.plusDays((int) setting.periodCycle + 1);
            DateTime nextOvulationFrom = dateToBePainted.plusDays(6);
            DateTime nextOvulationTo = dateToBePainted.plusDays((int) setting.periodCycle - 8);

            SummaryRepository summary = new SummaryRepository();
            summary.expectedMenstrualDateTo = nextMenstrualTo;
            summary.expectedMenstrualDateFrom = nextMenstrualFrom;
            summary.expectedOvulationDateTo = nextOvulationTo;
            summary.expectedOvulationDateFrom = nextOvulationFrom;
            summary.expectedOvulationDate = nextOvulationFrom.plusDays((int) (nextOvulationTo.getMillis() - nextOvulationFrom.getMillis()) / 2 / 1000 / 60 / 60 / 24);

            setBroadcastNotification(summary);

            Intent summaryIntent = new Intent(this, SummaryActivity.class);
            SummaryActivityExtra extra = SummaryActivityExtra.fromSummaryRepository(summary);
            summaryIntent.putExtra(SummaryActivityExtra.SummaryActivityExtraExtra, extra.toJson());
            startActivityForResult(summaryIntent, DisplaySummary);
        } catch (Resources.NotFoundException e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void setBroadcastNotification(SummaryRepository summary) {

        if (getUsageCounter(PSettingIsNotifyPeriod) == 1) {

            DateTime dateToBeNotified = summary.expectedMenstrualDateFrom
                    .minusDays(getUsageCounter(PSettingNotifyPeriodDay));

            BroadcastNotificationPublisher notifier = new BroadcastNotificationPublisher();
            notifier.setNotification(this, dateToBeNotified,
                    getResources().getString(R.string.notify_period_title),
                    getResources().getString(R.string.notify_period_message));
        }

        if (getUsageCounter(PSettingIsNotifyOvulation) == 1) {

            DateTime dateTimeToBeNotified = summary.expectedOvulationDate
                    .minusDays(getUsageCounter(PSettingNotifyOvulationDay));

            BroadcastNotificationPublisher notifier = new BroadcastNotificationPublisher();
            notifier.setNotification(this, dateTimeToBeNotified,
                    getResources().getString(R.string.notify_ovulation_title),
                    getResources().getString(R.string.notify_ovulation_message));
        }
    }

    private void removePeriodAndOvulationFlagToDateMeter() {

        try {
            int counter = 50;

            while (selectedDateIsBeforeTheFirstDateMeter(dateMeterContainer) && counter-- > 0) {

                endLayoutAction(dateMeterContainer, false);
            }
            counter = 50;
            while (selectedDateIsAfterTheLastDateMeter(dateMeterContainer) && counter-- > 0) {

                endLayoutAction(dateMeterContainer, true);
            }

            int index = getSelectedDateMeterIndex();

            addUsageCounter(PNonPeriodButtonUsageCounter);
            DateMeter dateMeterToBeChange = ((DateMeter) (dateMeterContainer.getChildAt(index)));
            DateTime dateToBeChange = dateMeterToBeChange.getDate();

            paintDateMeter(dateToBeChange, dateToBeChange, DateMeter.Nothing);
            dateMeterToBeChange.changeColor(DateMeter.Nothing);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private int getSelectedDateMeterIndex() {

        int index = 0;

        try {
            DateTime firstDate = ((DateMeter) dateMeterContainer.getChildAt(1)).getDate();
            int dateDiff = (int) ((selectedDate.getDate().getMillis() - firstDate.getMillis()) / 1000 / 60 / 60 / 24);

            index = dateDiff + 1;
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
            return index;
        }
        return index;
    }

    static boolean isAdjusted = false;

    private void moveDateMeterToCurrentDate() {

        try {
            ViewTreeObserver obs = dateMeterContainer.getViewTreeObserver();

            obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (isAdjusted) {
                        return;
                    } else if (dateMeterContainer.getWidth() == 0) {
                        return;
                    }

                    isAdjusted = true;

                    int[] firstChildLocal = new int[2];
                    int[] scrollViewLocal = new int[2];

                    dateMeterScroller.getChildAt(0).getLocationOnScreen(firstChildLocal);
                    dateMeterScroller.getLocationOnScreen(scrollViewLocal);

                    if (firstChildLocal[0] == scrollViewLocal[0]) {
                        int[] todayLocal = new int[2];
                        int[] fingerIndexLocal = new int[2];
                        dateMeterContainer.getChildAt(16).getLocationOnScreen(todayLocal);
                        fingerIndex.getLocationOnScreen(fingerIndexLocal);
                        int centerOfFinger = fingerIndexLocal[0] + fingerIndex.getWidth() / 2;
                        dateMeterScroller.scrollTo(todayLocal[0] -
                                dateMeterScroller.getWidth() / 2, 0);
                    }

                    if (setting.isFirstTime) {
                        setting.isFirstTime = false;

                        Intent wizardIntent = new Intent(getBaseContext(), SetupWizardActivity.class);
                        startActivityForResult(wizardIntent, DisplaySettingWizard);

                        setting.saveSetting(getBaseContext());
                    }

                    int targetLength = (int) (dateMeterScroller.getHeight() * 0.5);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(targetLength, targetLength);
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                    fingerIndex.setLayoutParams(params);

                    selectedDate = (DateMeter) dateMeterContainer.getChildAt(17);
                    setDateDetailText(selectedDate);
                    dateMeterScroller.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSelectedDateToAlignWithFingerIndex();
                        }
                    }, 50);
                }
            });
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    synchronized private void moveDateMeter(int targetPixel) {
        moveDateMeter(targetPixel, 0);
    }

    synchronized private void moveDateMeter(final int targetPixel, final int movedPixel) {

        if (Math.abs(movedPixel) < Math.abs(targetPixel)) {
            dateMeterScroller.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dateMeterScroller.scrollBy(targetPixel / 10, 0);
                    moveDateMeter(targetPixel, movedPixel + targetPixel / 10);
                    setSelectedDateToAlignWithFingerIndex();
                }
            }, 10);
        }
    }

    synchronized public static void sendTrafficMessage(Log log) {

        try {
            if (log == null) {
                return;
            }

            if (DeviceId != null) {
                if (DeviceId.equals("2e0dc207-3f43-421a-a1ae-c47dcdd15490") ||
                        DeviceId.equals("481e9faa-1b89-4ec4-9439-4220bee1c6c2")) {
                    return;
                }
            }

            tracker.setScreenName(log.getScreenType());
            tracker.setClientId(log.getDeviceId());
            tracker.setAppVersion(log.getApplicationVersion());
            tracker.setLanguage(log.getLanguage());
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(log.getCategory())
                    .setAction(log.getAction())
                    .build());
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    synchronized public static void sendLoadTimeMessage(Log log, long loadTime) {

        try {
            if (log == null) {
                return;
            }

            if (DeviceId != null) {
                if (DeviceId.equals("2e0dc207-3f43-421a-a1ae-c47dcdd15490") ||
                        DeviceId.equals("481e9faa-1b89-4ec4-9439-4220bee1c6c2")) {
                    return;
                }
            }

            tracker.setScreenName(log.getScreenType());
            tracker.setClientId(log.getDeviceId());
            tracker.setAppVersion(log.getApplicationVersion());
            tracker.setLanguage(log.getLanguage());
            tracker.send(new HitBuilders.TimingBuilder()
                    .setCategory(log.getCategory())
                    .setVariable(log.getAction())
                    .setValue(loadTime)
                    .setLabel("Load Time")
                    .build());
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void processDateDetailFlinging(float distant) {
        int newDateMeterIndex = distant > 0 ? getSelectedDateMeterIndex() + 1 : getSelectedDateMeterIndex() - 1;
        if (newDateMeterIndex == 0 || newDateMeterIndex == dateMeterContainer.getChildCount() - 1)
            return;
        DateMeter currentSelectedDate = selectedDate;
        DateMeter newSelectedDate = (DateMeter) dateMeterContainer.getChildAt(newDateMeterIndex);
        selectedDate = newSelectedDate;

        int direction = distant > 0 ? -1 : 1;

        moveDateMeter(direction * (currentSelectedDate.getWidth() / 2 + newSelectedDate.getWidth() / 2 - 30));
    }

    private void getAllViews() {
        try {
            dateMeterContainer = (LinearLayout) findViewById(R.id.dateScrollerContent);
            newActionPanel = (LinearLayout) findViewById(R.id.new_action_panel);
            adMobLayout = (LinearLayout) findViewById(R.id.ads_mob_view);
            dateMeterScroller = (HorizontalScrollView) findViewById(R.id.dateScroller);
            fingerIndex = (ImageView) findViewById(R.id.finger_pointer);
            dateDetailActionButton = (Button) findViewById(R.id.date_detail_action_button);
            helpButton = (ImageButton) findViewById(R.id.main_help_button);
            dateDetailMainLayout = (LinearLayout) findViewById(R.id.date_detail_main_layout);
            conclusionSuggestDate = (TextView) findViewById(R.id.conclusion_suggestion_date);
            conclusionSuggestionDaysToDate = (TextView) findViewById(R.id.conclusion_suggestion_days_to_the_date);
            conclusionEstimatedNextMenstrualDate = (TextView) findViewById(R.id.conclusion_estimated_next_menstrual_date_string);
            conclusionEstimatedNextMenstrualDaysToDate = (TextView) findViewById(R.id.conclusion_estimated_next_menstrual_days_string);

            registerOnclickAndOnSwipeEvent();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void registerOnclickAndOnSwipeEvent() {
        try {
            helpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    log.setAction(Log.Action.ClickMainHelp);
                    log.setCategory(Log.Category.Button);
                    log.setScreenType(Log.Screen.MainScreenName);
                    sendTrafficMessage(log);

                    Intent intent = new Intent(getBaseContext(), MainHelpActivity.class);
                    startActivity(intent);
                }
            });
            dateDetailMainLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private Button dateDetailActionButton;
    private ImageView fingerIndex;
    private LinearLayout dateMeterContainer;
    private HorizontalScrollView dateMeterScroller;
    private LinearLayout newActionPanel;
    private AdView adView;
    private LinearLayout adMobLayout;
    private ImageButton helpButton;
    private LinearLayout dateDetailMainLayout;

    private TextView conclusionSuggestDate;
    private TextView conclusionSuggestionDaysToDate;
    private TextView conclusionEstimatedNextMenstrualDate;
    private TextView conclusionEstimatedNextMenstrualDaysToDate;

    private GestureDetector gestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > 200) {
                processDateDetailFlinging(velocityX);
            }
            return true;
        }
    });

    private int calendarCurrentMonth, calendarCurrentYear;
    private OnDateMeterFocusListener dateTouchListener;
    private PeriodCalendar calendar;
    private DateMeter selectedDate = null;
    public static AppForStatic analyticsApplication;
    public static String DeviceId;
    public static Tracker tracker;
    public Log log;
}