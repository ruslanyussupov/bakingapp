package com.ruslaniusupov.android.bakingapp.ui;

import android.content.Intent;
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

    private List<Step> mSteps;
    private int mStepPosition;

    @BindView(R.id.previous_step_btn)Button mPreviousStepBtn;
    @BindView(R.id.next_step_btn)Button mNextStepBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent.hasExtra(DetailActivity.EXTRA_STEPS) &&
                    intent.hasExtra(DetailActivity.EXTRA_STEP_POSITION)) {

                mSteps = intent.getParcelableArrayListExtra(DetailActivity.EXTRA_STEPS);
                mStepPosition = intent.getIntExtra(DetailActivity.EXTRA_STEP_POSITION, 0);

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

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_STEPS, (ArrayList<Step>) mSteps);
        outState.putInt(BUNDLE_STEP_POSITION, mStepPosition);
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
