package com.ruslaniusupov.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ruslaniusupov.android.bakingapp.adapters.StepsAdapter;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.models.Step;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements StepsAdapter.OnStepClickListener {

    private static final String BUNDLE_RECIPE = "recipe";
    public static final String EXTRA_STEPS = "steps";
    public static final String EXTRA_STEP_POSITION = "step_position";

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        View tabLayout = findViewById(R.id.tab_layout);
        mTwoPane = tabLayout != null && tabLayout.getVisibility() == View.VISIBLE;

        if (savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent.hasExtra(MainActivity.EXTRA_RECIPE)) {

                mRecipe = intent.getParcelableExtra(MainActivity.EXTRA_RECIPE);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_detail_container, RecipeDetailFragment.create(mRecipe))
                        .commit();

                if (mTwoPane) {

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_detail_container,
                                    StepDetailFragment.create(mRecipe.getSteps().get(0)))
                            .commit();

                }

            }

        } else {

            mRecipe = savedInstanceState.getParcelable(BUNDLE_RECIPE);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepClick(int position) {

        if (mTwoPane) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container,
                            StepDetailFragment.create(mRecipe.getSteps().get(position)))
                    .commit();

        } else {

            Intent openStepActivity = new Intent(this, StepActivity.class);
            openStepActivity.putExtra(EXTRA_STEPS, (ArrayList<Step>) mRecipe.getSteps());
            openStepActivity.putExtra(EXTRA_STEP_POSITION, position);
            startActivity(openStepActivity);

        }

    }

}
