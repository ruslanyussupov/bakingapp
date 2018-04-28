package com.ruslaniusupov.android.bakingapp;


import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ruslaniusupov.android.bakingapp.ui.DetailActivity;

import org.hamcrest.core.StringContains;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    private static final int STEP_POSITION = 3;
    private static final String STEP_DESC = "Recipe Introduction";

    @Rule
    public ActivityTestRule<DetailActivity> mActivityRule =
            new ActivityTestRule<>(DetailActivity.class, false, false);

    @Test
    public void clickStepViewItem_OpenStepDetail() {

        Intent intent = new Intent();
        intent.putExtra(DetailActivity.EXTRA_RECIPE, DummyData.getRecipe());
        mActivityRule.launchActivity(intent);

        Espresso.onView(ViewMatchers.withId(R.id.steps_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(STEP_POSITION, ViewActions.click()));

        Espresso.onView(ViewMatchers.withId(R.id.video_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.step_description))
                .check(ViewAssertions.matches(ViewMatchers.withText(StringContains.containsString(STEP_DESC))));

    }

}
