package com.ruslaniusupov.android.bakingapp.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.db.WidgetContract;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.ui.DetailActivity;
import com.ruslaniusupov.android.bakingapp.utils.DbUtils;


public class RecipeWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {

        Recipe recipe = DbUtils.queryRecipe(context, widgetId);

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        view.setTextViewText(R.id.recipe_title, recipe.getName());

        // Open recipe's detail activity when click in title
        Intent recipeDetail = new Intent(context, DetailActivity.class);
        recipeDetail.putExtra(DetailActivity.EXTRA_RECIPE, recipe);
        recipeDetail.setData(Uri.parse(recipeDetail.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                recipeDetail, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.recipe_title, pendingIntent);

        // Set adapter for list view
        Intent widgetService = new Intent(context, RecipeWidgetService.class);
        widgetService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        widgetService.setData(Uri.parse(widgetService.toUri(Intent.URI_INTENT_SCHEME)));
        view.setRemoteAdapter(R.id.ingredients_lv, widgetService);
        view.setEmptyView(R.id.ingredients_lv, R.id.emptyView);

        appWidgetManager.updateAppWidget(widgetId, view);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        super.onDeleted(context, appWidgetIds);

        for (int widgetId : appWidgetIds) {

            // Delete associated with widget data from db
            context.getContentResolver().delete(WidgetContract.RecipeEntry.CONTENT_URI,
                    WidgetContract.RecipeEntry.COLUMN_WIDGET_ID + "=?",
                    new String[]{String.valueOf(widgetId)});

            context.getContentResolver().delete(WidgetContract.IngredientEntry.CONTENT_URI,
                    WidgetContract.IngredientEntry.COLUMN_WIDGET_ID + "=?",
                    new String[]{String.valueOf(widgetId)});

        }

    }
}
