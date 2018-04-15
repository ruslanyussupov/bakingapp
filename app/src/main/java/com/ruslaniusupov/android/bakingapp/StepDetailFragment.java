package com.ruslaniusupov.android.bakingapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruslaniusupov.android.bakingapp.models.Step;


import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    private static final String BUNDLE_STEP = "step";

    private Step mStep;

    @BindView(R.id.step_description)TextView mStepDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {

            if (getArguments().containsKey(BUNDLE_STEP)) {

                mStep = getArguments().getParcelable(BUNDLE_STEP);

                showContent(mStep);

            }

        } else {

            mStep = savedInstanceState.getParcelable(BUNDLE_STEP);

            showContent(mStep);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_STEP, mStep);
        super.onSaveInstanceState(outState);
    }

    public static StepDetailFragment create(Step step) {

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_STEP, step);
        stepDetailFragment.setArguments(args);

        return stepDetailFragment;

    }

    private void showContent(Step step) {

        if (step != null) {

            String description = step.getDescription();
            mStepDescription.setText(description);

        }

    }

}
