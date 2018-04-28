package com.ruslaniusupov.android.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.adapters.RecipesAdapter;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.utils.DbUtils;
import com.ruslaniusupov.android.bakingapp.widget.RecipeWidgetProvider;


public class WidgetConfigActivity extends AppCompatActivity implements RecipesAdapter.OnRecipeClickListener {

    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        Intent intent = getIntent();
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(RESULT_CANCELED, resultValue);

    }

    @Override
    public void onRecipeClick(Recipe recipe) {

        DbUtils.insertRecipe(this, recipe, mWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(RESULT_OK, resultValue);

        RecipeWidgetProvider.updateWidget(this, AppWidgetManager.getInstance(this), mWidgetId);

        finish();

    }

}
