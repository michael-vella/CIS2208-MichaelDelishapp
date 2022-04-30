package com.example.michaeldelishapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaeldelishapp.backend.DbHelper;
import com.example.michaeldelishapp.backend.Recipe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditRecipeActivity extends AppCompatActivity {

    private Recipe recipe;
    private Bitmap bitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        // Gets the recipe object from intent
        this.recipe = (Recipe) intent.getSerializableExtra("RECIPE");

        // Initialises the EditText and sets text to recipe name
        EditText displayName = (EditText) findViewById(R.id.txtEditName);
        displayName.setText(recipe.getName());

        // Initialises the EditText and sets text to recipe description
        EditText displayDescription = (EditText) findViewById(R.id.txtEditDescription);
        displayDescription.setText(recipe.getDescription());

        // Initialises the EditText and sets text to recipe ingredients
        EditText displayIngredients = (EditText) findViewById(R.id.txtEditIngredients);
        displayIngredients.setText(recipe.getIngredients());

        // Initialises the EditText and sets text to recipe instructions
        EditText displayInstructions = (EditText) findViewById(R.id.txtEditInstructions);
        displayInstructions.setText(recipe.getInstructions());

        // Initialises button and sets OnClickListener
        Button buttonAddPhoto = (Button) findViewById(R.id.btnAddPhoto);
        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Intent to open Gallery and pick an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                someActivityResultLauncher.launch(intent);
            }
        });

        // Initialises button and sets OnClickListener
        Button btnEdit = (Button) findViewById(R.id.btnEditRecipe);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialises DbHelper
                DbHelper db = new DbHelper(getApplicationContext());

                // Gets the values for new Edited Recipe
                // Gets ID and Favourites value that were previously set because these cannot be edited (from here)
                long idValue = recipe.getId();
                String nameValue = displayName.getText().toString();
                String descriptionValue = displayDescription.getText().toString();
                String ingredientsValue = displayIngredients.getText().toString();
                String instructionsValue = displayInstructions.getText().toString();
                String imageString = "";
                Bitmap bitmapImagePreviously = recipe.getImage();
                if (bitmapImage != null) {
                    imageString = bitmapToString(bitmapImage);
                } else {
                    imageString = bitmapToString(bitmapImagePreviously);
                    bitmapImage = bitmapImagePreviously;
                }
                int favouriteValue = recipe.getFavourite();

                // Saving to database
                // Check whether they are not null
                // If null, output error message
                if (nameValue.equals("") || descriptionValue.equals("") || ingredientsValue.equals("") || instructionsValue.equals("") || imageString.equals("")){
                    Toast.makeText(getApplicationContext(),"Please insert all fields and photo.",Toast.LENGTH_SHORT).show();
                } else {
                    // Creates a new edited Recipe
                    Recipe editedRecipe = new Recipe(idValue, nameValue, descriptionValue, ingredientsValue, instructionsValue, bitmapImage, favouriteValue);

                    // Updates recipe in database
                    db.updateRecipe(editedRecipe);
                    Toast.makeText(getApplicationContext(), "Edited recipe " +editedRecipe.getName(),Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Initialises button and sets OnClickListener
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts intent to go back to the MainActivity class which goes to the Recipes Fragment
                Intent intent = new Intent(EditRecipeActivity.this, MainActivity.class);
                startActivity(intent);
                /*NavController navController = Navigation.findNavController(EditRecipeActivity.this, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_recipes);*/
            }
        });
    }

    // Intent to get photo from gallery
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            Toast.makeText(getApplicationContext(), "Successfully edited image",Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    // Methods to covert bitmapToString - used for image saving
    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}