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
 * Created by Emily on 11/30/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExistingGroupTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        //Given that you're in MainActivity (already signed in) and that you have a precreated
        //group called "newGroup"
        //click on "plus button"
        Thread.sleep(1000);
        onView(withId(R.id.myFab))
                .perform(click());

        Thread.sleep(1000);
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
    public void testExistingGroup(){
        //Given a preexisting group newGroup, when we search for new group in the list view,
        //then the new group is still there (and clickable)
        DataInteraction v2 = onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0);
        v2.check(matches(withText("newGroup")));
    }

    @Test
    public void testaddRandomMeeting() throws InterruptedException {

        //...When we click on the group
        onData(hasToString(startsWith("newGroup")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //Then we see the group content page (button new meeting exists)
        //When we click on new meeting...
        onView(withId(R.id.btnNewMeeting))
                .check(matches(isDisplayed()))
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

        Thread.sleep(1000);
        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        //Then new meeting shows up on the list.
        //withText(containsString("HW 4 Meeting")).matches(onView(withId(R.id.lvMeetings)));
        ViewInteraction v = onData(hasToString(startsWith("HW 4")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v.check(matches(isDisplayed()));
    }



}
