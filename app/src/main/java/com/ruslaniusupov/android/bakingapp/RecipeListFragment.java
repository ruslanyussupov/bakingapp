package com.ruslaniusupov.android.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ruslaniusupov.android.bakingapp.adapters.RecipesAdapter;
import com.ruslaniusupov.android.bakingapp.loaders.RecipesLoader;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ruslaniusupov.android.bakingapp.adapters.RecipesAdapter.*;

public class RecipeListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String BUNDLE_RECIPES = "recipes";
    private static final int RECIPES_LOADER_ID = 1;
    private static final int GRID_ROWS = 3;

    private List<Recipe> mRecipes;
    private RecipesAdapter mAdapter;
    private boolean mTabLayout;
    private OnRecipeClickListener mRecipeClickListener;

    @BindView(R.id.recipes_rv)RecyclerView mRecipesRv;
    @BindView(R.id.state_tv)TextView mStateTv;
    @BindView(R.id.loading_pb)ProgressBar mLoadingPb;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            mRecipeClickListener = (OnRecipeClickListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        View tabLayout = rootView.findViewById(R.id.tab_layout);
        mTabLayout = tabLayout != null && tabLayout.getVisibility() == View.VISIBLE;

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new RecipesAdapter(null, mRecipeClickListener);
        mRecipesRv.setAdapter(mAdapter);

        if (mTabLayout) {
            mRecipesRv.setLayoutManager(new GridLayoutManager(getActivity(), GRID_ROWS));
        } else {
            mRecipesRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

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

                if (NetworkUtils.hasNetworkConnection(getActivity())) {
                    showEmptyState();
                } else {
                    showNoNetworkConnectionState();
                }

            } else {
                mRecipesRv.setVisibility(View.VISIBLE);
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

        mLoadingPb.setVisibility(View.VISIBLE);

        return new RecipesLoader(getActivity(), NetworkUtils.JSON_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {

        mLoadingPb.setVisibility(View.GONE);

        if (data == null || data.size() == 0) {
            showEmptyState();
            return;
        }

        mRecipesRv.setVisibility(View.VISIBLE);
        mRecipes = data;
        mAdapter.swapData(data);

    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mRecipes = null;
    }

    private void showEmptyState() {
        mRecipesRv.setVisibility(View.GONE);
        mLoadingPb.setVisibility(View.GONE);
        mStateTv.setVisibility(View.VISIBLE);
        mStateTv.setText(R.string.no_recipes_state);
    }

    private void showNoNetworkConnectionState() {
        mRecipesRv.setVisibility(View.GONE);
        mLoadingPb.setVisibility(View.GONE);
        mStateTv.setVisibility(View.VISIBLE);
        mStateTv.setText(R.string.no_internet_connection_state);
    }

}
