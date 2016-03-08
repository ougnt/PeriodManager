package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.*;
import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.event.OnDateMeterTouchEventListener;
import com.ougnt.period_manager.repository.*;
import com.ougnt.period_manager.*;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InitialActivity extends Activity {

    final int EditComment = 0x01;
    final int DisplayHelp = 0x02;
    final int DisplayMenu = 0x04;
    final int DisplaySetting = 0x08;
    final int DisplaySummary = 0x10;

    final String statUri = "http://25.9.30.106:9000/usageStat";

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

    public static final String PReviewNow =  "period_manager_preference_review_now";
    public static final String PReviewLater =  "period_manager_preference_review_later";
    public static final String PNoReview =  "period_manager_preference_review_non";

    public static final String PFetchNextMonthUsageCounter = "period_manager_preference_fetch_next_usage_counter";
    public static final String PFetchPreviousMonthUsageCounter = "period_manager_preference_fetch_previous_usage_counter";

    public static final String PMenuSettingClickCounter = "period_manager_preference_menu_setting_click_counter";
    public static final String PMenuSummaryClickCounter = "period_manager_preference_menu_summary_click_counter";
    public static final String PMenuMonthViewClickCounter = "period_manager_preference_menu_month_view_click_counter";
    public static final String PMenuHelpClickCounter = "period_manager_preference_menu_help_click_counter";

    SettingRepository setting;

    public InitialActivity(){
        dateTouchListener = null;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUsageToReview();
        setApplicationVersion();
        setDeviceId();
        addUsageCounter(PUsageCounter);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        final LinearLayout dateMeterLayout = (LinearLayout)findViewById(R.id.dateScrollerContent);

        setOnDateMeterTouchEventListener(new OnDateMeterTouchEventListener() {
            @Override
            public void onNewTouch(DateTime touchDate) {

                DateMeter currentDate = (DateMeter)dateMeterLayout.getChildAt(1);;
                for(int i = 1 ; i < dateMeterLayout.getChildCount() - 1 ; i++) {
                    DateMeter dateMeter = (DateMeter)dateMeterLayout.getChildAt(i);
                    if(dateMeter.getDate() != touchDate) {

                        dateMeter.resetFormat();

                    } else {

                        EditText comment = (EditText)findViewById(R.id.notation_text);

                        comment.setText(dateMeter.comment);
                        if(i > 1 ) {
                            currentDate = (DateMeter)dateMeterLayout.getChildAt(i);
                        }

                    }

                }

                selectedDate = touchDate;
            }
        });

        addDateMeter(dateMeterLayout, DateTime.now().minusDays(15), DateTime.now().plusDays(15), true);

        dateMeterLayout.addView(generateEndLayout(dateMeterLayout, true));
        dateMeterLayout.addView(generateEndLayout(dateMeterLayout, false), 0);

        final HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.dateScroller);

        ImageButton redButton = (ImageButton) findViewById(R.id.makePeriodButton);
        ImageButton blueButton = (ImageButton) findViewById(R.id.makeSafeZoneButton);

        redButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ((ImageButton)v).setImageResource(R.drawable.red_button_push);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ((ImageButton)v).setImageResource(R.drawable.red_button);
                        break;
                    }

                }

                return false;
            }
        });

        blueButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ((ImageButton)v).setImageResource(R.drawable.blue_button_push);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ((ImageButton)v).setImageResource(R.drawable.blue_button);
                        break;
                    }

                }

                return false;
            }
        });

        setting = SettingRepository.getSettingRepository(this);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {

                scrollView.scrollTo(dateMeterLayout.getChildAt(1).getWidth() * 15, 0);
                DateMeter today = (DateMeter) dateMeterLayout.getChildAt(16);
                today.onTouchFormat();

                if(setting.isFirstTime) {
                    setting.isFirstTime = false;
                    setting.saveSetting(getBaseContext());
                    Intent settingIntent = new Intent(getBaseContext(), SettingActivity.class);
                    settingIntent.putExtra(SettingActivity.PeriodLengthExtra, setting.periodLength);
                    settingIntent.putExtra(SettingActivity.PeriodCycleExtra, setting.periodCycle);
                    settingIntent.putExtra(SettingActivity.AverageCycleExtra, setting.averageCycle);
                    settingIntent.putExtra(SettingActivity.AverageLengthExtra, setting.averageLength);
                    settingIntent.putExtra(SettingActivity.CountExtra, setting.count);
                    settingIntent.putExtra(SettingActivity.FlagExtra, setting.flag);
                    startActivityForResult(settingIntent, DisplaySetting);
                }

                int indicatorValue = HelpIndicatorRepository.getIndicator(getBaseContext());

                if((indicatorValue & 1) == 1) {

                    Intent helpIntent = new Intent(getBaseContext(), HelpActivity.class);
                    helpIntent.putExtra("INDICATOR", indicatorValue);
                    startActivityForResult(helpIntent, DisplayHelp);
                }
            }
        }, 250);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case EditComment : { saveComment(data); break; }
            case DisplayHelp : {
                HelpIndicatorRepository.setIndicator(this, data.getIntExtra("INDICATOR",1));
                break;
            }
            case DisplayMenu : {
                onDisplayMenuResult(data);
                break;
            }
            case DisplaySetting: {

                onDisplaySettingResult(data);
                break;
            }
            case DisplaySummary : {

                SummaryRepository summary = new SummaryRepository();

                summary.expectedMenstrualDateFrom = (DateTime.parse(data.getExtras().get(SummaryActivity.NextMenstrualFromExtra).toString()));
                summary.expectedMenstrualDateTo = (DateTime.parse(data.getExtras().get(SummaryActivity.NextMenstrualToExtra).toString()));
                summary.expectedOvulationDateFrom = (DateTime.parse(data.getExtras().get(SummaryActivity.NextOvulationFromExtra).toString()));
                summary.expectedOvulationDateTo = (DateTime.parse(data.getExtras().get(SummaryActivity.NextOvulationToExtra).toString()));

                summary.save(this);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        if(getUsageCounter(PUsageCounter) == getUsageCounter(PTimeOfUsageBeforeReview)) {

            setContentView(R.layout.review);

            Button reviewNowButton = (Button)findViewById(R.id.review_open);
            Button laterButton = (Button)findViewById(R.id.review_later);
            Button noShowButton = (Button)findViewById(R.id.review_no_show);

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
                    edit.putInt(PTimeOfUsageBeforeReview, pref.getInt(PTimeOfUsageBeforeReview,0) + 2);
                    edit.commit();
                    submitStat();
                    finish();
                }
            });

            noShowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addUsageCounter(PNoReview);
                    edit.putInt(PTimeOfUsageBeforeReview,-1);
                    edit.commit();
                    submitStat();
                    finish();
                }
            });

        } else {
            submitStat();
            finish();
        }
    }

    public void initialHelpActivity(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra("INDICATOR", HelpIndicatorRepository.getIndicator(getBaseContext()));
        startActivityForResult(intent, DisplayHelp);
    }

    public void hamburgerMenuClick(View view) {

        addUsageCounter(PMenuButtonUsageCounter);
        Intent intent = new Intent(this, MenuActivity.class);
        startActivityForResult(intent, DisplayMenu);
    }

    public void commentSave(View view) {
        // save
        if(selectedDate == null) {return;}

        if(view.getId() == R.id.comment_button) {
            addUsageCounter(PCommentButtonUsageCounter);
        } else if(view.getId() == R.id.notation_text) {
            addUsageCounter(PCommentTextUsageCounter);
        }

        EditText commentText = (EditText)findViewById(R.id.notation_text);
        Intent commentIntent = new Intent(this, CommentActivity.class);
        commentIntent.putExtra("Date", selectedDate.toString("yyyy-MM-dd"));
        commentIntent.putExtra("Comment", commentText.getText().toString());
        startActivityForResult(commentIntent, EditComment);
    }

    public void buttonClickHandler(View srcView) {

        if(selectedDate == null) {return;}

        int index = 0;
        int newType = 0;

        LinearLayout v = (LinearLayout)findViewById(R.id.dateScrollerContent);
        for(int i = 1 ; i < v.getChildCount(); i++) {

            if(((DateMeter)v.getChildAt(i)).getDate() == selectedDate) {

                index = i;
                break;
            }
        }

        if(srcView.getId() == R.id.makePeriodButton) {

            addUsageCounter(PPeriodButtonUsageCounter);
            ((DateMeter)(v.getChildAt(index))).changeColor(DateMeter.MenstrualColor, DateMeter.Menstrual);
            DateTime dateToBePainted = ((DateMeter)(v.getChildAt(index))).getDate();
            paintDateMeter(dateToBePainted, dateToBePainted.plusDays((int)setting.periodLength - 1), DateMeter.Menstrual);
            paintDateMeter(dateToBePainted.plusDays(7), dateToBePainted.plusDays((int)setting.periodCycle - 7), DateMeter.Ovulation);
            paintDateMeter(dateToBePainted.plusDays((int)setting.periodCycle - 7), dateToBePainted.plusDays((int)setting.periodCycle - 1), DateMeter.Nothing);
            paintDateMeter(dateToBePainted.plusDays((int)setting.periodCycle - 1), dateToBePainted.plusDays((int)setting.periodCycle + 1), DateMeter.Menstrual);
            newType = DateMeter.Menstrual;

            Intent summaryIntent = new Intent(this, SummaryActivity.class);
            summaryIntent.putExtra(SummaryActivity.NextMenstrualFromExtra, dateToBePainted.plusDays((int)setting.periodCycle - 1).toString("yyyy-MM-dd"));
            summaryIntent.putExtra(SummaryActivity.NextMenstrualToExtra, dateToBePainted.plusDays((int)setting.periodCycle + 1).toString("yyyy-MM-dd"));
            summaryIntent.putExtra(SummaryActivity.NextOvulationFromExtra, dateToBePainted.plusDays(6).toString("yyyy-MM-dd"));
            summaryIntent.putExtra(SummaryActivity.NextOvulationToExtra, dateToBePainted.plusDays((int)setting.periodCycle - 8).toString("yyyy-MM-dd"));

            startActivityForResult(summaryIntent, DisplaySummary);

        } else if(srcView.getId() == R.id.makeSafeZoneButton) {

            addUsageCounter(PNonPeriodButtonUsageCounter);
            ((DateMeter)(v.getChildAt(index))).changeColor(DateMeter.SafeZoneColor, DateMeter.Nothing);
            newType = DateMeter.Nothing;
        } else {return;}

        DateRepository.updateDateRepository(this, ((DateMeter)(v.getChildAt(index))).getDate(), newType);

    }

    public void setOnDateMeterTouchEventListener(OnDateMeterTouchEventListener listener) {
        dateTouchListener = listener;
    }

    private void submitStat() {
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(statUri);

        JSONObject json = new JSONObject();

        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
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

                    StringEntity entry = new StringEntity(json.toString());

                    httpPost.setEntity(entry);
                    httpPost.setHeader("Content-Type","application/Json");

                    client.execute(httpPost);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute();
    }

    private void initUsageToReview() {

        SharedPreferences pref = getSharedPreferences(PName, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(PTimeOfUsageBeforeReview, pref.getInt(PTimeOfUsageBeforeReview, 2));
        edit.commit();
    }

    private void setDeviceId() {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(PUuid, pref.getString(PUuid, UUID.randomUUID().toString()));
        edit.commit();
    }

    private UUID getDeviceId() {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        return UUID.fromString(pref.getString(PUuid, ""));
    }

    private void setApplicationVersion(){

        SharedPreferences pref = getSharedPreferences(PName,MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(PCurrentVersion,23);
        edit.commit();
    }

    private void addUsageCounter(String key) {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key, pref.getInt(key, 0) + 1);
        edit.commit();
    }

    private int getUsageCounter(String key) {

        SharedPreferences pref = getSharedPreferences(PName, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    private void onDisplayMenuResult(Intent data) {
        int selectedMenu = data.getIntExtra(MenuActivity.SelectedMenuExtra, 0);
        switch (selectedMenu) {
            case MenuActivity.SelectDisplayHelp : {
                addUsageCounter(PMenuHelpClickCounter);
                initialHelpActivity(null);
                break;
            }
            case MenuActivity.SelectSummary: {
                addUsageCounter(PMenuSummaryClickCounter);
                SummaryRepository summary = SummaryRepository.getSummary(this);
                if(summary != null) {
                    Intent summaryIntent = new Intent(this, SummaryActivity.class);
                    summaryIntent.putExtra(SummaryActivity.NextMenstrualFromExtra, summary.expectedMenstrualDateFrom.toString("yyyy-MM-dd"));
                    summaryIntent.putExtra(SummaryActivity.NextMenstrualToExtra, summary.expectedMenstrualDateTo.toString("yyyy-MM-dd"));
                    summaryIntent.putExtra(SummaryActivity.NextOvulationFromExtra, summary.expectedOvulationDateFrom.toString("yyyy-MM-dd"));
                    summaryIntent.putExtra(SummaryActivity.NextOvulationToExtra, summary.expectedOvulationDateTo.toString("yyyy-MM-dd"));

                    startActivityForResult(summaryIntent, DisplaySummary);
                }
                break;
            }
            case MenuActivity.SelectDisplaySetting : {
                addUsageCounter(PMenuSettingClickCounter);
                Intent settingIntent = new Intent(this, SettingActivity.class);
                settingIntent.putExtra(SettingActivity.PeriodLengthExtra, setting.periodLength);
                settingIntent.putExtra(SettingActivity.PeriodCycleExtra, setting.periodCycle);
                settingIntent.putExtra(SettingActivity.AverageCycleExtra, setting.averageCycle);
                settingIntent.putExtra(SettingActivity.AverageLengthExtra, setting.averageLength);
                settingIntent.putExtra(SettingActivity.CountExtra, setting.count);
                settingIntent.putExtra(SettingActivity.FlagExtra, setting.flag);
                startActivityForResult(settingIntent, DisplaySetting);
                break;
            }
            case MenuActivity.SelectMonthView : {
                addUsageCounter(PMenuMonthViewClickCounter);
                Intent monthViewIntent = new Intent(this, MonthViewActivity.class);
                monthViewIntent.putExtra(MonthViewActivity.MonthExtra, selectedDate.getMonthOfYear());
                monthViewIntent.putExtra(MonthViewActivity.YearExtra, selectedDate.getYear());

                startActivity(monthViewIntent);
            }
        }
    }

    private void onDisplaySettingResult(Intent data) {
        switch (data.getIntExtra(SettingActivity.ActionExtra, 0)) {

            case SettingActivity.SaveAction : {

                setting.periodCycle = data.getFloatExtra(SettingActivity.PeriodCycleExtra, 0f);
                setting.periodLength = data.getFloatExtra(SettingActivity.PeriodLengthExtra, 0f);
                setting.flag = data.getIntExtra(SettingActivity.FlagExtra, 0);
                setting.saveSetting(this);

                LinearLayout.LayoutParams visibleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0.9f);
                LinearLayout.LayoutParams hideParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0);

                View dayView = findViewById(R.id.day_view);

                if((setting.flag & SettingRepository.FlagCalendarMonthView) != 0 ) {

                    // TODO : swap to month view
                    dayView.setVisibility(View.GONE);
                } else {

                    // TODO : swap to day view
                    dayView.setVisibility(View.VISIBLE);
                }
                break;
            }
            case SettingActivity.CancelAction : {


                break;
            }
        }
    }

    private void saveComment(Intent intentResult) {

        if(intentResult == null) {return;}

        String date = intentResult.getExtras().get("Date").toString();
        DateTime targetDate = DateTime.parse(date);
        String comment = intentResult.getExtras().get("Comment").toString();
        DatabaseRepositoryHelper helper = new DatabaseRepositoryHelper(this);

        final LinearLayout dateLayout = (LinearLayout)findViewById(R.id.dateScrollerContent);
        final EditText commentText = (EditText)findViewById(R.id.notation_text);

        DateRepository.updateDateRepository(this, targetDate, comment);

        DateTime firstDate = ((DateMeter) dateLayout.getChildAt(1)).getDate();
        int dateDiff = (int)((targetDate.getMillis() - firstDate.getMillis()) / 1000 / 60 / 60 / 24);
        DateMeter targetDateToSave = (DateMeter)dateLayout.getChildAt(1 + dateDiff);
        targetDateToSave.comment = comment;
        commentText.setText(comment);
    }

    private void paintDateMeter(DateTime startDate, DateTime endDate, int type) {

        LinearLayout dateMeterLayout = (LinearLayout)findViewById(R.id.dateScrollerContent);

        for(int i = 1; i < dateMeterLayout.getChildCount() -1 ; i++) {

            DateMeter targetDateMeter = ((DateMeter)dateMeterLayout.getChildAt(i));
            if(targetDateMeter.getDate().compareTo(endDate) <= 0 && targetDateMeter.getDate().compareTo(startDate) >= 0) {
                switch(type) {
                    case DateMeter.Menstrual : ((DateMeter)dateMeterLayout.getChildAt(i)).changeColor(DateMeter.MenstrualColor, DateMeter.Menstrual); break;
                    case DateMeter.Ovulation : ((DateMeter)dateMeterLayout.getChildAt(i)).changeColor(DateMeter.OvulationColor, DateMeter.Ovulation); break;
                    case DateMeter.Nothing : ((DateMeter)dateMeterLayout.getChildAt(i)).changeColor(DateMeter.SafeZoneColor, DateMeter.Nothing); break;
                }

            }
        }

        for(int i = 0; i <= (endDate.getMillis() - startDate.getMillis()) / 86400000 ; i++) {

            DateRepository.updateDateRepository(this, startDate.plusDays(i), type);
        }
    }

    private LinearLayout generateEndLayout(LinearLayout callbackLayout) {

        return generateEndLayout(callbackLayout, true);
    }

    private LinearLayout generateEndLayout(LinearLayout callbackLayout, boolean isRightEnd) {

        FetchingButton retLayout = new FetchingButton(this, isRightEnd);

        retLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRightEnd) {

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
                            HorizontalScrollView parant = (HorizontalScrollView)callbackLayout.getParent();
                            parant.scrollTo(callbackLayout.getChildAt(1).getWidth() * 15, 0);
                        }
                    }, 50);
                }
            }
        });

        return retLayout;
    }

    private void addDateMeter(LinearLayout targetLayout, DateTime startDate, DateTime endDate, boolean isRight) {

        List<DateRepository> dates = DateRepository.getDateRepositories(this, startDate, endDate);


        Cursor c = new DatabaseRepositoryHelper(this).getReadableDatabase().rawQuery("SELECT * FROM DATE_REPOSITORY WHERE date = '2015-11-15'",  null);
        c.moveToFirst();

        // to setting up the DateMeter's color
        new DateMeter(this, DateTime.now(), 0, null, null, 0);

        if(isRight) {
            for (int i = 0; i < dates.size(); i++) {

                int color = 0;
                switch (dates.get(i).dateType) {
                    case DateMeter.Menstrual:
                        color = DateMeter.MenstrualColor;
                        break;
                    case DateMeter.Ovulation:
                        color = DateMeter.OvulationColor;
                        break;
                    case DateMeter.Nothing:
                    default:
                        color = DateMeter.SafeZoneColor;
                        break;
                }
                targetLayout.addView(new DateMeter(this, dates.get(i).date, color, dateTouchListener, dates.get(i).comment, dates.get(i).dateType));
            }
        } else {
            for(int i = dates.size() - 1; i >= 0; i--) {

                int color = 0;
                switch (dates.get(i).dateType) {
                    case DateMeter.Menstrual: color = DateMeter.MenstrualColor; break;
                    case DateMeter.Ovulation: color = DateMeter.OvulationColor; break;
                    case DateMeter.Nothing:
                    default: color = DateMeter.SafeZoneColor; break;
                }
                targetLayout.addView(new DateMeter(this, dates.get(i).date, color, dateTouchListener, dates.get(i).comment, dates.get(i).dateType), 0);
            }
        }
    }

    private OnDateMeterTouchEventListener dateTouchListener;
    private DateTime selectedDate = null;
    private Context _thisContext = this;
}