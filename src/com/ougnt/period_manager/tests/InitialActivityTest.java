package com.ougnt.period_manager.tests;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.LinearLayout;

import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.repository.DateRepository;

import junit.framework.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;


/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.ougnt.period_manager.activity.InitialActivityTest \
 * com.ougnt.period_manager.tests/android.test.InstrumentationTestRunner
 */
public class InitialActivityTest extends ActivityInstrumentationTestCase2<InitialActivity> {

    public InitialActivityTest() {
        super(InitialActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSetDeviceIdWillNotGenerateNewIdIfExist() {

        // Setup
        InitialActivity testActivity = getActivity();

        try {

            Method getDeviceIdMethod = InitialActivity.class.getDeclaredMethod("getDeviceId");
            Method setDeviceIdMethod = InitialActivity.class.getDeclaredMethod("setDeviceId");

            getDeviceIdMethod.setAccessible(true);
            setDeviceIdMethod.setAccessible(true);

            setDeviceIdMethod.invoke(testActivity);
            UUID oldUuid = (UUID) getDeviceIdMethod.invoke(testActivity);

            // Execute
            setDeviceIdMethod.invoke(testActivity);

            // Verify
            Assert.assertEquals(oldUuid.toString(), getDeviceIdMethod.invoke(testActivity).toString());

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Assert.fail(e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSetDeviceIdWillGenerateNewIdIfNotExist() {

        // Setup
        InitialActivity testActivity = getActivity();

        try {

            SharedPreferences pref = testActivity.getSharedPreferences(InitialActivity.PName, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.remove(InitialActivity.PUuid);
            edit.apply();

            Method getDeviceIdMethod = InitialActivity.class.getDeclaredMethod("getDeviceId");
            Method setDeviceIdMethod = InitialActivity.class.getDeclaredMethod("setDeviceId");

            getDeviceIdMethod.setAccessible(true);
            setDeviceIdMethod.setAccessible(true);

            // Execute
            setDeviceIdMethod.invoke(testActivity);

            // Verify
            Assert.assertNotNull(getDeviceIdMethod.invoke(testActivity).toString());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsBeforeTheFirstDateMeterMinusTwo() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(1));
        DateRepository date = DateRepository.getDateRepositories(this.getActivity().getBaseContext(), firstDateMeterDate.getDate().minusDays(2), firstDateMeterDate.getDate().minusDays(2)).get(0);

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, new DateMeter(this.getActivity().getBaseContext(), date, null));
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsBeforeTheFirstDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(true, actualResult);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsBeforeTheFirstDateMeterMinusOne() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(1));
        DateRepository date = DateRepository.getDateRepositories(this.getActivity().getBaseContext(), firstDateMeterDate.getDate().minusDays(1), firstDateMeterDate.getDate().minusDays(1)).get(0);

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, new DateMeter(this.getActivity().getBaseContext(), date, null));
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsBeforeTheFirstDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(true, actualResult);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsBeforeTheFirstDateMeter() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(1));

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, firstDateMeterDate);
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsBeforeTheFirstDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(false, actualResult);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsBeforeTheFirstDateMeterPlusOne() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(1));
        DateRepository date = DateRepository.getDateRepositories(this.getActivity().getBaseContext(), firstDateMeterDate.getDate().plusDays(1), firstDateMeterDate.getDate().plusDays(1)).get(0);

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, new DateMeter(this.getActivity().getBaseContext(), date, null));
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsBeforeTheFirstDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(false, actualResult);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsAfterTheLastDateMeterPlusTwo() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(containingLayout.getChildCount() - 2));
        DateRepository date = DateRepository.getDateRepositories(this.getActivity().getBaseContext(), firstDateMeterDate.getDate().plusDays(2), firstDateMeterDate.getDate().plusDays(2)).get(0);

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, new DateMeter(this.getActivity().getBaseContext(), date, null));
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsAfterTheLastDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(true, actualResult);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsAfterTheLastDateMeterPlusOne() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(containingLayout.getChildCount() - 2));
        DateRepository date = DateRepository.getDateRepositories(this.getActivity().getBaseContext(), firstDateMeterDate.getDate().plusDays(1), firstDateMeterDate.getDate().plusDays(1)).get(0);

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, new DateMeter(this.getActivity().getBaseContext(), date, null));
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsAfterTheLastDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(true, actualResult);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsAfterTheLastDateMeter() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(containingLayout.getChildCount() - 2));

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, firstDateMeterDate);
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsAfterTheLastDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(false, actualResult);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SmallTest
    public void testSelectedDateIsAfterTheLastDateMeterMinusOne() {

        // Setup
        InitialActivity activity = getActivity();
        LinearLayout containingLayout = (LinearLayout) activity.findViewById(R.id.dateScrollerContent);
        DateMeter firstDateMeterDate = ((DateMeter) containingLayout.getChildAt(containingLayout.getChildCount() - 2));
        DateRepository date = DateRepository.getDateRepositories(this.getActivity().getBaseContext(), firstDateMeterDate.getDate().minusDays(1), firstDateMeterDate.getDate().minusDays(1)).get(0);

        try {
            Field selectedDateField = activity.getClass().getDeclaredField("selectedDate");
            selectedDateField.setAccessible(true);
            selectedDateField.set(activity, new DateMeter(this.getActivity().getBaseContext(), date, null));
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Execute
        boolean actualResult;
        try {
            Method methodToBeTested = activity.getClass().getDeclaredMethod("selectedDateIsAfterTheLastDateMeter", LinearLayout.class);
            methodToBeTested.setAccessible(true);
            actualResult = (boolean) methodToBeTested.invoke(activity, containingLayout);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Assert.fail(e.getMessage());
            return;
        }

        // Verify
        Assert.assertEquals(false, actualResult);
    }
}
