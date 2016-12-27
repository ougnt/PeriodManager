package com.ougnt.period_manager.repository;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * * # Created by wacharint on 11/12/15.
 */
public class FetchingButton extends LinearLayout {

    public static int NextId = 0;
    public static int LastId =0 ;

    public FetchingButton(Context context) {
        this(context, true);
    }

    public FetchingButton(Context context, boolean isRightEdge) {
        super(context);
        this.isRightEdge = isRightEdge;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);
        TextView innerText = new TextView(context);
        innerText.setLayoutParams(params);
        if(isRightEdge) {
            innerText.setText("  >  ");
            if(Build.VERSION.SDK_INT < 17) {
                NextId = (int) (Math.random() * 1000000);
            } else {
                NextId = generateViewId();
            }
            setId(NextId);
        } else {
            innerText.setText("  <  ");
            if(Build.VERSION.SDK_INT < 17) {
                LastId = (int) (Math.random() * 1000000);
            } else {
                LastId = generateViewId();
            }
            setId(LastId);
        }

        innerText.setTextColor(Color.BLACK);
        innerText.setGravity(Gravity.CENTER);
        this.addView(innerText);
    }

    public boolean isRightEdge;
}
