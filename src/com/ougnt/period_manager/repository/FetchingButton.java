package com.ougnt.period_manager.repository;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * * # Created by wacharint on 11/12/15.
 */
public class FetchingButton extends LinearLayout {

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
        } else {
            innerText.setText("  <  ");
        }

        innerText.setTextColor(Color.BLACK);
        innerText.setGravity(Gravity.CENTER);
        this.addView(innerText);
    }

    public boolean isRightEdge;
}
