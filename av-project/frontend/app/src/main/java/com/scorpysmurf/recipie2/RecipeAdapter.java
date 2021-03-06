package com.scorpysmurf.recipie2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    ArrayList<Recipe> recipes;
    private ClickListener mOnClickListener;

    public RecipeAdapter(ArrayList<Recipe> recipes, ClickListener clickListener) {
        this.recipes = recipes;
        this.mOnClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_design,parent,false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view,mOnClickListener);

        return recipeViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        Recipe recipe = recipes.get(position);

        holder.name.setText(recipe.getName());
        holder.servings.setText(recipe.getServings());
        holder.totalTime.setText(Integer.toString(recipe.getTotalTime()));

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView servings;
        TextView totalTime;
        ClickListener clickListener;

        public RecipeViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.recipe_name_textview);
            servings = itemView.findViewById(R.id.servings_textview);
            totalTime = itemView.findViewById(R.id.time_textview);
            this.clickListener = clickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }

}
