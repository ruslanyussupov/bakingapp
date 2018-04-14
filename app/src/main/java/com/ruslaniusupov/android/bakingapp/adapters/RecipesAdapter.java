package com.ruslaniusupov.android.bakingapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;
    private OnRecipeClickListener mRecipeClickListener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipesAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        mRecipes = recipes;
        mRecipeClickListener = listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View recipeView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);

        return new RecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        Recipe recipe = mRecipes.get(position);

        String name = recipe.getName();
        int numOfIngredients = recipe.getNumOfIngredients();
        int numOfSteps = recipe.getNumOfSteps();

        if (!TextUtils.isEmpty(name)) {
            holder.mRecipeNameTv.setText(name);
        }

        holder.mNumOfIngredientsTv.setText(String.valueOf(numOfIngredients));
        holder.mNumOfStepsTv.setText(String.valueOf(numOfSteps));

    }

    @Override
    public int getItemCount() {

        if (mRecipes == null) {
            return 0;
        }

        return mRecipes.size();

    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_tv)TextView mRecipeNameTv;
        @BindView(R.id.num_of_ingredients)TextView mNumOfIngredientsTv;
        @BindView(R.id.num_of_steps)TextView mNumOfStepsTv;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecipeClickListener.onRecipeClick(mRecipes.get(getAdapterPosition()));
                }
            });

        }

    }

    public void swapData(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

}
