package com.example.android.groupschedulecoordinator;

import android.support.test.espresso.DataInteraction;
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

    @Before
    public void setUp(){
            //click on "plus button"
            onView(withId(R.id.myFab))
                    .perform(click());

            onView(withId(R.id.userInputDialog))
                    .perform(typeText("newGroup"));

            //click create
            ViewInteraction appCompatButton = onView(
                    allOf(withId(android.R.id.button1), withText("Create"),
                            withParent(allOf(withId(R.id.buttonPanel),
                                    withParent(withId(R.id.parentPanel)))),
                            isDisplayed()));
            appCompatButton.perform(click());
    }

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
    //Given that we create a group called group 1
    public void testCreateGroup() {
        //click on "plus button"
        onView(withId(R.id.myFab))
                .perform(click());

        onView(withId(R.id.userInputDialog))
                    .perform(typeText("Group A"));

        /*ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userInputDialog),
                        withParent(allOf(withId(R.id.custom_dialog_layout_design_user_input),
                                withParent(withId(R.id.custom)))),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Group A"), closeSoftKeyboard());*/

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        onData(hasToString(startsWith("Group A")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .check(matches(isDisplayed()));
         }


    @Test
    public void
    testaddRandomMeeting() {

        //...When we click on the group
        onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //Then we see the group page
        //When we click on new meeting...
        onView(withId(R.id.btnNewMeeting))
                .perform(click());
        //and When we type in an event name
        onView(withId(R.id.eventName))
                .perform(typeText("HW 4 Meeting"));


        //and When we choose a length for the meeting
        onView(withId(R.id.spinnerLengthHour)).perform(click());
        onData(allOf(is(instanceOf(Integer.class)), is(2))).perform(click());
        //Then the length of the meeting is correctly reflected.
        //onView(withId(R.id.spinnerLengthHour)).check(matches(withSpinnerText(containsString("2"))));

        //When we choose the time when we begin to be free
        onView(withId(R.id.spinnerBeginHour)).perform(click());
        onData(allOf(is(instanceOf(Integer.class)), is(9))).perform(click());

        //and when we choose the time we stop being free
        onView(withId(R.id.spinnerEndHour)).perform(click());
        onData(allOf(is(instanceOf(Integer.class)), is(5))).perform(click());
        onView(withId(R.id.spinnerEndMinute)).perform(click());
        onData(allOf(is(instanceOf(Integer.class)), is(30))).perform(click());
        onView(withId(R.id.spinnerEndToD)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("PM"))).perform(click());


        onView(withId(R.id.scrollpanel)).perform(swipeUp());
        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        //Then new meeting shows up on the list.
        withText(containsString("HW 4 Meeting")).matches(onView(withId(R.id.lvMeetings)));
    }

    @Test
    public void testMembersfromWidget(){
        onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //Then we see the group page
        //When we click on new meeting...
        onView(withId(R.id.btnNewMeeting))
                .check(matches(isDisplayed()));

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        onView(withId(R.id.addmembers))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testAddMember(){
        onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        onView(withId(R.id.addmembers))
                .perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.tbMemberName), isDisplayed()));
        appCompatEditText.perform(click());

        appCompatEditText.perform(replaceText("Harry"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.tbMemberEmail), isDisplayed()));
        appCompatEditText4.perform(replaceText("getfreemoney@ymail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.tbMemberEmail), withText("getfreemoney@ymail.com"), isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCreateGroup), withText("Add Member"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView5.perform(click());

        /* ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("getfreemoney@ymail.com"),
                        childAtPosition(
                                allOf(withId(R.id.lvMembers),
                                        childAtPosition(
                                                withId(R.id.members),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("getfreemoney@ymail.com")));*/
        DataInteraction v2 = onData(hasToString(startsWith("getfreemoney@ymail.com")))
                .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
        v2.check(matches(withText("getfreemoney@ymail.com")));

    }

    @Test
    public void testAddMember2(){
        onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        onView(withId(R.id.addmembers))
                .perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.tbMemberName), isDisplayed()));
        appCompatEditText.perform(click());

        appCompatEditText.perform(replaceText("Students"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.tbMemberEmail), isDisplayed()));
        appCompatEditText4.perform(typeText("getexpensivemisery@ucsd.edu"), closeSoftKeyboard());

        /*ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.tbMemberEmail), withText("getexpensivemisery@ucsd.edu"), isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());*/

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCreateGroup), withText("Add Member"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(android.R.id.title), withText("Info"), isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatTextView9 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView9.perform(click());


        /* ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("getexpensivemisery@ucsd.edu"),
                        childAtPosition(
                                allOf(withId(R.id.lvMembers),
                                        childAtPosition(
                                                withId(R.id.members),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("getexpensivemisery@ucsd.edu")));*/
        DataInteraction v2 = onData(hasToString(startsWith("getexpensivemisery@ucsd.edu")))
                .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
        v2.check(matches(withText("getexpensivemisery@ucsd.edu")));
    }


    @Test
    public void testExistingGroup(){
        DataInteraction v2 = onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0);
        v2.check(matches(withText("newGroup")));
    }

    @Test
    public void testDatePicker() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.myFab),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        //find text box and type group1
        onView(withId(R.id.userInputDialog))
                .perform(typeText("A Group"));

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        onData(hasToString(startsWith("A Group")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

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

        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")), withContentDescription("Next month"),
                        withParent(allOf(withClassName(is("android.widget.DayPickerView")),
                                withParent(withClassName(is("com.android.internal.widget.DialogViewAnimator"))))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnCreateMeeting), withText("Create Meeting"),
                        withParent(allOf(withId(R.id.activity_create_meeting),
                                withParent(withId(R.id.scrollpanel))))));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction v = onData(hasToString(startsWith("Meet at")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v.check(matches(withText("Meet at 01:00 - 03:00")));
    }

    @Test
    public void testBackButton(){
        onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        onView((withId(R.id.addmembers)))
                .check(matches(isDisplayed()))
                .perform(click());

        pressBack();

        onView(withId(R.id.addmembers)).check(matches(isDisplayed()));

        pressBack();

        onView(withId(R.id.myFab)).check(matches(isDisplayed()));
    }

}
