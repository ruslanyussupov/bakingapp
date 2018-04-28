package com.ruslaniusupov.android.bakingapp;


import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ruslaniusupov.android.bakingapp.models.Step;
import com.ruslaniusupov.android.bakingapp.ui.StepActivity;

import org.hamcrest.core.StringStartsWith;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class StepActivityTest {

    private static final String RECIPE_NAME = "Nutella Pie";
    private static final int STEP_POSITION = 3;
    private static final String STEP_ID = "4";

    @Rule
    public ActivityTestRule<StepActivity> mActivityRule =
            new ActivityTestRule<>(StepActivity.class, false, false);

    @Test
    public void clickNextStep_ShowsNextStep() {

        Intent intent = new Intent();
        intent.putExtra(StepActivity.EXTRA_RECIPE_NAME, RECIPE_NAME);
        intent.putExtra(StepActivity.EXTRA_STEP_POSITION, STEP_POSITION);
        intent.putExtra(StepActivity.EXTRA_STEPS, (ArrayList<Step>) DummyData.getSteps());
        mActivityRule.launchActivity(intent);

        Espresso.onView(ViewMatchers.withId(R.id.next_step_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.step_description))
                .check(ViewAssertions.matches(ViewMatchers.withText(StringStartsWith.startsWith(STEP_ID))));

    }

}
