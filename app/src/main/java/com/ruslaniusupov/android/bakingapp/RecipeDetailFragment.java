package com.ruslaniusupov.android.bakingapp;


import android.content.Intent;
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


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements StepsAdapter.OnStepClickListener {

    private static final String BUNDLE_RECIPE = "recipe";

    private StepsAdapter mAdapter;
    private Recipe mRecipe;

    @BindView(R.id.ingredients_tv)TextView mIngredientsTv;
    @BindView(R.id.steps_rv)RecyclerView mStepsRv;

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

        mAdapter = new StepsAdapter(null, this);
        mStepsRv.setAdapter(mAdapter);
        mStepsRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState == null) {

            Intent intent = getActivity().getIntent();

            if (intent.hasExtra(RecipeListFragment.EXTRA_RECIPE)) {
                mRecipe = intent.getParcelableExtra(RecipeListFragment.EXTRA_RECIPE);
                showContent(mRecipe);
            }

        } else {

            mRecipe = savedInstanceState.getParcelable(BUNDLE_RECIPE);
            showContent(mRecipe);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    public static RecipeDetailFragment create(Recipe recipe) {

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_RECIPE, recipe);
        recipeDetailFragment.setArguments(args);

        return recipeDetailFragment;

    }

    @Override
    public void onStepClick(Step step) {

    }

    private void showNoStepsState() {

    }

    private void showNoIngridientsState() {

    }

    private void showContent(Recipe recipe) {

        if (recipe != null) {

            List<Step> steps = recipe.getSteps();
            List<Ingredient> ingredients = recipe.getIngredients();

            if (steps == null || steps.size() == 0) {
                showNoStepsState();
            } else {
                mAdapter.swapData(steps);
            }

            if (ingredients == null || ingredients.size() == 0) {
                showNoIngridientsState();
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

        } else {
            showNoIngridientsState();
            showNoStepsState();
        }

    }

}
