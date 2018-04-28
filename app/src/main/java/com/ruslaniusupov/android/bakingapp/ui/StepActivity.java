package com.ruslaniusupov.android.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.models.Step;
import com.ruslaniusupov.android.bakingapp.ui.fragments.StepDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepActivity extends AppCompatActivity {

    private static final String BUNDLE_STEPS = "steps";
    private static final String BUNDLE_STEP_POSITION = "step_position";
    private static final String BUNDLE_RECIPE_NAME = "recipe_name";
    public static final String EXTRA_STEPS = "steps";
    public static final String EXTRA_STEP_POSITION = "step_position";
    public static final String EXTRA_RECIPE_NAME = "recipe_name";

    private List<Step> mSteps;
    private int mStepPosition;
    private String mRecipeName;

    @BindView(R.id.previous_step_btn)Button mPreviousStepBtn;
    @BindView(R.id.next_step_btn)Button mNextStepBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        mRecipeName = getString(R.string.default_recipe_name);

        if (savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_STEPS) &&
                    intent.hasExtra(EXTRA_STEP_POSITION)) {

                mSteps = intent.getParcelableArrayListExtra(EXTRA_STEPS);
                mStepPosition = intent.getIntExtra(EXTRA_STEP_POSITION, 0);

                if (intent.hasExtra(EXTRA_RECIPE_NAME)) {
                    mRecipeName = intent.getStringExtra(EXTRA_RECIPE_NAME);
                }

                if (mSteps == null) {
                    return;
                }

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_container,
                                StepDetailFragment.create(mSteps.get(mStepPosition)))
                        .commit();

                updateButtonsState();

            }

        } else {

            mSteps = savedInstanceState.getParcelableArrayList(BUNDLE_STEPS);
            mStepPosition = savedInstanceState.getInt(BUNDLE_STEP_POSITION);
            mRecipeName = savedInstanceState.getString(BUNDLE_RECIPE_NAME);

        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipeName);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_STEPS, (ArrayList<Step>) mSteps);
        outState.putInt(BUNDLE_STEP_POSITION, mStepPosition);
        outState.putString(BUNDLE_RECIPE_NAME, mRecipeName);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.previous_step_btn)
    public void previousStep() {

        if (mSteps == null) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container,
                        StepDetailFragment.create(mSteps.get(--mStepPosition)))
                .commit();

        updateButtonsState();

    }

    @OnClick(R.id.next_step_btn)
    public void nextStep() {

        if (mSteps == null) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container,
                        StepDetailFragment.create(mSteps.get(++mStepPosition)))
                .commit();

        updateButtonsState();

    }

    private void updateButtonsState() {

        if (mSteps != null && mStepPosition > 0) {
            mPreviousStepBtn.setEnabled(true);
        } else {
            mPreviousStepBtn.setEnabled(false);
        }

        if (mSteps != null && mStepPosition < mSteps.size() - 1) {
            mNextStepBtn.setEnabled(true);
        } else {
            mNextStepBtn.setEnabled(false);
        }

    }

}
