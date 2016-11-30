package com.example.android.groupschedulecoordinator;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by Emily on 11/26/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

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
    public void testCreateGroup() throws InterruptedException {
        //When the floating action button is clicked
        onView(withId(R.id.myFab))
                .perform(click());

        Thread.sleep(1000);
        //then the new group dialog pops up and when you type Group A into the dialog
        onView(withId(R.id.userInputDialog))
                    .check(matches(isDisplayed()))
                    .perform(typeText("Group A"));

        Thread.sleep(1000);
        //and when you click on create...
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        //then a new group "Group A" in the groupList list view is displayed
        onData(hasToString(startsWith("Group A")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));
         }


    @Test
    public void testAddMember() throws InterruptedException {
        Thread.sleep(1000);
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.myFab),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        Thread.sleep(1000);
        //and when we create a new group
        onView(withId(R.id.userInputDialog))
                .perform(typeText("Group C"));

        Thread.sleep(1000);
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        //when we enter the new group
        onData(hasToString(startsWith("Group C")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //When we click on members...
        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        //then members content page appears. When we click on it...
        onView(withId(R.id.addmembers))
                .check(matches(isDisplayed()))
                .perform(click());

        //Then Dialog appears to enter member
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.tbMemberName), isDisplayed()));
        appCompatEditText.perform(click());

        //When we enter "Students" for the name of the new member
        appCompatEditText.perform(replaceText("Students"), closeSoftKeyboard());

        //and when we enter an email
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.tbMemberEmail), isDisplayed()));
        appCompatEditText4.perform(typeText("getexpensivemisery@ucsd.edu"), closeSoftKeyboard());

        //and when we click addmember
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCreateGroup), withText("Add Member"), isDisplayed()));
        appCompatButton2.perform(click());

        //and when we cut away from member content page
        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(android.R.id.title), withText("Info"), isDisplayed()));
        appCompatTextView5.perform(click());

        //when we come back to the member content page
        ViewInteraction appCompatTextView9 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView9.perform(click());

        //the new member's email is still there!
        DataInteraction v2 = onData(hasToString(startsWith("getexpensivemisery@ucsd.edu")))
                .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
        v2.check(matches(withText("getexpensivemisery@ucsd.edu")));
    }


    @Test
    public void testaddMeetingDP() throws InterruptedException {
        Thread.sleep(1000);
        //Given the floating action button, when we click on it
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.myFab),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        Thread.sleep(1000);
        //and when we create a new group
        onView(withId(R.id.userInputDialog))
                .perform(typeText("Group B"));

        Thread.sleep(1000);
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        //when we enter the new group
        onData(hasToString(startsWith("Group B")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //then we can add a meeting
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnNewMeeting), withText("Add New Meeting"),
                        withParent(allOf(withId(R.id.info),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.eventName),
                        withParent(allOf(withId(R.id.activity_create_meeting),
                                withParent(withId(R.id.scrollpanel))))));
        appCompatEditText5.perform(scrollTo(), replaceText("Meet"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.spinnerLengthHour));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("2"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.spinnerEndHour));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("9"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                withId(R.id.spinnerEndToD));
        appCompatSpinner3.perform(scrollTo(), click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("PM"), isDisplayed()));
        appCompatTextView4.perform(click());

        //then there is a date picker we can use
        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")), withContentDescription("Next month"),
                        withParent(allOf(withClassName(is("android.widget.DayPickerView")),
                                withParent(withClassName(is("com.android.internal.widget.DialogViewAnimator"))))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //when we click create meeting
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnCreateMeeting), withText("Create Meeting"),
                        withParent(allOf(withId(R.id.activity_create_meeting),
                                withParent(withId(R.id.scrollpanel))))));
        appCompatButton3.perform(scrollTo(), click());

        //then the meeting is in the list view
        ViewInteraction v = onData(hasToString(startsWith("Meet at")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v.check(matches(isDisplayed()));
    }

    @Test
    public void testBackButton() throws InterruptedException {

        //Given the floating action button, when we click on it
        Thread.sleep(1000);
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.myFab),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        Thread.sleep(1000);
        //and when we create a new group
        onView(withId(R.id.userInputDialog))
                .perform(typeText("Group F"));

        Thread.sleep(1000);
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        //when we enter the new group
        onData(hasToString(startsWith("Group F")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //and when we click on members and add a member
        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        onView((withId(R.id.addmembers)))
                .check(matches(isDisplayed()))
                .perform(click());

        //when we press back
        pressBack();

        //then we are brought back to the member page
        onView(withId(R.id.addmembers)).check(matches(isDisplayed()));

        //and when we press back again
        pressBack();

        //then we are brought back to the groups page
        onView(withId(R.id.myFab)).check(matches(isDisplayed()));
    }


    @Test
    public void testMembersfromWidget() throws InterruptedException {
        Thread.sleep(1000);
        //Given the floating action button, when we click on it
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.myFab),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        Thread.sleep(1000);
        //and when we create a new group
        onView(withId(R.id.userInputDialog))
                .perform(typeText("Group H"));

        Thread.sleep(1000);
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        //when we enter the new group
        onData(hasToString(startsWith("Group H")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //Then we see the group page.
        onView(withId(R.id.btnNewMeeting))
                .check(matches(isDisplayed()));

        //When we click on the Members widget tab
        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        //Then the addmembers button is displayed
        onView(withId(R.id.addmembers))
                .check(matches(isDisplayed()));
    }


}
