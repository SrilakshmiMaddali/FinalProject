package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class MainActivityTest {

    private final IdlingResource mIdlingResource = IdlingResourceSingleton.getInstance().getIdlingResource();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {

        ViewInteraction buttonTellJoke = onView(
                allOf(withId(R.id.button_tell_joke),
                        isDisplayed()));
        buttonTellJoke.perform(click());

        ViewInteraction jokeTextView = onView(
                allOf(withId(R.id.joke_textview), isDisplayed()));
        jokeTextView.check(matches(textLengthNotZero()));
    }

    public static TypeSafeMatcher<View> textLengthNotZero() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return ((TextView) item).getText().toString().length() > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("textLengthNotZero");
            }
        };
    }
}
