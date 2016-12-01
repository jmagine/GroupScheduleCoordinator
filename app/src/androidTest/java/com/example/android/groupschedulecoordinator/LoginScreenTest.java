package com.example.android.groupschedulecoordinator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.groupschedulecoordinator.R.id.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


@RunWith(AndroidJUnit4.class)
public class LoginScreenTest {

    @Rule
    public ActivityTestRule<LoginScreen> activityTestRule =
            new ActivityTestRule<>(LoginScreen.class);

    @Test
    public void validateSignIn() {
        //Given the sign in page, then the sign in button appears and then we can click on it
        onView(withId(R.id.sign_in_button))
                .check(matches(isDisplayed()))
                .perform(click());
    }



}
