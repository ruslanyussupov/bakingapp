package com.ruslaniusupov.android.bakingapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ruslaniusupov.android.bakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final int POSITION = 2;
    private static final String TITLE = "Yellow Cake";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeViewItem_OpensDetailActivity() {

        Espresso.onView(ViewMatchers.withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION, ViewActions.click()));

        Espresso.onView(ViewMatchers.withText(TITLE))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }


}
