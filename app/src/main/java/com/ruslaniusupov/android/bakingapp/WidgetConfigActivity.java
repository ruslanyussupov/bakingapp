package com.ruslaniusupov.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ruslaniusupov.android.bakingapp.adapters.RecipesAdapter;
import com.ruslaniusupov.android.bakingapp.db.WidgetContract;
import com.ruslaniusupov.android.bakingapp.models.Ingredient;
import com.ruslaniusupov.android.bakingapp.models.Recipe;

import java.util.List;

public class WidgetConfigActivity extends AppCompatActivity implements RecipesAdapter.OnRecipeClickListener {

    private static final String LOG_TAG = WidgetConfigActivity.class.getSimpleName();

    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        Intent intent = getIntent();
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);



    }

    @Override
    public void onRecipeClick(Recipe recipe) {

        ContentValues recipeCv = new ContentValues();

        recipeCv.put(WidgetContract.RecipeEntry.COLUMN_WIDGET_ID, mWidgetId);
        recipeCv.put(WidgetContract.RecipeEntry.COLUMN_RECIPE_NAME, recipe.getName());
        recipeCv.put(WidgetContract.RecipeEntry.COLUMN_RECIPE_SERVINGS, recipe.getServings());

        Log.d(LOG_TAG, recipe.getName());
        Log.d(LOG_TAG, "Widget id: " + mWidgetId);

        getContentResolver().insert(WidgetContract.RecipeEntry.CONTENT_URI, recipeCv);

        List<Ingredient> ingredients = recipe.getIngredients();

        if (ingredients != null && ingredients.size() != 0) {

            for (Ingredient ingredient : ingredients) {

                ContentValues ingredientCv = new ContentValues();

                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_WIDGET_ID, mWidgetId);
                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_INGREDIENT_NAME,
                        ingredient.getName());
                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_QUANTITY,
                        ingredient.getQuantity());
                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_MEASURE,
                        ingredient.getMeasure());

                getContentResolver().insert(WidgetContract.IngredientEntry.CONTENT_URI, ingredientCv);

            }

        }

        RecipeWidgetProvider.updateWidget(this, AppWidgetManager.getInstance(this), mWidgetId);

        finish();

    }

}
