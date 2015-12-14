package com.ougnt.period_manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.ougnt.period_manager.event.OnDateMeterTouchEventListener;
import com.ougnt.period_manager.repository.DatabaseRepositoryHelper;
import com.ougnt.period_manager.repository.DateRepository;
import com.ougnt.period_manager.repository.FetchingButton;
import com.ougnt.period_manager.repository.HelpIndicatorRepository;
import org.joda.time.DateTime;

import java.util.List;

public class InitialActivity extends Activity {

    final int EditComment = 1;
    final int DisplayHelp = 2;
    final int DisplayMenu = 4;

    public InitialActivity(){
        dateTouchListener = null;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {

                scrollView.scrollTo(dateMeterLayout.getChildAt(1).getWidth() * 15, 0);
                DateMeter today = (DateMeter) dateMeterLayout.getChildAt(16);
                today.onTouchFormat();

                int indicatorValue = HelpIndicatorRepository.getIndicator(getBaseContext());

                if((indicatorValue & 1) == 1) {

                    Intent helpIntent = new Intent(getBaseContext(), HelpActivity.class);
                    helpIntent.putExtra("INDICATOR", indicatorValue);
                    startActivityForResult(helpIntent, DisplayHelp);
                }
            }
        }, 250);
    }

    public void initialHelpActivity(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra("INDICATOR", HelpIndicatorRepository.getIndicator(getBaseContext()));
        startActivityForResult(intent, DisplayHelp);
    }

    public void hamburgerMenuClick(View view) {

        Intent intent = new Intent(this, MenuActivity.class);
        startActivityForResult(intent, DisplayMenu);
    }

    public void commentSave(View view) {
        // save
        if(selectedDate == null) {return;}
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

            ((DateMeter)(v.getChildAt(index))).changeColor(DateMeter.MenstrualColor, DateMeter.Menstrual);
            DateTime dateToBePainted = ((DateMeter)(v.getChildAt(index))).getDate();
            paintDateMeter(dateToBePainted, dateToBePainted.plusDays(6), DateMeter.Menstrual);
            paintDateMeter(dateToBePainted.plusDays(7), dateToBePainted.plusDays(20), DateMeter.Ovulation);
            paintDateMeter(dateToBePainted.plusDays(21), dateToBePainted.plusDays(27), DateMeter.Nothing);
            paintDateMeter(dateToBePainted.plusDays(28), dateToBePainted.plusDays(28), DateMeter.Menstrual);
            newType = DateMeter.Menstrual;
        } else if(srcView.getId() == R.id.makeSafeZoneButton) {

            ((DateMeter)(v.getChildAt(index))).changeColor(DateMeter.SafeZoneColor, DateMeter.Nothing);
            newType = DateMeter.Nothing;
        } else {return;}

        DateRepository.updateDateRepository(this, ((DateMeter)(v.getChildAt(index))).getDate(), newType);

    }

    public void setOnDateMeterTouchEventListener(OnDateMeterTouchEventListener listener) {
        dateTouchListener = listener;
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
                int selectedMenu = data.getIntExtra(MenuActivity.SelectedMenuExtra, 0);
                switch (selectedMenu) {
                    case MenuActivity.SelectDisplayHelp : {
                        initialHelpActivity(null);
                        break;
                    }
                }
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
                    DateMeter lastDateMeter = (DateMeter) callbackLayout.getChildAt(callbackLayout.getChildCount() - 2);
                    callbackLayout.removeViewAt(callbackLayout.getChildCount() - 1);
                    addDateMeter(callbackLayout, lastDateMeter.getDate().plusDays(1), lastDateMeter.getDate().plusDays(15), true);
                    callbackLayout.addView(generateEndLayout(callbackLayout));
                } else {
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
