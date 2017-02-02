package com.ougnt.period_manager.androidtests.pages;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.androidtests.ViewActions.ImageViewGetterViewAction;
import com.ougnt.period_manager.androidtests.ViewActions.LinearLayoutGetterViewAction;
import com.ougnt.period_manager.androidtests.ViewActions.TextViewGetterViewAction;
import com.ougnt.period_manager.repository.FetchingButton;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
                getPointedDateMeter().getDate().toString("yyyy-MM-dd"),
                Matchers.equalTo(expectedDate.toString("yyyy-MM-dd")));
    }

    public void clickTomorrowDateMeter() {
        onView(Matchers.allOf(
                withChild(withText(DateTime.now().plusDays(1).toString("dd")))
                )).perform(ViewActions.click());
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

    public void clickConclusionToggleButton() {
        onView(withId(R.id.conclusion_view_toggle)).perform(ViewActions.click());
    }

    public void checkConclusionViewShowing() {
        onView(withId(R.id.conclusion_panel)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void clickDateViewToggleButton() {
        onView(withId(R.id.date_view_toggle)).perform(ViewActions.click());
    }

    public void checkDateViewShowing() {
        onView(withId(R.id.date_scroller)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void clickMonthViewToggleButton() {
        onView(withId(R.id.month_view_toggle)).perform(ViewActions.click());
    }

    public void checkMonthViewShowing() {
        onView(withId(R.id.month_view_panel)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void clickChartViewToggleButton() {
        onView(withId(R.id.chart_view_toggle)).perform(ViewActions.click());
    }

    public void checkChartViewToggleButton() {
        onView(withId(R.id.chart_view_panel)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void setTodayIsFirstMenstrualDate(DateDetailPage dateDetailPage) {

        if(isTodayMenstrualDate()) {
            clickDateDetailButton();
            dateDetailPage.clickActionButton();
            dateDetailPage.clickSave();
        }

        clickDateDetailButton();
        dateDetailPage.clickActionButton();
        dateDetailPage.clickSave();
        pressBack();
    }

    private boolean isTodayMenstrualDate() {
        DateMeter today = getPointedDateMeter();
        return today.dateType == DateMeter.Menstrual;
    }

    public void checkOvulationSuggestion() {
        TextView[] textViews = new TextView[1];
        onView(withId(R.id.conclusion_suggestion_date)).perform(ViewActions.scrollTo(), new TextViewGetterViewAction(textViews));
        Assert.assertEquals(String.format(
                        textViews[0].getContext().getString(R.string.conclusion_suggestion_date_string),
                        DateTime.now().plusDays(12).toString(textViews[0].getResources().getString(R.string.short_date_format)),
                        DateTime.now().plusDays(14).toString(textViews[0].getResources().getString(R.string.short_date_format))),
                textViews[0].getText().toString());
    }

    public void checkAssignedDateMetersAreCorrect() {
        LinearLayout[] containers = new LinearLayout[1];
        onView(withId(R.id.dateScrollerContent)).perform(new LinearLayoutGetterViewAction(containers));
        clickDateViewToggleButton();
        onView(withId(FetchingButton.NextId)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withId(FetchingButton.NextId)).perform(ViewActions.scrollTo(), ViewActions.click());
        Assert.assertEquals(DateMeter.Menstrual, ((DateMeter) containers[0].getChildAt(15 + 1)).dateType);
        Assert.assertEquals(DateMeter.Menstrual, ((DateMeter) containers[0].getChildAt(15 + 7)).dateType);
        Assert.assertEquals(DateMeter.PossiblyOvulation, ((DateMeter) containers[0].getChildAt(15 + 8)).dateType);
        Assert.assertEquals(DateMeter.OvulationDate, ((DateMeter) containers[0].getChildAt(15 + 14)).dateType);
        Assert.assertEquals(DateMeter.PossiblyOvulation, ((DateMeter) containers[0].getChildAt(15 + 21)).dateType);
        Assert.assertEquals(DateMeter.Nothing, ((DateMeter) containers[0].getChildAt(15 + 22)).dateType);
        Assert.assertEquals(DateMeter.Nothing, ((DateMeter) containers[0].getChildAt(15 + 27)).dateType);
        Assert.assertEquals(DateMeter.ExpectedMenstrual, ((DateMeter) containers[0].getChildAt(15 + 28)).dateType);
        Assert.assertEquals(DateMeter.ExpectedMenstrual, ((DateMeter) containers[0].getChildAt(15 + 31)).dateType);
    }
}
