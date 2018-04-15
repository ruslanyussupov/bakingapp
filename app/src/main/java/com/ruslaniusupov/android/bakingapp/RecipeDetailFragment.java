package com.ruslaniusupov.android.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruslaniusupov.android.bakingapp.adapters.StepsAdapter;
import com.ruslaniusupov.android.bakingapp.models.Ingredient;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.models.Step;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    private static final String BUNDLE_RECIPE = "recipe";
    private static final String BUNDLE_INGREDIENTS = "ingredients";
    private static final String BUNDLE_STEPS = "steps";

    private StepsAdapter mAdapter;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private StepsAdapter.OnStepClickListener mStepClickListener;

    @BindView(R.id.ingredients_tv)TextView mIngredientsTv;
    @BindView(R.id.steps_rv)RecyclerView mStepsRv;
    @BindView(R.id.no_steps_state_tv)TextView mNoStepsStateTv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepsAdapter.OnStepClickListener) {
            mStepClickListener = (StepsAdapter.OnStepClickListener) context;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new StepsAdapter(null, mStepClickListener);
        mStepsRv.setAdapter(mAdapter);
        mStepsRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState == null) {

            if (getArguments().containsKey(BUNDLE_RECIPE)) {

                Recipe recipe = getArguments().getParcelable(BUNDLE_RECIPE);

                showContent(recipe);

            }

        } else {

            mIngredients = savedInstanceState.getParcelableArrayList(BUNDLE_INGREDIENTS);
            mSteps = savedInstanceState.getParcelableArrayList(BUNDLE_STEPS);

            showIngredients(mIngredients);
            showSteps(mSteps);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_INGREDIENTS, (ArrayList<Ingredient>) mIngredients);
        outState.putParcelableArrayList(BUNDLE_STEPS, (ArrayList<Step>) mSteps);
        super.onSaveInstanceState(outState);
    }

    public static RecipeDetailFragment create(Recipe recipe) {

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_RECIPE, recipe);
        recipeDetailFragment.setArguments(args);

        return recipeDetailFragment;

    }

    private void showNoStepsState() {

        mStepsRv.setVisibility(View.GONE);
        mNoStepsStateTv.setVisibility(View.VISIBLE);
        mNoStepsStateTv.setText(R.string.no_steps_state);

    }

    private void showNoIngredientsState() {

        mIngredientsTv.setText(R.string.no_ingredients_state);

    }

    private void showContent(Recipe recipe) {

        if (recipe == null) {

            showNoIngredientsState();
            showNoStepsState();

        } else {

            mIngredients = recipe.getIngredients();
            mSteps = recipe.getSteps();

            showIngredients(mIngredients);
            showSteps(mSteps);

        }

    }

    private void showIngredients(List<Ingredient> ingredients) {

        if (ingredients == null || ingredients.size() == 0) {

            showNoIngredientsState();

        } else {

            for (Ingredient ingredient : ingredients) {

                String name = ingredient.getName();
                String quantity = String.valueOf(ingredient.getQuantity());
                String measure = ingredient.getMeasure();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(quantity)) {

                    mIngredientsTv.append(String.format(getString(R.string.ingredient),
                            name, quantity, measure));

                } else if (!TextUtils.isEmpty(name)) {

                    mIngredientsTv.append(String.format(
                            getString(R.string.ingredient_without_measure),
                            name));

                }

            }

        }

    }

    private void showSteps(List<Step> steps) {

        if (steps == null || steps.size() == 0) {
            showNoStepsState();
        } else {
            mStepsRv.setVisibility(View.VISIBLE);
            mAdapter.swapData(steps);
        }

    }

}
