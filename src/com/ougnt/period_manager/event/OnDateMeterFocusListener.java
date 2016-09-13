package com.ougnt.period_manager.event;

import android.content.Context;
import android.view.View;

import com.ougnt.period_manager.DateMeter;

import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 10/31/15.
 */
public interface OnDateMeterFocusListener {

    void onFocusMoveIn(DateMeter focusDate);

}
