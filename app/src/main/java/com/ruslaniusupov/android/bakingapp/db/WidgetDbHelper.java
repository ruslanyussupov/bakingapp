package com.ruslaniusupov.android.bakingapp.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class WidgetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "widget.db";
    private static final int DATABASE_VERSION = 1;

    WidgetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RECIPE_TABLE = "CREATE TABLE " + WidgetContract.RecipeEntry.TABLE_NAME + " ("
                + WidgetContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WidgetContract.RecipeEntry.COLUMN_WIDGET_ID + " INTEGER NOT NULL, "
                + WidgetContract.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, "
                + WidgetContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT, "
                + WidgetContract.RecipeEntry.COLUMN_RECIPE_SERVINGS + " INTEGER);";

        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + WidgetContract.IngredientEntry.TABLE_NAME + " ("
                + WidgetContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WidgetContract.IngredientEntry.COLUMN_WIDGET_ID + " INTEGER NOT NULL, "
                + WidgetContract.IngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT, "
                + WidgetContract.IngredientEntry.COLUMN_QUANTITY + " INTEGER, "
                + WidgetContract.IngredientEntry.COLUMN_MEASURE + " TEXT);";

        String CREATE_STEP_TABLE = "CREATE TABLE " + WidgetContract.StepEntry.TABLE_NAME + " ("
                + WidgetContract.StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WidgetContract.StepEntry.COLUMN_WIDGET_ID + " INTEGER NOT NULL, "
                + WidgetContract.StepEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, "
                + WidgetContract.StepEntry.COLUMN_SHORT_DESCRIPTION + " TEXT, "
                + WidgetContract.StepEntry.COLUMN_DESCRIPTION + " TEXT, "
                + WidgetContract.StepEntry.COLUMN_VIDEO_URL + " TEXT, "
                + WidgetContract.StepEntry.COLUMN_THUMBNAIL_URL + " TEXT);";

        db.execSQL(CREATE_RECIPE_TABLE);
        db.execSQL(CREATE_INGREDIENT_TABLE);
        db.execSQL(CREATE_STEP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
