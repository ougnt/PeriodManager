package com.ougnt.period_manager.androidtests.pages;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.androidtests.ViewActions.ImageViewGetterViewAction;
import com.ougnt.period_manager.androidtests.ViewActions.LinearLayoutGetterViewAction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DateDetailPage {

    public void clickEmotionSad() {

        ViewInteraction emotionButton = onView(withId(R.id.emotion_icon_sad));
        emotionButton.perform(ViewActions.scrollTo());
        emotionButton.perform(ViewActions.click());
    }

    public void clickHaveIntercourse() {

        ViewInteraction emotionButton = onView(withId(R.id.action_panel_intercourse));
        emotionButton.perform(ViewActions.scrollTo());
        emotionButton.perform(ViewActions.click());
    }

    public void clickSave() {
        onView(withId(R.id.action_save_button)).perform(ViewActions.click());
    }

    public void clickEmotionNothing() {
        ViewInteraction emotionButton = onView(withId(R.id.emotion_icon_nothing));
        emotionButton.perform(ViewActions.scrollTo());
        emotionButton.perform(ViewActions.click());
    }

    public void clickHaventIntercourse() {
        ViewInteraction emotionButton = onView(withId(R.id.action_panel_no_intercourse));
        emotionButton.perform(ViewActions.scrollTo());
        emotionButton.perform(ViewActions.click());
    }

    public void clickActionButton() {
        onView(withId(R.id.action_action_button_image)).perform(ViewActions.click());
    }
}
