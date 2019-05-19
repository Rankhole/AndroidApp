package com.example.androidapp;

import android.app.Instrumentation;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.androidapp.bt.ConnectActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Test class for MainActivity
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;

    //ConnectActivity Monitor
    //Instrumentation.ActivityMonitor monitorConnectActivity = getInstrumentation().addMonitor(ConnectActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){

        //Views are existing/visible
        //singleplayer button
        assertNotNull(mActivity.findViewById(R.id.button3));
        onView(withId(R.id.button3)).check(matches(isDisplayed()));

        //multiplayer button
        assertNotNull(mActivity.findViewById(R.id.button2));
        onView(withId(R.id.button2)).check(matches(isDisplayed()));

        //settings button
        assertNotNull(mActivity.findViewById(R.id.button4));
        onView(withId(R.id.button4)).check(matches(isDisplayed()));

    }

    @Test
    //TODO
    public void testLaunchOfConnectActivityOnButtonClickMultiplayer() {
        assertNotNull(mActivity.findViewById(R.id.button2));

        //opens ConnectActivity
        //onView(withId(R.id.button2)).perform(click());  //buggy wegen bluetooth

        //assertNotNull(getInstrumentation().waitForMonitorWithTimeout(monitorConnectActivity,5000));

        //check if ConnectActivity opened and Views are visible
        //onView(withId(R.id.buttonPairNewDevice)).check(matches(isDisplayed());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}