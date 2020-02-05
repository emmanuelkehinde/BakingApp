package com.kehinde.bakingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kehinde.bakingapp.R;
import com.kehinde.bakingapp.models.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kehinde on 6/3/17.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.RecipeViewHolder> {

    private ArrayList<Ingredient> ingredientArrayList;


    public IngredientListAdapter(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ingredient_name) TextView ingredient_name;
        @BindView(R.id.ingredient_measure) TextView ingredient_measure;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_ingredient,viewGroup,false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int i) {
        Ingredient ingredient=ingredientArrayList.get(i);
        String measure=ingredient.getQuantity()+" "+ingredient.getMeasure();

        recipeViewHolder.ingredient_name.setText(ingredient.getIngredient());
        recipeViewHolder.ingredient_measure.setText(measure);

    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }
}
