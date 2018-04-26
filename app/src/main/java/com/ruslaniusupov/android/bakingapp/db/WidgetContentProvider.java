package com.ruslaniusupov.android.bakingapp.db;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class WidgetContentProvider extends ContentProvider {

    private static final int RECIPE = 100;
    private static final int RECIPE_ID = 101;
    private static final int INGREDIENT = 200;
    private static final int INGREDIENT_ID = 201;
    private static final int STEP = 300;
    private static final int STEP_ID = 301;

    private WidgetDbHelper mWidgetDbHelper;
    private static UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mWidgetDbHelper = new WidgetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mWidgetDbHelper.getReadableDatabase();
        long id;
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case RECIPE:
                cursor = db.query(WidgetContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RECIPE_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(WidgetContract.RecipeEntry.TABLE_NAME,
                        projection,
                        WidgetContract.RecipeEntry._ID + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT:
                cursor = db.query(WidgetContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(WidgetContract.IngredientEntry.TABLE_NAME,
                        projection,
                        WidgetContract.IngredientEntry._ID + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder);
                break;
            case STEP:
                cursor = db.query(WidgetContract.StepEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STEP_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(WidgetContract.StepEntry.TABLE_NAME,
                        projection,
                        WidgetContract.StepEntry._ID + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (sUriMatcher.match(uri)) {

            case RECIPE:
                return WidgetContract.RecipeEntry.CONTENT_TYPE;
            case RECIPE_ID:
                return WidgetContract.RecipeEntry.CONTENT_ITEM_TYPE;
            case INGREDIENT:
                return WidgetContract.IngredientEntry.CONTENT_TYPE;
            case INGREDIENT_ID:
                return WidgetContract.IngredientEntry.CONTENT_ITEM_TYPE;
            case STEP:
                return WidgetContract.StepEntry.CONTENT_TYPE;
            case STEP_ID:
                return WidgetContract.StepEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mWidgetDbHelper.getWritableDatabase();
        long rowId;
        Uri retUri = null;

        switch (sUriMatcher.match(uri)) {

            case RECIPE:
                rowId = db.insert(WidgetContract.RecipeEntry.TABLE_NAME, null, values);
                if (rowId > 0) {
                    retUri = WidgetContract.RecipeEntry.buildRecipeUri(rowId);
                }
                break;
            case INGREDIENT:
                rowId = db.insert(WidgetContract.IngredientEntry.TABLE_NAME, null, values);
                if (rowId > 0) {
                    retUri = WidgetContract.IngredientEntry.buildIngredientUri(rowId);
                }
                break;
            case STEP:
                rowId = db.insert(WidgetContract.StepEntry.TABLE_NAME, null, values);
                if (rowId > 0) {
                    retUri = WidgetContract.StepEntry.buildStepUri(rowId);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);

        }

        if (rowId > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mWidgetDbHelper.getWritableDatabase();
        long id;
        int rowsDeleted;

        switch (sUriMatcher.match(uri)) {

            case RECIPE:
                rowsDeleted = db.delete(WidgetContract.RecipeEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case RECIPE_ID:
                id = ContentUris.parseId(uri);
                rowsDeleted = db.delete(WidgetContract.RecipeEntry.TABLE_NAME,
                        WidgetContract.RecipeEntry._ID + "=?",
                        new String[]{String.valueOf(id)});
                break;
            case INGREDIENT:
                rowsDeleted = db.delete(WidgetContract.IngredientEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case INGREDIENT_ID:
                id = ContentUris.parseId(uri);
                rowsDeleted = db.delete(WidgetContract.IngredientEntry.TABLE_NAME,
                        WidgetContract.IngredientEntry._ID + "=?",
                        new String[]{String.valueOf(id)});
                break;
            case STEP:
                rowsDeleted = db.delete(WidgetContract.StepEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case STEP_ID:
                id = ContentUris.parseId(uri);
                rowsDeleted = db.delete(WidgetContract.StepEntry.TABLE_NAME,
                        WidgetContract.StepEntry._ID + "=?",
                        new String[]{String.valueOf(id)});
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);

        }

        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mWidgetDbHelper.getWritableDatabase();
        long id;
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {

            case RECIPE:
                rowsUpdated = db.update(WidgetContract.RecipeEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case RECIPE_ID:
                id = ContentUris.parseId(uri);
                rowsUpdated = db.update(WidgetContract.RecipeEntry.TABLE_NAME,
                        values,
                        WidgetContract.RecipeEntry._ID + "=?",
                        new String[]{String.valueOf(id)});
                break;
            case INGREDIENT:
                rowsUpdated = db.update(WidgetContract.IngredientEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case INGREDIENT_ID:
                id = ContentUris.parseId(uri);
                rowsUpdated = db.update(WidgetContract.IngredientEntry.TABLE_NAME,
                        values,
                        WidgetContract.IngredientEntry._ID + "=?",
                        new String[]{String.valueOf(id)});
                break;
            case STEP:
                rowsUpdated = db.update(WidgetContract.StepEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case STEP_ID:
                id = ContentUris.parseId(uri);
                rowsUpdated = db.update(WidgetContract.StepEntry.TABLE_NAME,
                        values,
                        WidgetContract.StepEntry._ID + "=?",
                        new String[]{String.valueOf(id)});
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);

        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private static UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(WidgetContract.CONTENT_AUTHORITY, WidgetContract.PATH_RECIPE, RECIPE);
        matcher.addURI(WidgetContract.CONTENT_AUTHORITY, WidgetContract.PATH_RECIPE + "/#",
                RECIPE_ID);
        matcher.addURI(WidgetContract.CONTENT_AUTHORITY, WidgetContract.PATH_INGREDIENT, INGREDIENT);
        matcher.addURI(WidgetContract.CONTENT_AUTHORITY, WidgetContract.PATH_INGREDIENT + "/#",
                INGREDIENT_ID);
        matcher.addURI(WidgetContract.CONTENT_AUTHORITY, WidgetContract.PATH_STEP, STEP);
        matcher.addURI(WidgetContract.CONTENT_AUTHORITY, WidgetContract.PATH_STEP + "/#",
                STEP_ID);

        return matcher;
    }

}
