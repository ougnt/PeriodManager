package com.ougnt.period_manager;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ougnt.period_manager.activity.AppForStatic;
import com.ougnt.period_manager.activity.helper.FlagHelper;
import com.ougnt.period_manager.event.OnDateMeterFocusListener;
import com.ougnt.period_manager.exception.NotImplementException;
import com.ougnt.period_manager.handler.HttpHelper;
import com.ougnt.period_manager.repository.IDateRepository;
import com.ougnt.period_manager.tests.MockDateRepository;

import org.joda.time.DateTime;

import java.util.HashMap;

/**
 * * # Created by wacharint on 10/26/15.
 */
public class DateMeter extends LinearLayout {

    public static final int Menstrual = 0x01;
    public static final int PossiblyOvulation = 0x02;
    public static final int OvulationDate = 0x04;
    public static final int Nothing = 0x00;

    public static final int MenstrualColor;
    public static final int PossiblyOvulationColor;
    public static final int OvulationDateColor;
    public static final int SafeZoneColor;
    public static final int OnSelectColor;
    public static final int TextColor;
    public static final int TodayTextColor;

    public static final HashMap<Integer, Integer> ColorForDateType = new HashMap<>();

    static {
        if (AppForStatic.getContext() == null) {
            MenstrualColor = 0xFFF3CDFF;
            PossiblyOvulationColor = 0xFFA1FF97;
            OvulationDateColor = 0xFF01FF01;
            SafeZoneColor = 0xFFBEFCF0;
            OnSelectColor = 0xFF8989FF;
            TextColor = 0xFF7F7F7F;
            TodayTextColor = 0xFFEF7A54;
        } else {
            MenstrualColor = ContextCompat.getColor(AppForStatic.getContext(), R.color.menstrual_zone_bg);
            PossiblyOvulationColor = ContextCompat.getColor(AppForStatic.getContext(), R.color.ovulation_zone_bg);
            OvulationDateColor = ContextCompat.getColor(AppForStatic.getContext(), R.color.ovulation_date_bg);
            SafeZoneColor = ContextCompat.getColor(AppForStatic.getContext(), R.color.safe_zone_bg);
            OnSelectColor = ContextCompat.getColor(AppForStatic.getContext(), R.color.on_select_zone_bg);
            TextColor = ContextCompat.getColor(AppForStatic.getContext(), R.color.text_color);
            TodayTextColor = ContextCompat.getColor(AppForStatic.getContext(), R.color.today_text_color);
        }
        ColorForDateType.put(Menstrual, MenstrualColor);
        ColorForDateType.put(PossiblyOvulation, PossiblyOvulationColor);
        ColorForDateType.put(OvulationDate, OvulationDateColor);
        ColorForDateType.put(Nothing, SafeZoneColor);
    }

    static int IconSize = 0;

    // Only for the visualize tool don't use.
    public DateMeter(Context context, AttributeSet attrib) throws NotImplementException {

        this(context, new MockDateRepository(), null);

        if (!isInEditMode()) {
            throw new NotImplementException();
        }
    }

    public DateMeter(Context context, IDateRepository initialDate, OnDateMeterFocusListener listener) {
        super(context);

        try {
            comment = initialDate.comment;
            dateType = initialDate.dateType;
            temperature = initialDate.temperature;
            date = initialDate.date;
            flags = initialDate.flags;

            // Wait until the static method fill in the ColorForDateType
            while (ColorForDateType == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (ColorForDateType.size() < 4) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            _mainColor = ColorForDateType.get(dateType) == null ? Nothing : ColorForDateType.get(dateType);

            this.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.setLayoutParams(params);
            this.setGravity(Gravity.CENTER);

            _leftHorizontalLayout = formatHorizontalMarginLayout(new LinearLayout(context));
            LinearLayout _contentHorizontalLayout = generateContentLayout();
            _rightHorizontalLayout = formatHorizontalMarginLayout(new LinearLayout(context));

            this.addView(_leftHorizontalLayout);
            this.addView(_contentHorizontalLayout);
            this.addView(_rightHorizontalLayout);
            this._listener = listener;

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeSelectedFormat();
                }
            });
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    public String comment = "";

