package com.ruslaniusupov.android.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.adapters.StepsAdapter;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.models.Step;
import com.ruslaniusupov.android.bakingapp.ui.fragments.RecipeDetailFragment;
import com.ruslaniusupov.android.bakingapp.ui.fragments.StepDetailFragment;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements StepsAdapter.OnStepClickListener {

    private static final String BUNDLE_RECIPE = "recipe";
    public static final String EXTRA_RECIPE = "recipe";

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
            if (intent.hasExtra(EXTRA_RECIPE)) {

                mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);

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

        if (mRecipe != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(mRecipe.getName());
            }
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
            openStepActivity.putExtra(StepActivity.EXTRA_STEPS, (ArrayList<Step>) mRecipe.getSteps());
            openStepActivity.putExtra(StepActivity.EXTRA_STEP_POSITION, position);
            openStepActivity.putExtra(StepActivity.EXTRA_RECIPE_NAME, mRecipe.getName());
            startActivity(openStepActivity);

        }

    }

}
