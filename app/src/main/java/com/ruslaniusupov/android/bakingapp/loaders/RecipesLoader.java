package com.ruslaniusupov.android.bakingapp.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.utils.JsonUtils;
import com.ruslaniusupov.android.bakingapp.utils.NetworkUtils;

import java.util.List;

public class RecipesLoader extends AsyncTaskLoader<List<Recipe>> {

    private final String mJsonUrl;
    private List<Recipe> mRecipes;

    public RecipesLoader(Context context, String jsonUrl) {
        super(context);
        mJsonUrl = jsonUrl;
    }

    @Override
    protected void onStartLoading() {

        if (mRecipes == null) {
            forceLoad();
        } else {
            deliverResult(mRecipes);
        }

    }

    @Override
    public List<Recipe> loadInBackground() {
        String json = NetworkUtils.getResponseFromUrl(mJsonUrl);
        return JsonUtils.getRecipesFromJson(json);
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        mRecipes = data;
        super.deliverResult(data);
    }
}
