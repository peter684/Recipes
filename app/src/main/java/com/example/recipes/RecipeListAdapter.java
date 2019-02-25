package com.example.recipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class RecipeListAdapter extends SelectableAdapter<RecipeListAdapter.ViewHolder> implements Recipe.DownloadCompleteListener {

    private ArrayList<Recipe> recipes;

    private ViewHolder.ClickHandler clickHandler;

    public void onDataLoaded(String recipeId){
        //@TODO: //find specific item based on ID
        notifyDataSetChanged();
    }

    RecipeListAdapter(ArrayList<Recipe> recipes, ViewHolder.ClickHandler clickHandler) {
        super();
        this.recipes = recipes;
        this.clickHandler = clickHandler;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_row, parent, false);
        return new ViewHolder(v, clickHandler);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Recipe recipe = recipes.get(position);
        recipe.setDownloadCompleteListener(this);
        holder.recipeTitle.setText(recipe.getRecipeTitle());
        holder.recipeBody.setText(recipe.getRecipeBody());
        holder.recipeBitmap.setImageBitmap(recipe.getRecipeBitmap());

        if (isSelected(position)) {
            holder.selectedOverlay.setVisibility(View.VISIBLE);
        }
        else{
            holder.selectedOverlay.setVisibility(View.INVISIBLE);
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public ArrayList<Recipe> getData(){
        return recipes;
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView recipeTitle, recipeBody;
        ImageView recipeBitmap;
        View selectedOverlay;
        private ClickHandler clickHandler;

        ViewHolder(View itemView, ClickHandler clickHandler) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.txtRecipeTitle);
            recipeBody = itemView.findViewById(R.id.txtRecipeBody);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);
            recipeBitmap = itemView.findViewById(R.id.bmpRecipeBitmap);
            this.clickHandler = clickHandler;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (clickHandler!= null)  clickHandler.onItemClicked(getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View v) {
            if (clickHandler != null) return  clickHandler.onItemLongClicked(getAdapterPosition());
            else return false;
        }



        public interface ClickHandler{
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }

    }

}
