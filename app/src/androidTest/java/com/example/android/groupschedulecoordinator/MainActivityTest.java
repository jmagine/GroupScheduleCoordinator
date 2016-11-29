package com.example.android.groupschedulecoordinator;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Emily on 11/26/2016.
 */

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        //click on "plus button"
        onView(withId(R.id.myFab))
                .perform(click());

        //find text box and type group1
        onView(withId(R.id.userInputDialog))
                .perform(typeText("group1"));

        //click create
        onView(withText("Create"))
                .perform(click());

        //assert that group shows up in list
        withText(containsString("group1")).matches(onView(withId(R.id.groupName)));
    }

    @Test
    public void clickgroup() {
        onData(allOf(is(instanceOf(String.class)), is("group1")))
                .inAdapterView(withId(R.id.groupList))
                .perform(click());
    }

    @Test
    public void addRandomMeeting() {
        //add meeting with default settings, no use of ActivityCreateMeeting
        onData(allOf(is(instanceOf(String.class)), is("group1")))
                .inAdapterView(withId(R.id.groupList))
                .perform(click());
        onView(withId(R.id.btnNewMeeting))
                .perform(click());
        onView(withId(R.id.eventName))
                .perform(typeText("HW 4 Meeting"));
        onView(withText(R.id.btnCreateMeeting))
                .perform(click());

        //new meeting shows up on the list.
        withText(containsString("HW 4 Meeting")).matches(onView(withId(R.id.lvMeetings)));
    }

    /*@Test
    public void addSpecificMeeting() {
        //meeting choosing dates and time
        onData(allOf(is(instanceOf(String.class)), is("group1")))
                .inAdapterView(withId(R.id.groupName))
                .perform(click());
        onView(withText("ADD NEW MEETING"))
                .perform(click());
        onView(withId(R.id.textView2))
                .perform(typeText("HW 4 Meeting"));

        withText(containsString("HW 4 Meeting")).matches(onView(withId(R.id.lvMeetings)));
    }*/

}
