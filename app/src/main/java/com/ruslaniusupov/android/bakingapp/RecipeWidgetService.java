package com.ruslaniusupov.android.bakingapp;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ruslaniusupov.android.bakingapp.db.WidgetContract;


public class RecipeWidgetService extends RemoteViewsService {

    private static final String LOG_TAG = RecipeWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class RecipeRemoteViewsFactory implements RemoteViewsFactory {

        private int mWidgetId;
        private Cursor mCursor;
        Context mContext;

        RecipeRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
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

            if (mCursor != null) {
                mCursor.close();
            }

            mCursor = mContext.getContentResolver().query(WidgetContract.IngredientEntry.CONTENT_URI,
                    null,
                    WidgetContract.IngredientEntry.COLUMN_WIDGET_ID + "=?",
                    new String[]{String.valueOf(mWidgetId)},
                    null);

        }

        @Override
        public void onDestroy() {
            Log.d(LOG_TAG, "onDestroy");
            mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor == null) {
                Log.d(LOG_TAG, "Cursor is NULL");
                return 0;
            }
            Log.d(LOG_TAG, "Cursor size: " + mCursor.getCount());
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Log.d(LOG_TAG, "getViewAt");

            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);

            if (mCursor != null && mCursor.getCount() != 0) {

                mCursor.moveToPosition(position);

                String name = mCursor.getString(mCursor.getColumnIndex(
                        WidgetContract.IngredientEntry.COLUMN_INGREDIENT_NAME));
                int quantity = mCursor.getInt(mCursor.getColumnIndex(
                        WidgetContract.IngredientEntry.COLUMN_QUANTITY));
                String measure = mCursor.getString(mCursor.getColumnIndex(
                        WidgetContract.IngredientEntry.COLUMN_MEASURE));

                Log.d(LOG_TAG, name);

                view.setTextViewText(R.id.ingredient_tv,
                        String.format(getString(R.string.ingredient_format_without_newline),
                                name,
                                String.valueOf(quantity),
                                measure));

            }

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
