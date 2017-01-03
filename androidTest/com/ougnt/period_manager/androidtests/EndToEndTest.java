package com.ougnt.period_manager.androidtests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.activity.helper.FlagHelper;
import com.ougnt.period_manager.androidtests.pages.DateDetailPage;
import com.ougnt.period_manager.androidtests.pages.InitialPage;
import com.ougnt.period_manager.androidtests.pages.MenuPage;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class EndToEndTest {

    @Rule
    public ActivityTestRule<InitialActivity> mActivityRule = new ActivityTestRule<>(InitialActivity.class);

    private InitialPage initialPage;
    private DateDetailPage dateDetailPage;
    private MenuPage menuPage;

    @Before
    public void setUp() {

        initialPage = new InitialPage();
        dateDetailPage = new DateDetailPage();
        menuPage = new MenuPage();

        initialPage.clickHamburgerMenu();
        menuPage.changeLanguageToEnglish();
    }

    @Test
    public void ensureTheHamburgerMenuClickedWillShowMenu() {
        // Type text and then press the button.
        initialPage.clickHamburgerMenu();

        // Check that the text was changed.
        menuPage.checkWhetherMenuPageDisplayed();
    }

    @Test
    public void ensureTheDateDetailsIsWorking() {

        // Check the initial page first
        initialPage.clickDateViewToggleButton();
        initialPage.checkSelectedDateEqualToToday();
        DateMeter pointingDateMeter = initialPage.getPointedDateMeter();

        Assert.assertEquals(DateTime.now().toString("yyyy-MM-dd"), pointingDateMeter.getDate().toString("yyyy-MM-dd"));
        initialPage.clickDateDetailButton();

        dateDetailPage.clickEmotionSad();
        dateDetailPage.clickHaveIntercourse();
        dateDetailPage.clickSave();

        Assert.assertEquals(DateTime.now().toString("yyyy-MM-dd"), pointingDateMeter.getDate().toString("yyyy-MM-dd"));
        Assert.assertEquals(FlagHelper.EmotionSadIcon, FlagHelper.GetEmotionFlag(pointingDateMeter.getFlags()) & FlagHelper.EmotionSadIcon);
        Assert.assertEquals(FlagHelper.HaveIntercourseFlag, FlagHelper.GetIntercourseFlag(pointingDateMeter.getFlags()));

        initialPage.clickDateDetailButton();
        dateDetailPage.clickEmotionNothing();
        dateDetailPage.clickHaventIntercourse();
        dateDetailPage.clickSave();

        pointingDateMeter = initialPage.getPointedDateMeter();
        Assert.assertEquals(DateTime.now().toString("yyyy-MM-dd"), pointingDateMeter.getDate().toString("yyyy-MM-dd"));
        Assert.assertEquals(FlagHelper.EmotionNothingIcon, FlagHelper.GetEmotionFlag(pointingDateMeter.getFlags()) & FlagHelper.EmotionSadIcon);
        Assert.assertEquals(FlagHelper.HaventIntercourseFlag, FlagHelper.GetIntercourseFlag(pointingDateMeter.getFlags()));

        initialPage.clickDateDetailButton();
        dateDetailPage.clickEmotionStressful();
        dateDetailPage.clickSave();

        Assert.assertEquals(FlagHelper.EmotionStressfulIcon, FlagHelper.GetEmotionFlag(pointingDateMeter.getFlags()) & FlagHelper.EmotionStressfulIcon);
    }

    @Test
    public void ensureTheConclusionShowingTheRightInfo() {
        initialPage.clickDateViewToggleButton();
        initialPage.setTodayIsFirstMenstrualDate(dateDetailPage);
        initialPage.clickConclusionToggleButton();
        initialPage.checkOvulationSuggestion();
//        initialPage.checkMenstrualSuggestion();
        initialPage.checkAssignedDateMetersAreCorrect();
    }

    @Test
    public void ensureTheViewToggleIsWorking() {
        initialPage.clickConclusionToggleButton();
        initialPage.checkConclusionViewShowing();
        initialPage.clickDateViewToggleButton();
        initialPage.checkDateViewShowing();
        initialPage.clickMonthViewToggleButton();
        initialPage.checkMonthViewShowing();
        initialPage.clickChartViewToggleButton();
        initialPage.checkChartViewToggleButton();
        initialPage.clickConclusionToggleButton();
        initialPage.checkConclusionViewShowing();
    }

    @After
    public void tearDown() {  }
}
