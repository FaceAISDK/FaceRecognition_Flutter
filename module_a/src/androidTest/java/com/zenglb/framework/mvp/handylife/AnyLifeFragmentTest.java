package com.zenglb.framework.mvp.handylife;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.zenglb.framework.EspressoIdlingResource;
import com.zenglb.framework.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.Espresso.onView;
import static org.hamcrest.CoreMatchers.is;


/**
 * MVP 中View 层的测试我一般都是不写的，所见即所得，测试工程师写的测试用例也能很容易的测试出View 的问题
 *
 * 平衡写用例的时间和收益，所以基本不写测试用例
 *
 * Tests for the Handy life screen, the main screen which contains a list of specify type data.
 *
 * Created by zlb on 2018/3/24.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AnyLifeFragmentTest {
    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<AnyLifeActivity> mTasksActivityTestRule =
            new ActivityTestRule<AnyLifeActivity>(AnyLifeActivity.class) {
                /**
                 * do something before each test.
                 */
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    // Doing this in @Before generates a race condition.

                }
            };
    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests
     * significantly more reliable.
     */
    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }


    /**
     * 点击tab layout 切换
     */
    @Test
    public void clickTabsToChangeScreen() throws Exception {
        // Click on the add task button
        onView(withId(R.id.layout_tab)).perform(click());
        onView(withId(R.id.layout_tab)).check(matches(isFocusable()));

        onView(withText("SHOP")).perform(click());
        onView(withText("SHOP")).check(matches(isSelected()));

        onView(withText("EAT")).perform(click());
        onView(withText("EAT")).check(matches(isSelected()));

        onView(withText("CITY GUIDE")).perform(click());
        onView(withText("CITY GUIDE")).check(matches(isSelected()));
    }


    /**
     * 滑动page adapter 切换，测试是否有联动
     *
     */
    @Test
    public void swipeToChangeBetweenTabs() throws Exception {
        onView(withText("CITY GUIDE")).perform(click());
        onView(withText("CITY GUIDE")).check(matches(isSelected()));

        onView(withId(R.id.container)).perform(ViewActions.swipeLeft());
        onView(withText("SHOP")).check(matches(isSelected()));

        onView(withId(R.id.container)).perform(ViewActions.swipeLeft());
        onView(withText("EAT")).check(matches(isSelected()));

        onView(withId(R.id.container)).perform(ViewActions.swipeRight());
        onView(withText("SHOP")).check(matches(isSelected()));

        onView(withId(R.id.container)).perform(ViewActions.swipeRight());
        onView(withText("CITY GUIDE")).check(matches(isSelected()));
    }

    /**
     * 滑动page adapter 切换
     *
     */
    @Test
    public void scrollVerticallyDown()throws Exception {
        onView(withId(R.id.layout_tab)).perform(click());
        onView(withText("CITY GUIDE")).perform(click());

        onView(withTagValue(is("tagcity_guide"))).perform(ViewActions.swipeDown());
        //
        onView(withTagValue(is("tagcity_guide"))).check(matches(isDisplayed()));
    }


}