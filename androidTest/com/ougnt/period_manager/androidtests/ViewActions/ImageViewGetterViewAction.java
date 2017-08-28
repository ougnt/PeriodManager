package com.ougnt.period_manager.androidtests.ViewActions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Created by wacharint on 12/12/2016 AD.
 */
public class ImageViewGetterViewAction implements ViewAction {

    private ImageView[] ret;

    public ImageViewGetterViewAction(ImageView[] returnedObject) {
        ret = returnedObject;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isAssignableFrom(ImageView.class);
    }

    @Override
    public String getDescription() {
        return "To get view object of the ImageView";
    }

    @Override
    public void perform(UiController uiController, View view) {
        ret[0] = (ImageView) view;
    }
}
