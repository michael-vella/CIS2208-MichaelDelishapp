package com.example.michaeldelishapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaeldelishapp.backend.DbHelper;
import com.example.michaeldelishapp.backend.Recipe;

import org.w3c.dom.Text;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<Recipe> items;

    public RecipesAdapter(List<Recipe> recipes){
        this.items = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Gets the itemView based on the layout_recipe.xml
        View itemView = inflater.inflate(R.layout.layout_recipe, parent, false);

        // Returns itemView (how the recipe is going to be viewed, the Recipe card)
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // Gets recipe at a particular position
        Recipe recipe = items.get(position);

        // Initialising the name, image and buttons to be displayed in the layout_recipe xml file
        TextView recipeName = holder.recipeName;
        recipeName.setText(recipe.getName());

        ImageView recipeImage = holder.recipeImageView;
        recipeImage.setImageBitmap(recipe.getImage());

        ImageView favouriteImage = holder.favouriteImageView;
        // Determines whether that particular recipe is set as a favourite and displays favourite icon accordingly
        if (recipe.getFavourite() == 0){
            // Not favourite Icon
            favouriteImage.setImageResource(R.drawable.ic_favourites__white24dp);
        } else {
            // Favourite Icon
            favouriteImage.setImageResource(R.drawable.ic_favourites_black_24dp);
        }
    }

    @Override
    public int getItemCount(){
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Initialisation of variables
        public TextView recipeName;
        public ImageView favouriteImageView;
        public ImageView editImageView;
        public ImageView deleteImageView;
        public ImageView recipeImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            // Sets the TextViews and ImagesViews and associates them with the views inside the xml file
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
            favouriteImageView = (ImageView) itemView.findViewById(R.id.favourite_icon);
            editImageView = (ImageView) itemView.findViewById(R.id.edit_icon);
            deleteImageView = (ImageView) itemView.findViewById(R.id.delete_icon);
            recipeImageView = (ImageView) itemView.findViewById(R.id.recipe_image);

            // Setting OnClickListeners for all methods

            recipeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){ recipeOnClick(v); }
            });

            recipeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){ recipeOnClick(v); }
            });

            favouriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    favouriteOnClick();
                }
            });

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    editOnClick(v);
                }
            });

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    deleteOnClick();
                }
            });
        }
        public void recipeOnClick(View v) {
            // Gets adapter position
            int pos = getAdapterPosition();
            // Gets recipe at that current position
            Recipe recipe = items.get(pos);

            // Passes recipe inside an Indent to open on a new Activity
            Intent intent = new Intent(v.getContext(), ViewRecipeActivity.class);
            intent.putExtra("RECIPE", recipe);
            v.getContext().startActivity(intent);
        }
        public void favouriteOnClick() {
            // Gets adapter position
            int pos = getAdapterPosition();
            // Gets recipe at that current position
            Recipe recipe = items.get(pos);

            // Initialise DbHelper class
            DbHelper db = new DbHelper(itemView.getContext());

            // Check whether it is favourite or not
            if (recipe.getFavourite() == 0){
                // Add to favourite
                recipe.setFavourite(1);
                db.addRecipeToFavourites(recipe);
                Toast.makeText(itemView.getContext(), "Added " +recipe.getName()+ " to favourite.",Toast.LENGTH_SHORT).show();
                favouriteImageView.setImageResource(R.drawable.ic_favourites_black_24dp);
            } else {
                // Already favourite, so remove from favourites
                recipe.setFavourite(0);
                db.removeRecipeFromFavourites(recipe);
                Toast.makeText(itemView.getContext(), "Removed " +recipe.getName()+ " from favourites.",Toast.LENGTH_SHORT).show();
                favouriteImageView.setImageResource(R.drawable.ic_favourites__white24dp);
            }

            notifyDataSetChanged();
        }
        public void editOnClick(View v){
            // Gets adapter position
            int pos = getAdapterPosition();
            // Gets recipe at that current position
            Recipe recipe = items.get(pos);

            // Passes recipe inside an Indent to open on a new Activity
            Intent intent = new Intent(v.getContext(), EditRecipeActivity.class);
            intent.putExtra("RECIPE", recipe);
            v.getContext().startActivity(intent);
            // Updates the data
            notifyDataSetChanged();
        }
        public void deleteOnClick(){
            // Gets adapter position
            int pos = getAdapterPosition();
            // Gets recipe at that current position
            Recipe recipe = items.get(pos);

            // Initialise DbHelper class
            DbHelper db = new DbHelper(itemView.getContext());

            // Remove recipe from both the database and the item List
            db.deleteRecipe(recipe);
            items.remove(recipe);
            // Updates the data
            notifyDataSetChanged();
        }
    }
}
