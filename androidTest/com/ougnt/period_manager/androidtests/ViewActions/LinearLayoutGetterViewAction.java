package com.ougnt.period_manager.androidtests.ViewActions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.LinearLayout;

import com.ougnt.period_manager.DateMeter;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Created by wacharint on 12/13/2016 AD.
 */
public class LinearLayoutGetterViewAction implements ViewAction {

    LinearLayout[] rets;

    public LinearLayoutGetterViewAction(LinearLayout[] returnedLinearLayouts) {
        rets = returnedLinearLayouts;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isAssignableFrom(LinearLayout.class);
    }

    @Override
    public String getDescription() {
        return "To get the DateMeter view object";
    }

    @Override
    public void perform(UiController uiController, View view) {
        rets[0] = (LinearLayout) view;
    }
}
