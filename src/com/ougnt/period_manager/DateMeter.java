package com.ougnt.period_manager;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Layout;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ougnt.period_manager.event.OnDateMeterTouchEventListener;
import org.joda.time.DateTime;
import org.w3c.dom.Text;

/**
 * * # Created by wacharint on 10/26/15.
 */
public class DateMeter extends LinearLayout {

    static final int Menstrual = 0x01;
    static final int Ovulation = 0x02;
    static final int Nothing = 0x00;

    static int MenstrualColor = Color.RED;
    static int OvulationColor = Color.GREEN;
    static int SafeZoneColor = Color.CYAN;
    static int OnSelectColor = Color.BLUE;
    static int TextColor = Color.BLACK;

    static int IconWidth = 0;

    public DateMeter(Context context, DateTime initialDate, int color, OnDateMeterTouchEventListener listener, String comment, int dateType ) {
        super(context);


        SafeZoneColor = getResources().getColor(R.color.safe_zone_bg);
        MenstrualColor = getResources().getColor(R.color.menstrual_zone_bg);
        OvulationColor = getResources().getColor(R.color.ovulation_zone_bg);
        OnSelectColor = getResources().getColor(R.color.on_select_zone_bg);
        TextColor = getResources().getColor(R.color.text_color);

        if(initialDate.toString("yyyy-MM-dd").equals(DateTime.now().toString("yyyy-MM-dd"))) {
            TextColor = getResources().getColor(R.color.today_text_color);
        }

        this.comment = comment;
        this.dateType = dateType;

        _mainColor = color;
        setBackgroundColor(getResources().getColor(R.color.main_bg));
        this.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);
        this.setGravity(Gravity.CENTER);

        this.setBackgroundColor(_mainColor);

        _leftHorizontalLayout = formatHorizontalMarginLayout(new LinearLayout(context));
        _contentHorizontalLayout = generateContentLayout(initialDate);
        _rightHorizontalLayout = formatHorizontalMarginLayout(new LinearLayout(context));

