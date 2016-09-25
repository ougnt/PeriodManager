package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.PeriodCalendar;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.extra.ActionActivityExtra;
import com.ougnt.period_manager.activity.extra.SetupWizardActivityExtra;
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

import static com.ougnt.period_manager.activity.SummaryActivity.NextMenstrualFromExtra;
import static com.ougnt.period_manager.activity.SummaryActivity.NextMenstrualToExtra;
import static com.ougnt.period_manager.activity.SummaryActivity.NextOvulationExtra;
import static com.ougnt.period_manager.activity.SummaryActivity.NextOvulationFromExtra;
import static com.ougnt.period_manager.activity.SummaryActivity.NextOvulationToExtra;
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

    public static final int ApplicationVersion = 56 ;

    // TODO : Change this to the real one
    // Live Env
    public static final String StatServer = "27.254.81.190:5555";
    // Dev env
//    public static final String StatServer = "192.168.1.101:9000";
    public static final String StatUri = String.format("http://%s/usageStat", StatServer);
    public static final String AdsRequestUri = String.format("http://%s/adsAsk", StatServer);
    public static final String AdsClickUri = String.format("http://%s/adsClick", StatServer);

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
    public static DateTime startTime = now();

    SettingRepository setting;

    public InitialActivity() {
        dateTouchListener = null;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAdjusted = false;
        startTime = now();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isAdjusted = false;
    }

    private void initialApplication() {

        setContentView(R.layout.main);

        AdRequest.Builder adBuilder = new AdRequest.Builder();
        adBuilder.setGender(AdRequest.GENDER_FEMALE);
        AdRequest adRequest = adBuilder.build();
        final AdView adView = (AdView) findViewById(R.id.ad_view);
        adView.loadAd(adRequest);
        final LinearLayout adMobLayout = (LinearLayout) findViewById(R.id.ads_mob_view);
        adMobLayout.setVisibility(View.GONE);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adMobLayout.setLayoutParams(new LinearLayout.LayoutParams(adView.getAdSize().getWidth(), adView.getAdSize().getHeight()));
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

        addDateMeterView();
        addMonthView();

        adjustLayoutForDisplayModeAccordingToPDisplayMode();

        LinearLayout newActionPanel = (LinearLayout) findViewById(R.id.new_action_panel);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_detail, newActionPanel);

        findViewById(R.id.date_detail_action_button).setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(getBaseContext(), NewActionActivity.class);
                intent.putExtra(NewActionActivity.ExtraKey, extra.toJson());
                startActivityForResult(intent, DisplayNewActionPanel);
            }
        });

        View view = findViewById(R.id.dateScroller);
        view.setOnTouchListener(new View.OnTouchListener() {

            private ViewTreeObserver observer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.dateScroller);
                observer = observer == null ? scrollView.getViewTreeObserver() : observer;

                observer.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                    @Override
                    public void onScrollChanged() {

                        int[] fingerIndexLocator = new int[2];
                        int[] dateMeterLocator = new int[2];

                        ImageView fingerIndex = (ImageView) findViewById(R.id.finger_pointer);
                        LinearLayout scrollContent = (LinearLayout) findViewById(R.id.dateScrollerContent);
                        fingerIndex.getLocationOnScreen(fingerIndexLocator);

                        for (int i = 1; i < scrollContent.getChildCount() - 2; i++) {
                            DateMeter targetDateMeter = (DateMeter) scrollContent.getChildAt(i);
                            targetDateMeter.getLocationOnScreen(dateMeterLocator);
                            int fingerIndexPointerX = fingerIndexLocator[0] + fingerIndex.getWidth() / 3;

                            if (fingerIndexPointerX > dateMeterLocator[0] && fingerIndexPointerX < dateMeterLocator[0] + targetDateMeter.getWidth()) {

                                if (selectedDate == null) selectedDate = targetDateMeter;
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
                        headerText.setText(selectedDate.getDate().toString(getResources().getText(R.string.short_date_format).toString()));
                    }
                });
                return false;
            }
        });
    }

    private void addDateMeterView() {

        final LinearLayout dateMeterLayout = (LinearLayout) findViewById(R.id.dateScrollerContent);

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

        addDateMeter(dateMeterLayout, now().minusDays(15), now().plusDays(15), true);

        dateMeterLayout.addView(generateEndLayout(dateMeterLayout, true));
        dateMeterLayout.addView(generateEndLayout(dateMeterLayout, false), 0);

        setting = SettingRepository.getSettingRepository(this);

        moveDateMeterToCurrentDate();
    }

    private void setDateDetailText(DateMeter touchDate) {

        TextView todayText = (TextView) findViewById(R.id.date_detail_text);
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
    }

    private void addMonthView() {

        DateTime calendarDate = selectedDate == null ? now() : selectedDate.getDate();

        setupCalendar(calendarDate.getMonthOfYear(), calendarDate.getYear());
        loadDatesToView();

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
    }

    private void setupCalendar(int month, int year) {

        calendarCurrentMonth = month;
        calendarCurrentYear = year;
        calendar = new PeriodCalendar(this,
                calendarCurrentMonth,
                calendarCurrentYear);

        TextView monthText = (TextView) findViewById(R.id.calendar_view_month_text);
        monthText.setText(DateTime.parse(String.format("%s-%s-01", year, month)).toString("MMMM"));
    }

    private void loadDatesToView() {

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
//                        selectedDate = tempDate;
                        DateRepository date = DateRepository.getDateRepositories(getBaseContext(), tempDate, tempDate).get(0);

                        selectedDate = new DateMeter(getBaseContext(), date, dateTouchListener);

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
    }

    private void loadDateToView(DateRepository date, TextView view) {

        int color;

        if (date.date.getMonthOfYear() != calendarCurrentMonth) {

            color = ContextCompat.getColor(this, R.color.calendar_other_month_text);
        } else {

            switch (date.dateType) {

                case DateMeter.Menstrual:
                    color = ContextCompat.getColor(this, R.color.calendar_period_text);
                    break;
                case DateMeter.PossiblyOvulation:
                    color = ContextCompat.getColor(this, R.color.calendar_ovulation_text);
                    break;
                default:
                    color = ContextCompat.getColor(this, R.color.calendar_text_color);
            }
        }

        view.setTextColor(color);
        String displayText = date.date.getDayOfMonth() + "";
        view.setText(displayText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

                SummaryRepository summary = new SummaryRepository();

                summary.expectedMenstrualDateFrom = DateTime.parse(data.getExtras().getString(NextMenstrualFromExtra));
                summary.expectedMenstrualDateTo = DateTime.parse(data.getExtras().getString(NextMenstrualToExtra));
                summary.expectedOvulationDateFrom = DateTime.parse(data.getExtras().getString(NextOvulationFromExtra));
                summary.expectedOvulationDateTo = DateTime.parse(data.getExtras().getString(NextOvulationToExtra));
                summary.expectedOvulationDate = DateTime.parse(data.getExtras().getString(NextOvulationExtra));

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

                if (data == null) return;
                ActionActivityExtra extra = ActionActivityExtra.fromJsonString(data.getExtras().getString(NewActionActivity.ExtraKey));

                if (extra.isButtonPush) {
                    if (extra.dateType == DateMeter.Menstrual) {
                        removePeriodAndOvulationFlagToDateMeter();
                    } else {
                        addPeriodAndOvulationFlagToDateMeters();
                    }
                }
                selectedDate.comment = extra.comment;
                selectedDate.temperature = (float) extra.temperature;
                DateRepository.updateDateRepositorySetComment(this, selectedDate.getDate(), selectedDate.comment);
                DateRepository.updateDateRepositorySetTemperature(this, selectedDate.getDate(), selectedDate.temperature);
                addMonthView();
            }
        }
    }

    @Override
    public void onBackPressed() {

        final SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        final SharedPreferences.Editor edit = pref.edit();

        if (getUsageCounter(PUsageCounter) == getUsageCounter(PTimeOfUsageBeforeReview)) {

            setContentView(R.layout.review);

            Button reviewNowButton = (Button) findViewById(R.id.review_open);
            Button laterButton = (Button) findViewById(R.id.review_later);
            Button noShowButton = (Button) findViewById(R.id.review_no_show);

            reviewNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addUsageCounter(PReviewNow);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ougnt.period_manager")));
                    submitStat();
                    finish();
                }
            });

            laterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUsageCounter(PReviewLater);
                    edit.putInt(PTimeOfUsageBeforeReview, pref.getInt(PTimeOfUsageBeforeReview, 0) + 10);
                    edit.apply();
                    submitStat();
                    finish();
                }
            });

            noShowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addUsageCounter(PNoReview);
                    edit.putInt(PTimeOfUsageBeforeReview, -1);
                    edit.apply();
                    submitStat();
                    finish();
                }
            });

        } else {
            submitStat();
            finish();
        }
    }

    public void moveCalendarToPreviousMonth() {

        if (calendarCurrentMonth == 1) {

            calendarCurrentMonth = 12;
            setupCalendar(calendarCurrentMonth, --calendarCurrentYear);
        } else {

            setupCalendar(--calendarCurrentMonth, calendarCurrentYear);
        }

        loadDatesToView();
    }

    public void moveCalendarToNextMonth() {

        if (calendarCurrentMonth == 12) {

            calendarCurrentMonth = 1;
            setupCalendar(calendarCurrentMonth, ++calendarCurrentYear);
        } else {

            setupCalendar(++calendarCurrentMonth, calendarCurrentYear);
        }

        loadDatesToView();

    }

    public void initialHelpActivity(View view) {

    }

    public void hamburgerMenuClick(View view) {

        addUsageCounter(PMenuButtonUsageCounter);
        Intent intent = new Intent(this, MenuActivity.class);
        startActivityForResult(intent, DisplayMenu);
    }

    public void setOnDateMeterTouchEventListener(OnDateMeterFocusListener listener) {
        dateTouchListener = listener;
    }

    public void toggleView(View clickedButton) {

        int newDisplayMode;
        Log log = new Log(this);
        log.setScreenType(Log.Screen.MainScreenName);
        log.setCategory(Log.Category.Button);

        switch (clickedButton.getId()) {

            case R.id.date_view_toggle: {

                switch (getUsageCounter(PMainDisplayMode)) {
                    case DisplayModeDateScroller:{
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
                    case DisplayModeDateScroller:{
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
                    case DisplayModeDateScroller:{
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
    }

    private void adjustLayoutForDisplayModeAccordingToPDisplayMode() {

        LinearLayout dateScrollerView = (LinearLayout) findViewById(R.id.date_scroller);
        LinearLayout monthView = (LinearLayout) findViewById(R.id.month_view_panel);
        LinearLayout chartView = (LinearLayout) findViewById(R.id.chart_view_panel);

        switch (getUsageCounter(PMainDisplayMode)) {

            case DisplayModeMonthView: {

                // Show only month view hide other
                monthView.setVisibility(View.VISIBLE);
                dateScrollerView.setVisibility(View.GONE);
                chartView.setVisibility(View.GONE);
                break;
            }
            case DisplayModeChartView: {

                // Show only chart view, hide other
                chartView.setVisibility(View.VISIBLE);
                dateScrollerView.setVisibility(View.GONE);
                monthView.setVisibility(View.GONE);
                loadChartData();
                break;
            }
            case DisplayModeDateScroller:
            default: {

                // The default is show only date scroller view, hide other
                dateScrollerView.setVisibility(View.VISIBLE);
                monthView.setVisibility(View.GONE);
                chartView.setVisibility(View.GONE);
            }
        }
    }

    private void loadChartData() {

        LineChart temperatureChart = (LineChart) findViewById(R.id.temperature_chart);
        final int initialDaysOffset = 20;
        ChartHandler _chartHandler = new ChartHandler(this, temperatureChart);

        _chartHandler.initialChart(initialDaysOffset);

        Button fetchPreviousButton = (Button) findViewById(R.id.chart_fetch_previous_data_button);
        Button fetchNextButton = (Button) findViewById(R.id.chart_fetch_next_data_button);

        ChartFetchingOnclickHandler _chartButtonHandler = new ChartFetchingOnclickHandler(_chartHandler);

        fetchNextButton.setOnClickListener(_chartButtonHandler);
        fetchPreviousButton.setOnClickListener(_chartButtonHandler);
    }

    private void submitStat() {

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
            e.printStackTrace();
        }

    }

    private void initUsageToReview() {

        SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(PTimeOfUsageBeforeReview, pref.getInt(PTimeOfUsageBeforeReview, 10));
        edit.apply();
    }

    private void setDeviceId() {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(PUuid, pref.getString(PUuid, UUID.randomUUID().toString()));
        edit.apply();
        DeviceId = pref.getString(PUuid, UUID.randomUUID().toString());
    }

    private UUID getDeviceId() {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        UUID id = UUID.fromString(pref.getString(PUuid, ""));
        DeviceId = id.toString();
        return id;
    }

    private void setApplicationVersion() {

        SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(PCurrentVersion, ApplicationVersion);
        edit.apply();
    }

    private void addUsageCounter(String key) {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key, pref.getInt(key, 0) + 1);
        edit.apply();
    }

    private String getStringPreference(String key) {

        SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        return pref.getString(key, "");
    }

    private int getUsageCounter(String key) {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    private void setSharedPreference(String key, int value) {

        SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    private void setSharedPreference(String key, String value) {

        SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.apply();
    }

    private void onDisplayMenuResult(Intent data) {
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
                    summaryIntent.putExtra(NextMenstrualFromExtra, summary.expectedMenstrualDateFrom.toString("yyyy-MM-dd"));
                    summaryIntent.putExtra(NextMenstrualToExtra, summary.expectedMenstrualDateTo.toString("yyyy-MM-dd"));
                    summaryIntent.putExtra(NextOvulationFromExtra, summary.expectedOvulationDateFrom.toString("yyyy-MM-dd"));
                    summaryIntent.putExtra(NextOvulationToExtra, summary.expectedOvulationDateTo.toString("yyyy-MM-dd"));

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
    }

    private void onDisplaySettingResult(Intent data) {
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

                    BroadcastNotificationPublisher notifier = new BroadcastNotificationPublisher();

                    if (isNotifyPeriod) {

                        notifier.setNotification(
                                this,
                                summary.expectedMenstrualDateFrom.minusDays(notifyPeriodDay),
                                getResources().getString(R.string.notify_period_title),
                                getResources().getString(R.string.notify_period_message)
                        );
                        addUsageCounter(PSettingNotifyPeriodCounter);
                    }

                    if (isNotifyOvulation) {

                        notifier.setNotification(
                                this,
                                summary.expectedOvulationDateFrom.minusDays(notifyOvulationDay),
                                getResources().getString(R.string.notify_ovulation_title),
                                getResources().getString(R.string.notify_ovulation_message)
                        );
                        addUsageCounter(PSettingNotifyOvulationCounter);
                    }
                }

                break;
            }
            case SettingActivity.CancelAction: {


                break;
            }
        }
    }

    private void onDisplaySettingWizardResult(Intent data) {

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

            BroadcastNotificationPublisher notifier = new BroadcastNotificationPublisher();

            if (isNotifyPeriod) {

                notifier.setNotification(
                        this,
                        summary.expectedMenstrualDateFrom.minusDays(notifyPeriodDay),
                        getResources().getString(R.string.notify_period_title),
                        getResources().getString(R.string.notify_period_message)
                );
                addUsageCounter(PSettingNotifyPeriodCounter);
            }

            if (isNotifyOvulation) {

                notifier.setNotification(
                        this,
                        summary.expectedOvulationDateFrom.minusDays(notifyOvulationDay),
                        getResources().getString(R.string.notify_ovulation_title),
                        getResources().getString(R.string.notify_ovulation_message)
                );
                addUsageCounter(PSettingNotifyOvulationCounter);
            }
        }
    }

    private void paintDateMeter(DateTime startDate, DateTime endDate, int type) {

        LinearLayout dateMeterLayout = (LinearLayout) findViewById(R.id.dateScrollerContent);

        for (int i = 1; i < dateMeterLayout.getChildCount() - 1; i++) {

            DateMeter targetDateMeter = ((DateMeter) dateMeterLayout.getChildAt(i));
            if (targetDateMeter.getDate().compareTo(endDate) <= 0 && targetDateMeter.getDate().compareTo(startDate) >= 0) {
                ((DateMeter) dateMeterLayout.getChildAt(i)).changeColor(type);
            }
        }

        for (int i = 0; i <= (endDate.getMillis() - startDate.getMillis()) / 86400000; i++) {

            DateRepository.updateDateRepositorySetDateType(this, startDate.plusDays(i), type);
        }
    }

    private LinearLayout generateEndLayout(LinearLayout callbackLayout) {

        return generateEndLayout(callbackLayout, true);
    }

    private void endLayoutAction(final LinearLayout callbackLayout, boolean isRightEnd) {

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
    }

    private LinearLayout generateEndLayout(final LinearLayout callbackLayout, final boolean isRightEnd) {

        FetchingButton retLayout = new FetchingButton(this, isRightEnd);

        retLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endLayoutAction(callbackLayout, isRightEnd);
            }
        });

        return retLayout;
    }

    private void addDateMeter(LinearLayout targetLayout, DateTime startDate, DateTime endDate, boolean isRight) {

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
    }

    private boolean selectedDateIsBeforeTheFirstDateMeter(LinearLayout dateMeterHolder) {

        return ((DateMeter) (dateMeterHolder.getChildAt(1))).getDate().isAfter(selectedDate.getDate().getMillis());
    }

    private boolean selectedDateIsAfterTheLastDateMeter(LinearLayout dateMeterHolder) {

        return ((DateMeter) (dateMeterHolder.getChildAt(dateMeterHolder.getChildCount() - 2))).getDate().isBefore(selectedDate.getDate().getMillis());
    }

    private void addPeriodAndOvulationFlagToDateMeters() {

        addUsageCounter(PPeriodButtonUsageCounter);

        LinearLayout v = (LinearLayout) findViewById(R.id.dateScrollerContent);
        int counter = 50;

        while (selectedDateIsBeforeTheFirstDateMeter(v) && counter-- > 0) {

            endLayoutAction(v, false);
        }
        counter = 50;
        while (selectedDateIsAfterTheLastDateMeter(v) && counter-- > 0) {

            endLayoutAction(v, true);
        }

        int index = getSelectedDateMeterIndex();

        ((DateMeter) (v.getChildAt(index))).changeColor(DateMeter.Menstrual);
        DateTime dateToBePainted = ((DateMeter) (v.getChildAt(index))).getDate();

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
        paintDateMeter(estimatedNextMenstrualFrom, estimatedNextMenstrualTo, DateMeter.Menstrual);

        Intent summaryIntent = new Intent(this, SummaryActivity.class);
        summaryIntent.putExtra(NextMenstrualFromExtra, dateToBePainted.plusDays((int) setting.periodCycle - 1).toString("yyyy-MM-dd"));
        summaryIntent.putExtra(NextMenstrualToExtra, dateToBePainted.plusDays((int) setting.periodCycle + 1).toString("yyyy-MM-dd"));
        summaryIntent.putExtra(NextOvulationFromExtra, dateToBePainted.plusDays(6).toString("yyyy-MM-dd"));
        summaryIntent.putExtra(NextOvulationToExtra, dateToBePainted.plusDays((int) setting.periodCycle - 8).toString("yyyy-MM-dd"));

        if (getUsageCounter(PSettingIsNotifyPeriod) == 1) {

            DateTime dateToBeNotified = DateTime.parse(summaryIntent.getExtras()
                    .getString(NextMenstrualFromExtra))
                    .minusDays(getUsageCounter(PSettingNotifyPeriodDay));

            BroadcastNotificationPublisher notifier = new BroadcastNotificationPublisher();
            notifier.setNotification(this, dateToBeNotified,
                    getResources().getString(R.string.notify_period_title),
                    getResources().getString(R.string.notify_period_message));
        }

        if (getUsageCounter(PSettingIsNotifyOvulation) == 1) {

            DateTime dateTimeToBeNotified = DateTime.parse(summaryIntent.getExtras()
                    .getString(NextOvulationFromExtra))
                    .minusDays(getUsageCounter(PSettingNotifyOvulationDay));
            BroadcastNotificationPublisher notifier = new BroadcastNotificationPublisher();
            notifier.setNotification(this, dateTimeToBeNotified,
                    getResources().getString(R.string.notify_ovulation_title),
                    getResources().getString(R.string.notify_ovulation_message));
        }

        startActivityForResult(summaryIntent, DisplaySummary);
    }

    private void removePeriodAndOvulationFlagToDateMeter() {

        LinearLayout v = (LinearLayout) findViewById(R.id.dateScrollerContent);
        int counter = 50;

        while (selectedDateIsBeforeTheFirstDateMeter(v) && counter-- > 0) {

            endLayoutAction(v, false);
        }
        counter = 50;
        while (selectedDateIsAfterTheLastDateMeter(v) && counter-- > 0) {

            endLayoutAction(v, true);
        }

        int index = getSelectedDateMeterIndex();

        addUsageCounter(PNonPeriodButtonUsageCounter);
        DateMeter dateMeterToBeChange = ((DateMeter) (v.getChildAt(index)));
        DateTime dateToBeChange = dateMeterToBeChange.getDate();

        paintDateMeter(dateToBeChange, dateToBeChange, DateMeter.Nothing);
        dateMeterToBeChange.changeColor(DateMeter.Nothing);
    }

    private int getSelectedDateMeterIndex() {

        int index;

        LinearLayout v = (LinearLayout) findViewById(R.id.dateScrollerContent);

        DateTime firstDate = ((DateMeter) v.getChildAt(1)).getDate();
        int dateDiff = (int) ((selectedDate.getDate().getMillis() - firstDate.getMillis()) / 1000 / 60 / 60 / 24);

        index = dateDiff + 1;
        return index;
    }

    static boolean isAdjusted = false;

    private void moveDateMeterToCurrentDate() {

        ViewTreeObserver obs = findViewById(R.id.dateScrollerContent).getViewTreeObserver();

        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (isAdjusted) {
                    return;
                } else if (findViewById(R.id.dateScrollerContent).getWidth() == 0) {
                    return;
                }

                isAdjusted = true;

                final LinearLayout dateMeterLayout = (LinearLayout) findViewById(R.id.dateScrollerContent);
                final HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.dateScroller);

                int[] firstChildLocal = new int[2];
                int[] scrollViewLocal = new int[2];

                scrollView.getChildAt(0).getLocationOnScreen(firstChildLocal);
                scrollView.getLocationOnScreen(scrollViewLocal);

                if (firstChildLocal[0] == scrollViewLocal[0]) {
                    scrollView.scrollTo(dateMeterLayout.getChildAt(1).getWidth() * 15, 0);
                }

                if (setting.isFirstTime) {
                    setting.isFirstTime = false;

                    Intent wizardIntent = new Intent(getBaseContext(), SetupWizardActivity.class);
                    startActivityForResult(wizardIntent, DisplaySettingWizard);

                    setting.saveSetting(getBaseContext());
                }

                int targetLength = (int) (scrollView.getHeight() * 0.5);
                ImageView finger = (ImageView) findViewById(R.id.finger_pointer);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(targetLength, targetLength);
                params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                finger.setLayoutParams(params);

                DateMeter todayDateMeter = (DateMeter) ((LinearLayout) findViewById(R.id.dateScrollerContent)).getChildAt(17);
                setDateDetailText(todayDateMeter);
                selectedDate = todayDateMeter;
            }
        });
    }

    synchronized public static void sendTrafficMessage(Log log) {

        if(log == null) {
            return;
        }

        tracker.setScreenName(log.getScreenType());
        tracker.setClientId(log.getDeviceId());
        tracker.setAppVersion(log.getApplicationVersion());
        tracker.setLanguage(log.getLanguage());
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(log.getCategory())
                .setAction(log.getAction())
                .build());
    }

    private int calendarCurrentMonth, calendarCurrentYear;
    private OnDateMeterFocusListener dateTouchListener;
    private PeriodCalendar calendar;
    private DateMeter selectedDate = null;
    public static AppForStatic analyticsApplication;
    public static String DeviceId;
    public static Tracker tracker;
    public Log log;
}