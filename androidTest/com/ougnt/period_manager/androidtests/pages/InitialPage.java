package com.ougnt.period_manager.androidtests.pages;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.androidtests.ViewActions.ImageViewGetterViewAction;
import com.ougnt.period_manager.androidtests.ViewActions.LinearLayoutGetterViewAction;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class InitialPage {

    public void clickHamburgerMenu() {
        onView(withId(R.id.hamburger_menu_button)).perform(ViewActions.click());
    }

    public void clickDateDetailButton() {
        onView(withId(R.id.date_detail_action_button)).perform(ViewActions.click());
    }

    public void checkSelectedDateEqualToToday() {
        checkSelectedDate(DateTime.now());
    }

    public void checkSelectedDate(DateTime expectedDate) {
        assertThat("Selected Date doesn't equal to " + expectedDate.toString("yyyy-MM-dd"),
                getSelectedDate().toString("yyyy-MM-dd"),
                Matchers.equalTo(expectedDate.toString("yyyy-MM-dd")));
    }

    public List<Integer> getFingerLocation() {
        ImageView[] fingers = new ImageView[1];
        onView(withId(R.id.finger_pointer)).perform(new ImageViewGetterViewAction(fingers));

        int[] fingerLocator = new int[2];
        fingers[0].getLocationOnScreen(fingerLocator);
        ArrayList<Integer> ret = new ArrayList<>(2);
        ret.add(fingerLocator[0] + fingers[0].getWidth() / 3);
        ret.add(fingerLocator[1]);
        return ret;
    }

    public DateMeter getPointedDateMeter() {
        LinearLayout[] containers = new LinearLayout[1];
        onView(withId(R.id.dateScrollerContent)).perform(new LinearLayoutGetterViewAction(containers));

        int[] locator = new int[2];
        List<Integer> fingerLocations = getFingerLocation();
        for(int i = 0 ; i < containers[0].getChildCount(); i++) {
            containers[0].getChildAt(i).getLocationOnScreen(locator);

            if(locator[0] < fingerLocations.get(0) &&
                    locator[0] + containers[0].getChildAt(i).getWidth() > fingerLocations.get(0)) {
                return (DateMeter) containers[0].getChildAt(i);
            }
        }
        return null;
    }

    public DateTime getSelectedDate() {

        final String[] ret = new String[1];
        onView(withId(R.id.main_header_text)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Get Test From TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ret[0] = ((TextView) view).getText().toString();
            }
        });
        return DateTimeFormat.forPattern("MMM dd yyyy").parseDateTime(ret[0]);
    }
}
