package com.ruslaniusupov.android.bakingapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.db.WidgetContract;
import com.ruslaniusupov.android.bakingapp.models.Ingredient;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    private static final int DEFAULT_VALUE = -1;

    public static void insertRecipe(Context context, Recipe recipe, int widgetId) {

        ContentValues recipeCv = new ContentValues();

        recipeCv.put(WidgetContract.RecipeEntry.COLUMN_WIDGET_ID, widgetId);
        recipeCv.put(WidgetContract.RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
        recipeCv.put(WidgetContract.RecipeEntry.COLUMN_RECIPE_NAME, recipe.getName());
        recipeCv.put(WidgetContract.RecipeEntry.COLUMN_RECIPE_SERVINGS, recipe.getServings());

        context.getContentResolver().insert(WidgetContract.RecipeEntry.CONTENT_URI, recipeCv);

        List<Ingredient> ingredients = recipe.getIngredients();

        insertIngredients(context, ingredients, widgetId);

        List<Step> steps = recipe.getSteps();

        insertSteps(context, steps, widgetId);

    }

    private static void insertIngredients(Context context, List<Ingredient> ingredients, int widgetId) {

        if (ingredients != null && ingredients.size() != 0) {

            for (Ingredient ingredient : ingredients) {

                ContentValues ingredientCv = new ContentValues();

                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_WIDGET_ID, widgetId);
                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_INGREDIENT_NAME,
                        ingredient.getName());
                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_QUANTITY,
                        ingredient.getQuantity());
                ingredientCv.put(WidgetContract.IngredientEntry.COLUMN_MEASURE,
                        ingredient.getMeasure());

                context.getContentResolver().insert(WidgetContract.IngredientEntry.CONTENT_URI, ingredientCv);

            }

        }

    }

    private static void insertSteps(Context context, List<Step> steps, int widgetId) {

        if (steps != null && steps.size() != 0) {

            for (Step step : steps) {

                ContentValues stepCv = new ContentValues();

                stepCv.put(WidgetContract.StepEntry.COLUMN_WIDGET_ID, widgetId);
                stepCv.put(WidgetContract.StepEntry.COLUMN_STEP_ID, step.getId());
                stepCv.put(WidgetContract.StepEntry.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
                stepCv.put(WidgetContract.StepEntry.COLUMN_DESCRIPTION, step.getDescription());
                stepCv.put(WidgetContract.StepEntry.COLUMN_VIDEO_URL, step.getVideoUrl());
                stepCv.put(WidgetContract.StepEntry.COLUMN_THUMBNAIL_URL, step.getThumbnailUrl());

                context.getContentResolver().insert(WidgetContract.StepEntry.CONTENT_URI, stepCv);

            }

        }

    }

    public static Recipe queryRecipe(Context context, int widgetId) {

        String recipeName = context.getString(R.string.default_recipe_name);
        int recipeId = DEFAULT_VALUE;
        int recipeServings = DEFAULT_VALUE;
        List<Ingredient> ingredients = queryIngredients(context, widgetId);
        List<Step> steps = querySteps(context, widgetId);

        Cursor recipeCursor = context.getContentResolver().query(WidgetContract.RecipeEntry.CONTENT_URI,
                null,
                WidgetContract.RecipeEntry.COLUMN_WIDGET_ID + "=?",
                new String[]{String.valueOf(widgetId)},
                null);

        if (recipeCursor != null && recipeCursor.moveToFirst()) {
            recipeName = recipeCursor.getString(recipeCursor.getColumnIndex(
                    WidgetContract.RecipeEntry.COLUMN_RECIPE_NAME));
            recipeId = recipeCursor.getInt(recipeCursor.getColumnIndex(
                    WidgetContract.RecipeEntry.COLUMN_RECIPE_ID));
            recipeServings = recipeCursor.getInt(recipeCursor.getColumnIndex(
                    WidgetContract.RecipeEntry.COLUMN_RECIPE_SERVINGS));

            recipeCursor.close();
        }

        return new Recipe(recipeId, recipeName, ingredients, steps, recipeServings, null);

    }

    private static List<Ingredient> queryIngredients(Context context, int widgetId) {

        List<Ingredient> ingredients = null;

        Cursor ingredientCursor = context.getContentResolver().query(WidgetContract.IngredientEntry.CONTENT_URI,
                null,
                WidgetContract.IngredientEntry.COLUMN_WIDGET_ID + "=?",
                new String[]{String.valueOf(widgetId)},
                null);

        if (ingredientCursor != null && ingredientCursor.getCount() != 0) {

            ingredients = new ArrayList<>(ingredientCursor.getCount());

            while (ingredientCursor.moveToNext()){

                String name = ingredientCursor.getString(ingredientCursor.getColumnIndex(
                        WidgetContract.IngredientEntry.COLUMN_INGREDIENT_NAME));
                int quantity = ingredientCursor.getInt(ingredientCursor.getColumnIndex(
                        WidgetContract.IngredientEntry.COLUMN_QUANTITY));
                String measure = ingredientCursor.getString(ingredientCursor.getColumnIndex(
                        WidgetContract.IngredientEntry.COLUMN_MEASURE));

                Ingredient ingredient = new Ingredient(quantity, measure, name);

                ingredients.add(ingredient);

            }

            ingredientCursor.close();

        }

        return ingredients;

    }

    private static List<Step> querySteps(Context context, int widgetId) {

        List<Step> steps = null;

        Cursor stepCursor = context.getContentResolver().query(WidgetContract.StepEntry.CONTENT_URI,
                null,
                WidgetContract.StepEntry.COLUMN_WIDGET_ID + "=?",
                new String[]{String.valueOf(widgetId)},
                null);

        if (stepCursor != null && stepCursor.getCount() != 0) {

            steps = new ArrayList<>(stepCursor.getCount());

            while (stepCursor.moveToNext()) {

                int id = stepCursor.getInt(stepCursor.getColumnIndex(
                        WidgetContract.StepEntry.COLUMN_STEP_ID));
                String shortDescription = stepCursor.getString(stepCursor.getColumnIndex(
                        WidgetContract.StepEntry.COLUMN_SHORT_DESCRIPTION));
                String description = stepCursor.getString(stepCursor.getColumnIndex(
                        WidgetContract.StepEntry.COLUMN_DESCRIPTION));
                String videoUrl = stepCursor.getString(stepCursor.getColumnIndex(
                        WidgetContract.StepEntry.COLUMN_VIDEO_URL));
                String thumbnailUrl = stepCursor.getString(stepCursor.getColumnIndex(
                        WidgetContract.StepEntry.COLUMN_THUMBNAIL_URL));

                Step step = new Step(id, shortDescription, description, videoUrl, thumbnailUrl);

                steps.add(step);

            }

            stepCursor.close();

        }

        return steps;

    }

}
