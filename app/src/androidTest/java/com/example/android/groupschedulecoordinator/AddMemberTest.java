

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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddMemberTest {

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

    @Before
    public void setUp() throws InterruptedException {
        //Given that you're in MainActivity (already signed in) and that you have a precreated
        //group called "Group E"
        //click to create group
        Thread.sleep(4000);
        onView(withId(R.id.myFab))
                .perform(click());

        //name the group
        Thread.sleep(5000);
        onView(withId(R.id.userInputDialog))
                .perform(typeText("Group E"));

        //click create
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Create"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());
    }

    //tests adding a member to an existing group
    @Test
    public void testAddMember(){
        //Given that we have a new group, when we click on it
        onData(hasToString(startsWith("Group E")))
                .inAdapterView(withId(R.id.groupList)).atPosition(0)
                .perform(click());

        //and when we click on members in the content page
        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView7.perform(click());

        //and when we click on add members
        onView(withId(R.id.addmembers))
                .perform(click());

        //and when we type in the name of and email of the new member
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

        //and click on the button add member
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCreateGroup), withText("Add Member"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(android.R.id.title), withText("Members"), isDisplayed()));
        appCompatTextView5.perform(click());

        //then a new member with the info we just inputted appears in the list view
        DataInteraction v2 = onData(hasToString(startsWith("getfreemoney@ymail.com")))
                .inAdapterView(withId(R.id.lvMembers)).atPosition(0);
        v2.check(matches(withText("getfreemoney@ymail.com")));

    }


}
