package com.ruslaniusupov.android.bakingapp.db;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class WidgetContract {

    public static final String CONTENT_AUTHORITY = "com.ruslaniusupov.android.bakingapp";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_INGREDIENT = "ingredient";
    public static final String PATH_STEP = "step";


    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_WIDGET_ID = "widgetId";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_RECIPE_NAME = "recipeName";
        public static final String COLUMN_RECIPE_SERVINGS = "recipeServings";

        public static Uri buildRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENT;

        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_WIDGET_ID = "widgetId";
        public static final String COLUMN_INGREDIENT_NAME = "ingredientName";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";

        public static Uri buildIngredientUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class StepEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEP).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STEP;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +  "/" + CONTENT_AUTHORITY + "/" + PATH_STEP;

        public static final String TABLE_NAME = "step";
        public static final String COLUMN_WIDGET_ID = "widgetId";
        public static final String COLUMN_STEP_ID = "stepId";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoUrl";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailUrl";

        public static Uri buildStepUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
