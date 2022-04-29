package com.example.michaeldelishapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michaeldelishapp.backend.Recipe;

public class ViewRecipeActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        // Gets the recipe object from intent
        this.recipe = (Recipe) intent.getSerializableExtra("RECIPE");

        // Initialises the TextView and sets Text to recipe name
        TextView displayName = (TextView) findViewById(R.id.display_name);
        displayName.setText(recipe.getName());

        // Initialises the TextView and sets Text to recipe description
        TextView displayDescription = (TextView) findViewById(R.id.display_description);
        displayDescription.setText(recipe.getDescription());

        // Initialises the TextView and sets Text to recipe ingredients
        TextView displayIngredients = (TextView) findViewById(R.id.display_ingredients);
        displayIngredients.setText(recipe.getIngredients());

        // Initialises the TextView and sets Text to recipe instructions
        TextView displayInstructions = (TextView) findViewById(R.id.display_instructions);
        displayInstructions.setText(recipe.getInstructions());

        // Initialises the ImageView and sets the Image to recipe image
        ImageView recipeImage = (ImageView) findViewById(R.id.recipe_image);;
        recipeImage.setImageBitmap(recipe.getImage());

        // Initialises button and sets OnClickListener
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts intent to go back to the MainActivity class which goes to the Recipes Fragment
                Intent intent = new Intent(ViewRecipeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}