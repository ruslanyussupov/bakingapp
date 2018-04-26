package com.ruslaniusupov.android.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.adapters.RecipesAdapter;
import com.ruslaniusupov.android.bakingapp.models.Recipe;


public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnRecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent openRecipeDetail = new Intent(this, DetailActivity.class);
        openRecipeDetail.putExtra(DetailActivity.EXTRA_RECIPE, recipe);
        startActivity(openRecipeDetail);
    }

}
