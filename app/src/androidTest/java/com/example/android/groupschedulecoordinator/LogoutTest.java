package com.example.android.groupschedulecoordinator;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Emily on 11/30/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogoutTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        //Given that you're in MainActivity (already signed in) and that you have a precreated
        //group called "newGroup"
        //click on "plus button"
        Thread.sleep(3000);
        onView(withId(R.id.myFab))
                .perform(click());

        Thread.sleep(3000);
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

    @Test
    public void testLogout(){
        //Given the logout button, when we press it
        ViewInteraction logoutButton = onView(
                allOf(withId(R.id.button2),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        logoutButton.perform(click());

        //then we are brought back in to the sign in page and the user can sign back in.
        onView((withId(R.id.sign_in_button)))
                .check(matches(isDisplayed()));
    }

}
