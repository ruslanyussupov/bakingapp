package com.ruslaniusupov.android.bakingapp;

import com.ruslaniusupov.android.bakingapp.models.Ingredient;
import com.ruslaniusupov.android.bakingapp.models.Recipe;
import com.ruslaniusupov.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

class DummyData {

    private static List<Ingredient> getIngredients() {

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient;
        String name = "unsalted butter, melted";
        String measure = "TBLSP";
        int quantity = 6;

        for (int i = 0; i < 6; i++) {
            ingredient = new Ingredient(quantity, measure, name);
            ingredients.add(ingredient);
        }

        return ingredients;

    }

    static List<Step> getSteps() {

        ArrayList<Step> steps = new ArrayList<>();
        Step step;
        String shortDesc = "Recipe Introduction";
        String descFormat = "%d - Recipe Introduction";
        String videoUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";

        for (int i = 0; i < 6; i++) {
            step = new Step(i, shortDesc, String.format(descFormat, i), videoUrl, null);
            steps.add(step);
        }

        return steps;

    }

    static Recipe getRecipe() {

        int id = 1;
        String name = "Nutella Pie";
        int servings = 8;

        return new Recipe(id, name, getIngredients(), getSteps(), servings, null);

    }

}
