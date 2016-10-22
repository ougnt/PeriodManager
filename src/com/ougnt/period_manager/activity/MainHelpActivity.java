package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.handler.HttpHelper;

public class MainHelpActivity extends Activity {

    @Override
    public void onCreate(Bundle b) {
        try {
            super.onCreate(b);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.main_help);

            getAllViews();
            setAllViewVisibility(View.GONE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    do {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        optimalLength = ((View) menstrualIcon.getParent()).getWidth() / 10;
                    } while (optimalLength == 0);

                    assignSquareParamsToView();
                    assignRectangleParamToView();
                    setAllViewVisibility(View.VISIBLE);
                }
            }, 50);

        } catch (Exception e) {
            HttpHelper.sendErrorLog(e);
        }
    }

    private void assignSquareParamsToView() {
        params = new LinearLayout.LayoutParams(optimalLength, optimalLength);
        params.setMargins(10, 10, 10, 10);
        menstrualIcon.setLayoutParams(params);
        menstrualIcon.setLayoutParams(params);
        nonOvulationIcon.setLayoutParams(params);
        ovulationIcon.setLayoutParams(params);
        intercourseIcon.setLayoutParams(params);
        pencilIcon.setLayoutParams(params);
        emotionNormalIcon.setLayoutParams(params);
        emotionHappyIcon.setLayoutParams(params);
        emotionSadIcon.setLayoutParams(params);
        emotionAngryIcon.setLayoutParams(params);
    }

    private void assignRectangleParamToView() {
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, optimalLength);
        params.setMargins(10, 10, 10, 10);
        menstrualText.setLayoutParams(params);
        nonOvulationText.setLayoutParams(params);
        ovulationText.setLayoutParams(params);
        intercourseText.setLayoutParams(params);
        pencilText.setLayoutParams(params);
        emotionNormalText.setLayoutParams(params);
        emotionHappyText.setLayoutParams(params);
        emotionSadText.setLayoutParams(params);
        emotionAngryText.setLayoutParams(params);
    }

    private void setAllViewVisibility(int visibility) {
        menstrualIcon.setVisibility(visibility);
        nonOvulationIcon.setVisibility(visibility);
        ovulationIcon.setVisibility(visibility);
        intercourseIcon.setVisibility(visibility);
        pencilIcon.setVisibility(visibility);
        menstrualText.setVisibility(visibility);
        nonOvulationText.setVisibility(visibility);
        ovulationText.setVisibility(visibility);
        intercourseText.setVisibility(visibility);
        pencilText.setVisibility(visibility);
        closeButton.setVisibility(visibility);
        emotionNormalIcon.setVisibility(visibility);
        emotionHappyIcon.setVisibility(visibility);
        emotionSadIcon.setVisibility(visibility);
        emotionAngryIcon.setVisibility(visibility);
        emotionNormalText.setVisibility(visibility);
        emotionHappyText.setVisibility(visibility);
        emotionSadText.setVisibility(visibility);
        emotionAngryText.setVisibility(visibility);
    }

    private void getAllViews() {
        menstrualIcon = (ImageView) findViewById(R.id.main_help_menstrual_icon);
        nonOvulationIcon = (ImageView) findViewById(R.id.main_help_non_ovulation_icon);
        ovulationIcon = (ImageView) findViewById(R.id.main_help_ovulation_icon);
        intercourseIcon = (ImageView) findViewById(R.id.main_help_intercourse_icon);
        pencilIcon = (ImageView) findViewById(R.id.main_help_pencil_icon);
        emotionNormalIcon = (ImageView) findViewById(R.id.main_help_emotion_nothing_icon);
        emotionHappyIcon = (ImageView) findViewById(R.id.main_help_emotion_happy_icon);
        emotionSadIcon = (ImageView) findViewById(R.id.main_help_emotion_sad_icon);
        emotionAngryIcon = (ImageView) findViewById(R.id.main_help_emotion_angry_icon);

        menstrualText = (TextView) findViewById(R.id.main_help_menstrual_text);
        nonOvulationText = (TextView) findViewById(R.id.main_help_non_ovulation_text);
        ovulationText = (TextView) findViewById(R.id.main_help_ovulation_text);
        intercourseText = (TextView) findViewById(R.id.main_help_intercourse_text);
        pencilText = (TextView) findViewById(R.id.main_help_pencil_text);
        closeButton = (ImageButton) findViewById(R.id.main_help_close_button);
        emotionNormalText = (TextView) findViewById(R.id.main_help_emotion_nothing_text);
        emotionHappyText = (TextView) findViewById(R.id.main_help_emotion_happy_text);
        emotionSadText = (TextView) findViewById(R.id.main_help_emotion_sad_text);
        emotionAngryText = (TextView) findViewById(R.id.main_help_emotion_angry_text);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int optimalLength;
    private LinearLayout.LayoutParams params;

    private ImageView menstrualIcon;
    private ImageView nonOvulationIcon;
    private ImageView ovulationIcon;
    private ImageView intercourseIcon;
    private ImageView pencilIcon;
    private ImageView emotionNormalIcon;
    private ImageView emotionHappyIcon;
    private ImageView emotionSadIcon;
    private ImageView emotionAngryIcon;

    private TextView menstrualText;
    private TextView nonOvulationText;
    private TextView ovulationText;
    private TextView intercourseText;
    private TextView pencilText;
    private TextView emotionNormalText;
    private TextView emotionHappyText;
    private TextView emotionSadText;
    private TextView emotionAngryText;

    private ImageButton closeButton;
}
