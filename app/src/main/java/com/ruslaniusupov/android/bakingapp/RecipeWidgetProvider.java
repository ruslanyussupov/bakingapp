package com.ruslaniusupov.android.bakingapp;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.ruslaniusupov.android.bakingapp.db.WidgetContract;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = RecipeWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        String recipeName = context.getString(R.string.default_recipe_name);

        Cursor cursor = context.getContentResolver().query(WidgetContract.RecipeEntry.CONTENT_URI,
                new String[]{WidgetContract.RecipeEntry.COLUMN_RECIPE_NAME},
                WidgetContract.RecipeEntry.COLUMN_WIDGET_ID + "=?",
                new String[]{String.valueOf(widgetId)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            recipeName = cursor.getString(cursor.getColumnIndex(
                    WidgetContract.RecipeEntry.COLUMN_RECIPE_NAME));
            cursor.close();
        }

        Log.d(LOG_TAG, recipeName);

        view.setTextViewText(R.id.recipe_title, recipeName);

        Intent widgetService = new Intent(context, RecipeWidgetService.class);
        widgetService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        widgetService.setData(Uri.parse(widgetService.toUri(Intent.URI_INTENT_SCHEME)));
        view.setRemoteAdapter(R.id.ingredients_lv, widgetService);
        view.setEmptyView(R.id.ingredients_lv, R.id.emptyView);

        appWidgetManager.updateAppWidget(widgetId, view);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.ingredients_lv);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        Log.d(LOG_TAG, "onDeleted");

        super.onDeleted(context, appWidgetIds);

        for (int widgetId : appWidgetIds) {

            context.getContentResolver().delete(WidgetContract.RecipeEntry.CONTENT_URI,
                    WidgetContract.RecipeEntry.COLUMN_WIDGET_ID + "=?",
                    new String[]{String.valueOf(widgetId)});

            context.getContentResolver().delete(WidgetContract.IngredientEntry.CONTENT_URI,
                    WidgetContract.IngredientEntry.COLUMN_WIDGET_ID + "=?",
                    new String[]{String.valueOf(widgetId)});

        }

    }
}