    public float temperature = 0f;

    public DateTime getDate() {
        return date;
    }

    public void makeSelectedFormat() {
        try {
            _leftHorizontalLayout.setBackgroundColor(OnSelectColor);
            _rightHorizontalLayout.setBackgroundColor(OnSelectColor);
            _contentTopLayout.setBackgroundColor(OnSelectColor);
            _contentBottomLayout.setBackgroundColor(OnSelectColor);
            _listener.onFocusMoveIn(this);
            this.setBackgroundColor(OnSelectColor);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    public void resetFormat() {
        try {
            _leftHorizontalLayout.setBackgroundColor(Color.GRAY);
            _rightHorizontalLayout.setBackgroundColor(Color.GRAY);
            _contentTopLayout.setBackgroundColor(_mainColor);
            _contentBottomLayout.setBackgroundColor(_mainColor);
            this.setBackgroundColor(_mainColor);
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    public void changeColor(int newDateType) {

        try {
            dateType = newDateType;
            int newColor = ColorForDateType.get(dateType);

            _mainColor = newColor;
            _leftSideLayout.setBackgroundColor(newColor);
            _dayTextLayout.setBackgroundColor(newColor);
            _monthLayout.setBackgroundColor(newColor);
            _rightSideLayout.setBackgroundColor(newColor);
            _monthText.setBackgroundColor(newColor);
            _monthTextLayout.setBackgroundColor(newColor);
            _centralLayout.setBackgroundColor(newColor);
            _upperMonthLayout.setBackgroundColor(newColor);
            _iconLayout.setBackgroundColor(newColor);
            _contentTopLayout.setBackgroundColor(newColor);
            _contentBottomLayout.setBackgroundColor(newColor);

            this.setBackgroundColor(newColor);

            formatIconVisibilityByDateType();
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private LinearLayout formatIconLayout(LinearLayout layout) {

        try {
            LayoutParams retParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(retParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setBackgroundColor(_mainColor);

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x == 0 ? 2400 : size.x;

            IconSize = width / 20;

            LayoutParams iconParam = new LayoutParams(IconSize, IconSize);

            _menstrualIcon = new ImageView(getContext());
            _menstrualIcon.setImageResource(R.drawable.menstrual_icon);
            _menstrualIcon.setScaleType(ImageView.ScaleType.FIT_END);
            _menstrualIcon.setLayoutParams(iconParam);

            _ovulationIcon = new ImageView(getContext());
            _ovulationIcon.setImageResource(R.drawable.ovulation_icon);
            _ovulationIcon.setScaleType(ImageView.ScaleType.FIT_END);
            _ovulationIcon.setLayoutParams(iconParam);

            _nonOvulationIcon = new ImageView(getContext());
            _nonOvulationIcon.setImageResource(R.drawable.non_ovulation_icon);
            _nonOvulationIcon.setScaleType(ImageView.ScaleType.FIT_END);
            _nonOvulationIcon.setLayoutParams(iconParam);

            _emotionIcon = new ImageView(getContext());
            switch (FlagHelper.GetEmotionFlag(flags)) {
                case FlagHelper.EmotionNothingIcon: {
                    _emotionIcon.setImageResource(R.drawable.emotion_nothing);
                    break;
                }
                case FlagHelper.EmotionSadIcon: {
                    _emotionIcon.setImageResource(R.drawable.emotion_sad);
                    break;
                }
                case FlagHelper.EmotionAngryIcon: {
                    _emotionIcon.setImageResource(R.drawable.emotion_angry);
                    break;
                }
                case FlagHelper.EmotionHappyIcon: {
                    _emotionIcon.setImageResource(R.drawable.emotion_happy);
                    break;
                }
            }
            _emotionIcon.setScaleType(ImageView.ScaleType.FIT_END);
            _emotionIcon.setLayoutParams(iconParam);

            _intercourseIcon = new ImageView(getContext());
            _intercourseIcon.setImageResource(R.drawable.intercourse_icon);
            _intercourseIcon.setScaleType(ImageView.ScaleType.FIT_END);
            _intercourseIcon.setLayoutParams(iconParam);


            formatIconVisibilityByDateType();

            LinearLayout iconPart = new LinearLayout(layout.getContext());

            LayoutParams iconParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.8f);
            iconPart.setLayoutParams(iconParams);
            iconPart.setGravity(Gravity.BOTTOM | Gravity.LEFT);

            iconPart.addView(_menstrualIcon);
            iconPart.addView(_ovulationIcon);
            iconPart.addView(_nonOvulationIcon);
            iconPart.addView(_emotionIcon);
            iconPart.addView(_intercourseIcon);

            layout.addView(iconPart);

            return layout;
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
            return layout;
        }
    }

    private void formatIconVisibilityByDateType() {

        try {
            switch (dateType) {
                case Menstrual: {
                    _menstrualIcon.setVisibility(VISIBLE);
                    _nonOvulationIcon.setVisibility(VISIBLE);
                    _ovulationIcon.setVisibility(GONE);
                    break;
                }
                case OvulationDate:
                case PossiblyOvulation: {
                    _menstrualIcon.setVisibility(GONE);
                    _ovulationIcon.setVisibility(VISIBLE);
                    _nonOvulationIcon.setVisibility(GONE);
                    break;
                }
                case Nothing: {
                    _menstrualIcon.setVisibility(GONE);
                    _ovulationIcon.setVisibility(GONE);
                    _nonOvulationIcon.setVisibility(VISIBLE);
                    break;
                }
            }

            _emotionIcon.setVisibility(VISIBLE);
            if (FlagHelper.GetIntercourseFlag(flags) == FlagHelper.HaveIntercourseFlag) {
                _intercourseIcon.setVisibility(VISIBLE);
            } else {
                _intercourseIcon.setVisibility(GONE);
            }
        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private LinearLayout formatVerticalMarginLayout(LinearLayout layout) {

        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
        layout.setBackgroundColor(_mainColor);
        return layout;
    }

    private LinearLayout formatHorizontalMarginLayout(LinearLayout layout) {

        layout.setLayoutParams(new LayoutParams(2, LayoutParams.MATCH_PARENT));
        layout.setBackgroundColor(Color.GRAY);
        return layout;
    }

    private LinearLayout generateContentLayout() {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout retLayout = new LinearLayout(getContext());
        retLayout.setLayoutParams(params);

        retLayout.setBackgroundColor(ColorForDateType.get(dateType));
        retLayout.setOrientation(LinearLayout.VERTICAL);

        _contentTopLayout = formatVerticalMarginLayout(new LinearLayout(retLayout.getContext()));
        _centralLayout = new LinearLayout(retLayout.getContext());
        _contentBottomLayout = formatVerticalMarginLayout(new LinearLayout(retLayout.getContext()));

        _leftSideLayout = generateSideLayout();
        _dayTextLayout = generateDayText(date);
        _monthLayout = generateMonthLayout(date);
        _rightSideLayout = generateSideLayout();
        _iconLayout = formatIconLayout(new LinearLayout(getContext()));

        _centralLayout.addView(_leftSideLayout);
        _centralLayout.addView(_dayTextLayout);
        _centralLayout.addView(_monthLayout);
        _centralLayout.addView(_rightSideLayout);

        retLayout.addView(_contentTopLayout);
        retLayout.addView(_centralLayout);
        retLayout.addView(_iconLayout);
        retLayout.addView(_contentBottomLayout);

        return retLayout;
    }

    private LinearLayout generateSideLayout() {

        LinearLayout retLayout = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(10, LayoutParams.MATCH_PARENT);
        retLayout.setLayoutParams(params);
        retLayout.setBackgroundColor(_mainColor);

        return retLayout;
    }

    private TextView generateDayText(DateTime date) {

        TextView returnText = new TextView(this.getContext());
        String day = date.getDayOfMonth() + "";
        if (day.length() == 1) {
            day = "0" + day;
        }

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        returnText.setTextSize(getProperFontSize());
        if (date.toString("yyyy-MM-dd").equals(DateTime.now().toString("yyyy-MM-dd"))) {
            returnText.setTextColor(TodayTextColor);
        } else {
            returnText.setTextColor(TextColor);
        }
        returnText.setLayoutParams(params);
        returnText.setText(day);
        returnText.setBackgroundColor(_mainColor);

        return returnText;
    }

    private LinearLayout generateMonthLayout(DateTime date) {

        LinearLayout retLayout = new LinearLayout(getContext());

        _upperMonthLayout = new LinearLayout(retLayout.getContext());
        _monthTextLayout = new LinearLayout(retLayout.getContext());
        _monthText = new TextView(_monthTextLayout.getContext());
        TextView _dayOfWeekText = new TextView(_monthTextLayout.getContext());
        LinearLayout.LayoutParams upperParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.65f);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.3f);

        retLayout.setOrientation(LinearLayout.VERTICAL);
        retLayout.setWeightSum(1);
        retLayout.setBackgroundColor(_mainColor);
        retLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        _upperMonthLayout.setLayoutParams(upperParams);
        _upperMonthLayout.setBackgroundColor(_mainColor);
        _monthTextLayout.setLayoutParams(textParams);
        _monthTextLayout.setBackgroundColor(_mainColor);

        _monthText.setText(date.toString("MMM"));
        _monthText.setTextSize(getProperMonthFontSize());
        _monthText.setTextColor(TextColor);

        _dayOfWeekText.setText(getDayOfWeek(date));
        _dayOfWeekText.setTextSize(getProperMonthFontSize());
        _dayOfWeekText.setTextColor(TextColor);

        _monthTextLayout.addView(_monthText);
        _upperMonthLayout.addView(_dayOfWeekText);

        retLayout.addView(_upperMonthLayout);
        retLayout.addView(_monthTextLayout);

        return retLayout;
    }

    private String getDayOfWeek(DateTime targetDate) {

        int day = targetDate.getDayOfWeek();

        switch (day) {
            case 1:
                return getResources().getString(R.string.monday);
            case 2:
                return getResources().getString(R.string.tuesday);
            case 3:
                return getResources().getString(R.string.wednesday);
            case 4:
                return getResources().getString(R.string.thursday);
            case 5:
                return getResources().getString(R.string.friday);
            case 6:
                return getResources().getString(R.string.saturday);
            case 7:
                return getResources().getString(R.string.sunday);
            default:
                return "";
        }
    }

    private int getProperMonthFontSize() {
        return getProperFontSize() / 3;
    }

    private int getProperFontSize() {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        switch (getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) {

            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return 84;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return 63;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return 42;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return 21;
        }

        return height / 28;
    }

    public long getFlags() {
        return flags;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    protected DateTime date;
    private long flags;
    private int _mainColor;

    private LinearLayout _leftHorizontalLayout;
    private LinearLayout _rightHorizontalLayout;
    private LinearLayout _contentTopLayout;
    private LinearLayout _contentBottomLayout;
    private LinearLayout _centralLayout;
    private LinearLayout _leftSideLayout;
    private TextView _dayTextLayout;
    private TextView _monthText;
    private LinearLayout _monthLayout;
    private LinearLayout _rightSideLayout;
    private LinearLayout _upperMonthLayout;
    private LinearLayout _monthTextLayout;
    private LinearLayout _iconLayout;

    private ImageView _menstrualIcon;
    private ImageView _ovulationIcon;
    private ImageView _nonOvulationIcon;
    private ImageView _emotionIcon;
    private ImageView _intercourseIcon;
    public int dateType = 0;

    private OnDateMeterFocusListener _listener;
}
