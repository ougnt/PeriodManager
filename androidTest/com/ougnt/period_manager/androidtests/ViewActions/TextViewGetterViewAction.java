package com.ougnt.period_manager.androidtests.ViewActions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Created by wacharint on 12/20/2016 AD.
 */
public class TextViewGetterViewAction implements ViewAction {
    TextView[] rets;

    public TextViewGetterViewAction(TextView[] returnedTextViews) {
        rets = returnedTextViews;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isAssignableFrom(TextView.class);
    }

    @Override
    public String getDescription() {
        return "To get the TextView view object";
    }

    @Override
    public void perform(UiController uiController, View view) {
        rets[0] = (TextView) view;
    }
}
