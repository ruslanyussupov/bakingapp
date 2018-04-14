package com.ruslaniusupov.android.bakingapp.utils;


import android.util.Log;

import com.ruslaniusupov.android.bakingapp.models.Ingredient;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String STEPS = "steps";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";


    public static ArrayList<Recipe> getRecipesFromJson(String json) {

        ArrayList<Recipe> recipes = null;

        try {

            JSONArray recipesJsonArray = new JSONArray(json);

            int recipesCount = recipesJsonArray.length();

            if (recipesCount == 0) {
                return null;
            }

            recipes = new ArrayList<>(recipesCount);

            for (int recipeIndex = 0; recipeIndex < recipesCount; recipeIndex++) {

                JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(recipeIndex);

                int recipeId = recipeJsonObject.getInt(ID);
                String recipeName = recipeJsonObject.getString(NAME);

                JSONArray ingredientsJsonArray = recipeJsonObject.getJSONArray(INGREDIENTS);
                ArrayList<Ingredient> ingredients = null;

                int ingredientsCount = ingredientsJsonArray.length();
                if (ingredientsCount != 0) {

                    ingredients = new ArrayList<>(ingredientsCount);

                    for (int ingredientIndex = 0; ingredientIndex < ingredientsCount; ingredientIndex ++) {

                        JSONObject ingredientJsonObject = ingredientsJsonArray.getJSONObject(ingredientIndex);

                        int quantity = ingredientJsonObject.getInt(QUANTITY);
                        String measure = ingredientJsonObject.getString(MEASURE);
                        String ingredientName = ingredientJsonObject.getString(INGREDIENT);

                        Ingredient ingredient = new Ingredient(quantity, measure, ingredientName);

                        ingredients.add(ingredient);

                    }

                }

                JSONArray stepsJsonArray = recipeJsonObject.getJSONArray(STEPS);
                ArrayList<Step> steps = null;

                int stepsCount = stepsJsonArray.length();
                if (stepsCount != 0) {

                    steps = new ArrayList<>(stepsCount);

                    for (int stepIndex = 0; stepIndex < stepsCount; stepIndex++) {

                        JSONObject stepJsonObject = stepsJsonArray.getJSONObject(stepIndex);

                        int stepId = stepJsonObject.getInt(ID);
                        String shortDescription = stepJsonObject.getString(SHORT_DESCRIPTION);
                        String description = stepJsonObject.getString(DESCRIPTION);
                        String videoUrl = stepJsonObject.getString(VIDEO_URL);
                        String thumbnailUrl = stepJsonObject.getString(THUMBNAIL_URL);

                        Step step = new Step(stepId,
                                shortDescription,
                                description,
                                videoUrl,
                                thumbnailUrl);

                        steps.add(step);

                    }

                }

                int servings = recipeJsonObject.getInt(SERVINGS);
                String image = recipeJsonObject.getString(IMAGE);

                Recipe recipe = new Recipe(recipeId,
                        recipeName,
                        ingredients,
                        steps,
                        servings,
                        image);

                recipes.add(recipe);

            }


        } catch (JSONException e) {

            Log.e(LOG_TAG, "Can't parse the json", e);

        }

        return recipes;

    }


}
