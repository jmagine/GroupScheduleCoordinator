package com.example.android.groupschedulecoordinator;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by Emily on 11/30/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MultiGroupMeetingTest {
    private static boolean set_Up = false;
    private static boolean m1 = false;
    private static boolean m2 = false;

    @Before
    public void setUp() throws InterruptedException {

        if (!set_Up) {
            //Given that you're in MainActivity (already signed in) and that you have a precreated
            //group called "newGroup"
            //click on "plus button"
            Thread.sleep(1000);
            onView(withId(R.id.myFab))
                    .perform(click());

            Thread.sleep(1000);
            onView(withId(R.id.userInputDialog))
                    .perform(typeText("A Group"));

            Thread.sleep(1000);
            //click create
            ViewInteraction appCompatButton = onView(
                    allOf(withId(android.R.id.button1), withText("Create"),
                            withParent(allOf(withId(R.id.buttonPanel),
                                    withParent(withId(R.id.parentPanel)))),
                            isDisplayed()));
            appCompatButton.perform(click());

            Thread.sleep(1000);
            onView(withId(R.id.myFab))
                    .perform(click());

            Thread.sleep(1000);
            onView(withId(R.id.userInputDialog))
                    .perform(typeText("B Group"));

            Thread.sleep(1000);
            //click create
            ViewInteraction ai = onView(
                    allOf(withId(android.R.id.button1), withText("Create"),
                            withParent(allOf(withId(R.id.buttonPanel),
                                    withParent(withId(R.id.parentPanel)))),
                            isDisplayed()));
            ai.perform(click());

            Thread.sleep(1000);
            onView(withId(R.id.myFab))
                    .perform(click());

            Thread.sleep(1000);
            onView(withId(R.id.userInputDialog))
                    .perform(typeText("C Group"));

            Thread.sleep(1000);
            //click create
            ViewInteraction ap = onView(
                    allOf(withId(android.R.id.button1), withText("Create"),
                            withParent(allOf(withId(R.id.buttonPanel),
                                    withParent(withId(R.id.parentPanel)))),
                            isDisplayed()));
            ap.perform(click());
        }
        set_Up = true;
    }

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void verifyGroups(){
        onData(hasToString(startsWith("A Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));

        onData(hasToString(startsWith("B Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));

        onData(hasToString(startsWith("C Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));
    }



}
