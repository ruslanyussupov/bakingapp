package com.ruslaniusupov.android.bakingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruslaniusupov.android.bakingapp.adapters.RecipesAdapter;
import com.ruslaniusupov.android.bakingapp.loaders.RecipesLoader;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Recipe>>, RecipesAdapter.OnRecipeClickListener {

    private static final String LOG_TAG = RecipeListFragment.class.getSimpleName();
    private static final int RECIPES_LOADER_ID = 1;
    private static final String BUNDLE_RECIPES = "recipes";
    public static final String EXTRA_RECIPE = "recipe";

    private List<Recipe> mRecipes;
    private RecipesAdapter mAdapter;

    @BindView(R.id.recipes_rv)RecyclerView mRecipesRv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new RecipesAdapter(null, this);
        mRecipesRv.setAdapter(mAdapter);
        mRecipesRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState == null) {

            if (NetworkUtils.hasNetworkConnection(getActivity())) {

                LoaderManager loaderManager = getLoaderManager();
                if (loaderManager.getLoader(RECIPES_LOADER_ID) == null) {
                    loaderManager.initLoader(RECIPES_LOADER_ID, null, this);
                } else {
                    loaderManager.restartLoader(RECIPES_LOADER_ID, null, this);
                }

            } else {

                showNoNetworkConnectionState();

            }

        } else {

            mRecipes = savedInstanceState.getParcelableArrayList(BUNDLE_RECIPES);

            if (mRecipes == null) {
                showEmptyState();
            } else {
                mAdapter.swapData(mRecipes);
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_RECIPES, (ArrayList<Recipe>) mRecipes);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipesLoader(getActivity(), NetworkUtils.JSON_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {

        if (data == null || data.size() == 0) {
            showEmptyState();
            return;
        }

        Log.d(LOG_TAG, "Number of recipes: " + data.size());

        mRecipes = data;
        mAdapter.swapData(data);

    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mRecipes = null;
    }

    private void showEmptyState() {

    }

    private void showNoNetworkConnectionState() {

    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent openRecipeDetail = new Intent(getActivity(), DetailActivity.class);
        openRecipeDetail.putExtra(EXTRA_RECIPE, recipe);
        startActivity(openRecipeDetail);
    }

}
