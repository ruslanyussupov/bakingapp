package com.ruslaniusupov.android.bakingapp;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ruslaniusupov.android.bakingapp.db.WidgetContract;
import com.ruslaniusupov.android.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class RecipeWidgetService extends RemoteViewsService {

    private static final String LOG_TAG = RecipeWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(intent);
    }

    class RecipeRemoteViewsFactory implements RemoteViewsFactory {

        private int mWidgetId;
        private ArrayList<Ingredient> mIngredients;

        RecipeRemoteViewsFactory(Intent intent) {
            mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            Log.d(LOG_TAG, "onCreate");
        }

        @Override
        public void onDataSetChanged() {

            Log.d(LOG_TAG, "onDataSetChanged");

            Cursor cursor = getContentResolver().query(WidgetContract.IngredientEntry.CONTENT_URI,
                    null,
                    WidgetContract.IngredientEntry.COLUMN_WIDGET_ID + "=?",
                    new String[]{String.valueOf(mWidgetId)},
                    null);

            if (cursor != null && cursor.getCount() != 0) {

                mIngredients = new ArrayList<>(cursor.getCount());

                while (cursor.moveToNext()) {

                    String name = cursor.getString(cursor.getColumnIndex(
                            WidgetContract.IngredientEntry.COLUMN_INGREDIENT_NAME));
                    int quantity = cursor.getInt(cursor.getColumnIndex(
                            WidgetContract.IngredientEntry.COLUMN_QUANTITY));
                    String measure = cursor.getString(cursor.getColumnIndex(
                            WidgetContract.IngredientEntry.COLUMN_MEASURE));

                    Log.d(LOG_TAG, name);

                    Ingredient ingredient = new Ingredient(quantity, measure, name);

                    mIngredients.add(ingredient);

                }

                cursor.close();

            }

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredients == null) {
                return 0;
            }
            return mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Ingredient ingredient = mIngredients.get(position);

            RemoteViews view = new RemoteViews(getPackageName(), R.layout.ingredient_widget_item);

            view.setTextViewText(R.id.ingredient_tv,
                    String.format(getString(R.string.ingredient_format_without_newline),
                            ingredient.getName(),
                            String.valueOf(ingredient.getQuantity()),
                            ingredient.getMeasure()));

            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
