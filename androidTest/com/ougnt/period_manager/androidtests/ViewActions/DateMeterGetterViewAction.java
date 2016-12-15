package com.ougnt.period_manager.androidtests.ViewActions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.ImageView;

import com.ougnt.period_manager.DateMeter;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Created by wacharint on 12/12/2016 AD.
 */
public class DateMeterGetterViewAction implements ViewAction {

    DateMeter[] datemeters;

    public DateMeterGetterViewAction(DateMeter[] returnedDateMeter) {
        datemeters = returnedDateMeter;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isAssignableFrom(DateMeter.class);
    }

    @Override
    public String getDescription() {
        return "To get the DateMeter view object";
    }

    @Override
    public void perform(UiController uiController, View view) {
        datemeters[0] = (DateMeter) view;
    }
}
