package com.ougnt.period_manager.androidtests.ViewActions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class ImageButtonGetterViewAction implements ViewAction {

    private ImageButton[] ret;

    public ImageButtonGetterViewAction(ImageButton[] returnedObject) {
        ret = returnedObject;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isAssignableFrom(ImageView.class);
    }

    @Override
    public String getDescription() {
        return "To get view object of the ImageButton";
    }

    @Override
    public void perform(UiController uiController, View view) {
        ret[0] = (ImageButton) view;
    }
}

