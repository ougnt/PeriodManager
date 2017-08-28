package com.ougnt.period_manager.androidtests.pages;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import com.ougnt.period_manager.R;

import org.hamcrest.Matchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by wacharint on 12/12/2016 AD.
 */
public class MenuPage {

    public void checkWhetherMenuPageDisplayed() {
        onView(withId(R.id.menu_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void changeLanguageToEnglish() {
        onView(withId(R.id.setting_language_selecter)).perform(ViewActions.click());
        onView(withId(R.id.flag_english)).perform(ViewActions.click());
    }
}
