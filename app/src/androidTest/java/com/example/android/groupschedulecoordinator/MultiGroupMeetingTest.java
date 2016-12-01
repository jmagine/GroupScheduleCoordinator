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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MultiGroupMeetingTest {
    private static boolean set_Up = false;
    private static boolean m1 = false;
    private static boolean m2 = false;

    @Before
    public void setUp() throws InterruptedException {

        if (!set_Up ) {

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
    public void verifyGroups() throws InterruptedException {
        //verify that all the groups were created are in the group list
        Thread.sleep(1000);
        onData(hasToString(startsWith("A Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));

        Thread.sleep(1000);
        onData(hasToString(startsWith("B Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));

        Thread.sleep(1000);
        onData(hasToString(startsWith("C Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void failedAddMeeting() throws InterruptedException {
        //when clicking on C Group
        Thread.sleep(1000);
        onData(hasToString(startsWith("C Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()))
                .perform(click());

        //Then we see the group content page (button new meeting exists)
        //When we click on new meeting...
        onView(withId(R.id.btnNewMeeting))
                .check(matches(isDisplayed()))
                .perform(click());

        //and When we type in an event name
        onView(withId(R.id.eventName))
                .perform(typeText("Woohoo"));

        Thread.sleep(1000);
        //and when we click create meeting

        onView(withId(R.id.scrollpanel)).perform(swipeUp());
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        onView(withId(R.id.scrollpanel)).perform(swipeDown());
        //Then nothing happens because the meeting doesn't have valid time
        //(Then we are still on the same page as before)
        onView(withId(R.id.spinnerLengthMinute)).check(matches(isDisplayed()));

        onView(withId(R.id.spinnerLengthMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.scrollpanel)).perform(swipeUp());

        Thread.sleep(1000);
        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        onView(withId(R.id.scrollpanel)).perform(swipeDown());

        //still cant create a meeting because free times not specified
        onView(withId(R.id.spinnerLengthMinute)).check(matches(isDisplayed()));

        onView(withId(R.id.scrollpanel)).perform(swipeUp());

        onView(withId(R.id.spinnerBeginHour)).perform(scrollTo(), click());

        //when we actually put in valid info
        onView(allOf(withId(android.R.id.text1), withText("6"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndHour)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("6"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndToD)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("PM"), isDisplayed())).perform(click());

        onView(withId(R.id.scrollpanel)).perform(swipeUp());

        Thread.sleep(1000);
        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        //then we were finally able to create a meeting
        ViewInteraction v = onData(hasToString(startsWith("Woohoo")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v.check(matches(isDisplayed()));

    }


    @Test
    public void add3Meetings() throws InterruptedException {
        Thread.sleep(1000);
        onData(hasToString(startsWith("B Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()))
                .perform(click());

        //Then we see the group content page (button new meeting exists)
        //When we click on new meeting...
        onView(withId(R.id.btnNewMeeting))
                .check(matches(isDisplayed()))
                .perform(click());

        //and When we type in an event name
        onView(withId(R.id.eventName))
                .perform(typeText("Meeting 1"));

        //and when we choose desired duration and free times
        onView(withId(R.id.spinnerLengthMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerBeginHour)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("7"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndHour)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("2"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndToD)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("PM"), isDisplayed())).perform(click());

        onView(withId(R.id.scrollpanel)).perform(swipeUp());

        Thread.sleep(1000);

        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        //then we are taken back to the meeting page, and when we click on the new meeting button
        onView(withId(R.id.btnNewMeeting))
                .check(matches(isDisplayed()))
                .perform(click());

        //and When we type in an event name
        onView(withId(R.id.eventName))
                .perform(typeText("Meeting 2"));

        //and when we create more meetings
        onView(withId(R.id.spinnerLengthMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerBeginHour)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("8"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndHour)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("2"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndToD)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("PM"), isDisplayed())).perform(click());

        onView(withId(R.id.scrollpanel)).perform(swipeUp());

        Thread.sleep(1000);
        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        //and when we create another meeting
        onView(withId(R.id.btnNewMeeting))
                .check(matches(isDisplayed()))
                .perform(click());

        //and When we type in an event name
        onView(withId(R.id.eventName))
                .perform(typeText("Meeting 3"));

        onView(withId(R.id.spinnerLengthMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerBeginHour)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("9"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndHour)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("2"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndMinute)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("30"), isDisplayed())).perform(click());

        onView(withId(R.id.spinnerEndToD)).perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.text1), withText("PM"), isDisplayed())).perform(click());

        onView(withId(R.id.scrollpanel)).perform(swipeUp());

        Thread.sleep(1000);
        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        //then all three meetings are present in the list
        ViewInteraction v = onData(hasToString(startsWith("Meeting 3")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v.check(matches(isDisplayed()));

        ViewInteraction v2 = onData(hasToString(startsWith("Meeting 2")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v.check(matches(isDisplayed()));

        ViewInteraction v1 = onData(hasToString(startsWith("Meeting 1")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v1.check(matches(isDisplayed()));
    }

}