        this.addView(_leftHorizontalLayout);
        this.addView(_contentHorizontalLayout);
        this.addView(_rightHorizontalLayout);
        this._listener = listener;
        this.date = initialDate;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTouchFormat();
            }
        });
    }

    public String comment = "";

    public DateTime getDate() {return date;}

    public boolean isMenstrual() {

        return (dateType & Menstrual) == Menstrual;
    }

    public boolean isOvulation() {

        return (dateType & Ovulation) == Ovulation;
    }

    public void onTouchFormat() {
        _leftHorizontalLayout.setBackgroundColor(OnSelectColor);
        _rightHorizontalLayout.setBackgroundColor(OnSelectColor);
        _contentTopLayout.setBackgroundColor(OnSelectColor);
        _contentBottomLayout.setBackgroundColor(OnSelectColor);
        _listener.onNewTouch(date);
        this.setBackgroundColor(OnSelectColor);
    }

    public void resetFormat() {
        _leftHorizontalLayout.setBackgroundColor(Color.GRAY);
        _rightHorizontalLayout.setBackgroundColor(Color.GRAY);
        _contentTopLayout.setBackgroundColor(_mainColor);
        _contentBottomLayout.setBackgroundColor(_mainColor);
        this.setBackgroundColor(_mainColor);
    }

    public void changeColor(int newColor, int newDateType) {

        _mainColor = newColor;
        _leftSideLayout.setBackgroundColor(newColor);
        _dayTextLayout.setBackgroundColor(newColor);
        _monthLayout.setBackgroundColor(newColor);
        _rightSideLayout.setBackgroundColor(newColor);
        _monthText.setBackgroundColor(newColor);
        _monthTextLayout.setBackgroundColor(newColor);
        _upperMonthLayout.setBackgroundColor(newColor);
        _iconLayout.setBackgroundColor(newColor);
        this.setBackgroundColor(newColor);

        LayoutParams visibleParams = new LayoutParams(IconWidth, LayoutParams.MATCH_PARENT);
        LayoutParams invisibleParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);

        if((newDateType & Menstrual) == Menstrual) {

            _menstrualIcon.setVisibility(VISIBLE);
            _menstrualIcon.setLayoutParams(visibleParams);
        } else {

            _menstrualIcon.setVisibility(INVISIBLE);
            _menstrualIcon.setLayoutParams(invisibleParams);
        }

        if((newDateType & Ovulation) == Ovulation) {

            _ovulationIcon.setVisibility(VISIBLE);
            _nonOvulationIcon.setVisibility(INVISIBLE);
            _ovulationIcon.setLayoutParams(visibleParams);
            _nonOvulationIcon.setLayoutParams(invisibleParams);
        } else {

            _ovulationIcon.setVisibility(INVISIBLE);
            _nonOvulationIcon.setVisibility(VISIBLE);
            _ovulationIcon.setLayoutParams(invisibleParams);
            _nonOvulationIcon.setLayoutParams(visibleParams);
        }
    }

    protected DateTime date;

    private int _mainColor;

    private LinearLayout formatIconLayout(LinearLayout layout) {

        LinearLayout.LayoutParams retParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.3f);
        layout.setWeightSum(1);
        layout.setLayoutParams(retParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(_mainColor);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        IconWidth = width / 14;

        LinearLayout.LayoutParams visibleParams = new LinearLayout.LayoutParams(IconWidth, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams invisibleParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);

        _menstrualIcon = new ImageView(getContext());
        _menstrualIcon.setImageResource(R.drawable.menstrual_icon);
        _menstrualIcon.setLayoutParams(visibleParams);
        if ((dateType & Menstrual) != Menstrual) {

            _menstrualIcon.setVisibility(INVISIBLE);
            _menstrualIcon.setLayoutParams(invisibleParams);
        }
        layout.addView(_menstrualIcon);

        _ovulationIcon = new ImageView(getContext());
        _ovulationIcon.setImageResource(R.drawable.ovulation_icon);
        _ovulationIcon.setLayoutParams(visibleParams);
        layout.addView(_ovulationIcon);

        _nonOvulationIcon = new ImageView(getContext());
        _nonOvulationIcon.setImageResource(R.drawable.non_ovulation_icon);
        _nonOvulationIcon.setLayoutParams(visibleParams);
        if ((dateType & Ovulation) != Nothing) {

            _nonOvulationIcon.setVisibility(INVISIBLE);
            _ovulationIcon.setVisibility(VISIBLE);
            _nonOvulationIcon.setLayoutParams(invisibleParams);
        } else {

            _nonOvulationIcon.setVisibility(VISIBLE);
            _ovulationIcon.setVisibility(INVISIBLE);
            _ovulationIcon.setLayoutParams(invisibleParams);
        }
        layout.addView(_nonOvulationIcon);

        return layout;
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

    private LinearLayout generateContentLayout(DateTime initialDate) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout retLayout = new LinearLayout(getContext());
        retLayout.setLayoutParams(params);
        retLayout.setBackgroundColor(getResources().getColor(R.color.main_bg));
        retLayout.setOrientation(LinearLayout.VERTICAL);

        _contentTopLayout = formatVerticalMarginLayout(new LinearLayout(retLayout.getContext()));
        _centralLayout = new LinearLayout(retLayout.getContext());
        _contentBottomLayout =  formatVerticalMarginLayout(new LinearLayout(retLayout.getContext()));

        _leftSideLayout = generateSideLayout();
        _dayTextLayout = generateDayText(initialDate);
        _monthLayout = generateMonthLayout(initialDate);
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
        LayoutParams params = new LayoutParams(10,LayoutParams.MATCH_PARENT);
        retLayout.setLayoutParams(params);
        retLayout.setBackgroundColor(_mainColor);

        return retLayout;
    }

    private TextView generateDayText(DateTime date) {

        TextView returnText = new TextView(this.getContext());
        String day = date.getDayOfMonth() + "";
        if(day.length() == 1) { day = "0" + day;}

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        returnText.setTextSize(getProperFontSize());
        returnText.setTextColor(TextColor);
        returnText.setLayoutParams(params);
        returnText.setText(day);
        returnText.setBackgroundColor(_mainColor);

        return returnText;
    }

    private LinearLayout generateMonthLayout(DateTime date){

        LinearLayout retLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams retParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        retLayout.setLayoutParams(retParams);
        _upperMonthLayout = new LinearLayout(retLayout.getContext());
        _monthTextLayout = new LinearLayout(retLayout.getContext());
        _lowerMonthLayout = new LinearLayout(retLayout.getContext());
        _monthText = new TextView(_monthTextLayout.getContext());
        _dayOfWeekText = new TextView(_monthTextLayout.getContext());
        LinearLayout.LayoutParams upperParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,0.65f);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,0.3f);
        LinearLayout.LayoutParams lowerParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,0.05f);

        retLayout.setOrientation(LinearLayout.VERTICAL);
        retLayout.setWeightSum(1);
        retLayout.setBackgroundColor(_mainColor);
        retLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        _upperMonthLayout.setLayoutParams(upperParams);
        _upperMonthLayout.setBackgroundColor(_mainColor);
        _monthTextLayout.setLayoutParams(textParams);
        _monthTextLayout.setBackgroundColor(_mainColor);
        _lowerMonthLayout.setLayoutParams(lowerParams);
        _lowerMonthLayout.setBackgroundColor(_mainColor);

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

        switch(day) {
            case 1: return getResources().getString(R.string.monday);
            case 2: return getResources().getString(R.string.tuesday);
            case 3: return getResources().getString(R.string.wednesday);
            case 4: return getResources().getString(R.string.thursday);
            case 5: return getResources().getString(R.string.friday);
            case 6: return getResources().getString(R.string.saturday);
            case 7: return getResources().getString(R.string.sunday);
            default: return "";
        }
    }

    private int getProperMonthFontSize(){
        return (int)(getProperFontSize() / 3);
    }

    private int getProperFontSize(){

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        switch(getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) {

            case Configuration.SCREENLAYOUT_SIZE_XLARGE : return 120;
            case Configuration.SCREENLAYOUT_SIZE_LARGE : return 90;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL : return 60;
            case Configuration.SCREENLAYOUT_SIZE_SMALL : return 30;
        }

        return height / 40;
    }

    private LinearLayout _leftHorizontalLayout;
    private LinearLayout _contentHorizontalLayout;
    private LinearLayout _rightHorizontalLayout;
    private LinearLayout _contentTopLayout;
    private LinearLayout _contentBottomLayout;
    private LinearLayout _centralLayout;
    private LinearLayout _leftSideLayout;
    private TextView _dayTextLayout;
    private TextView _monthText;
    private TextView _dayOfWeekText;
    private LinearLayout _monthLayout;
    private LinearLayout _rightSideLayout;
    private LinearLayout _upperMonthLayout;
    private LinearLayout _lowerMonthLayout;
    private LinearLayout _monthTextLayout;
    private LinearLayout _iconLayout;

    private ImageView _menstrualIcon;
    private ImageView _ovulationIcon;
    private ImageView _nonOvulationIcon;

    private int dateType = 0;

    private OnDateMeterTouchEventListener _listener;
}
