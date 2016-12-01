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
        import org.junit.BeforeClass;
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


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExistingGroupTest {
    private static boolean set_Up = false;
    private static boolean m1 = false;
    private static boolean m2 = false;
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {

        if (set_Up == false) {
            //Given that you're in MainActivity (already signed in) and that you have a precreated
            //group called "newGroup"
            //click on "plus button"
            Thread.sleep(1000);
            onView(withId(R.id.myFab))
                    .perform(click());

            //call the new group newGroup
            Thread.sleep(1000);
            onView(withId(R.id.userInputDialog))
                    .perform(typeText("Group Z"));

            Thread.sleep(1000);
            //click create
            ViewInteraction appCompatButton = onView(
                    allOf(withId(android.R.id.button1), withText("Create"),
                            withParent(allOf(withId(R.id.buttonPanel),
                                    withParent(withId(R.id.parentPanel)))),
                            isDisplayed()));
            appCompatButton.perform(click());
        }
        set_Up = true;
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
    public void testExistingGroup() throws InterruptedException {
        //Given a preexisting group newGroup, when we search for new group in the list view,
        //then the new group is still there (and clickable)
        Thread.sleep(1000);
        DataInteraction v2 = onData(hasToString(startsWith("Group Z")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0);
        v2.check(matches(withText("Group Z")));
    }

    @Test
    public void testaddRandomMeeting() throws InterruptedException {

        Thread.sleep(1000);
        //...When we click on the group
        onData(hasToString(startsWith("Group Z")))
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

        //Determine how long we want our meeting to be, as well as input free times
        ViewInteraction appCompatSpinner = onView(
                withId(R.id.spinnerLengthHour));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("1"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction as = onView(
                withId(R.id.spinnerBeginHour));
        as.perform(scrollTo(), click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("11"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.spinnerEndHour));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction a3 = onView(
                allOf(withId(android.R.id.text1), withText("5"), isDisplayed()));
        a3.perform(click());

        ViewInteraction a2 = onView(
                withId(R.id.spinnerEndMinute));
        a2.perform(scrollTo(), click());

        ViewInteraction a4 = onView(
                allOf(withId(android.R.id.text1), withText("30"), isDisplayed()));
        a4.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                withId(R.id.spinnerEndToD));
        appCompatSpinner3.perform(scrollTo(), click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("PM"), isDisplayed()));
        appCompatTextView4.perform(click());


        onView(withId(R.id.scrollpanel)).perform(swipeUp());

        Thread.sleep(1000);
        //and when we click create meeting
        onView(withId(R.id.btnCreateMeeting)).perform(click());

        //Then new meeting shows up on the list.
        ViewInteraction v = onData(hasToString(startsWith("HW 4")))
                .inAdapterView(withId(R.id.lvMeetings)).atPosition(0)
                .perform(click());
        v.check(matches(isDisplayed()));
    }

    @Test
    public void testAddMember() throws InterruptedException {
        //when we enter the new group
        Thread.sleep(1000);
        onData(hasToString(startsWith("Group Z")))
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
        appCompatEditText.perform(replaceText("Dogs"), closeSoftKeyboard());

        //and when we enter an email
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.tbMemberEmail), isDisplayed()));
        appCompatEditText4.perform(typeText("getfreeleaves@yahoo.com"), closeSoftKeyboard());

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

        //then the new member's email is still there!
        DataInteraction v2 = onData(hasToString(startsWith("getfreeleaves@yahoo.com")))
                .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
        v2.check(matches(withText("getfreeleaves@yahoo.com")));

        //if we already put in another member from another test, it's going to be still be there
        if (m2)
        {
            DataInteraction v3 = onData(hasToString(startsWith("sleep@yahoo.com")))
                    .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
            v3.check(matches(withText("sleep@yahoo.com")));
        }

        m1 = true;
    }

    @Test
    public void addMember2() throws InterruptedException {
        Thread.sleep(1000);
        onData(hasToString(startsWith("Group Z")))
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

        //When we enter "Cats" for the name of the new member
        appCompatEditText.perform(replaceText("Cats"), closeSoftKeyboard());

        //and when we enter an email
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.tbMemberEmail), isDisplayed()));
        appCompatEditText4.perform(typeText("sleep@yahoo.com"), closeSoftKeyboard());

        //and when we click addmember
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCreateGroup), withText("Add Member"), isDisplayed()));
        appCompatButton2.perform(click());

        //and when we click on the members tab
        ViewInteraction ac = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        ac.perform(click());

        Thread.sleep(1000);
        //the new member's email is still there!
        DataInteraction v2 = onData(hasToString(startsWith("sleep@yahoo.com")))
                .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
        v2.check(matches(withText("sleep@yahoo.com")));

        if (m1)
        {
            Thread.sleep(1000);
            DataInteraction v3 = onData(hasToString(startsWith("getfreeleaves@yahoo.com")))
                    .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
            v3.check(matches(withText("getfreeleaves@yahoo.com")));
        }
        m2 = true;
    }




}
