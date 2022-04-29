package com.example.michaeldelishapp.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "delishapp.db";
    private final String _name = RecipeContract.RecipeEntry.COLUMN_NAME_NAME;
    private final String _description = RecipeContract.RecipeEntry.COLUMN_NAME_DESCRIPTION;
    private final String _ingredients = RecipeContract.RecipeEntry.COLUMN_NAME_INGREDIENTS;
    private final String _instructions = RecipeContract.RecipeEntry.COLUMN_NAME_INSTRUCTIONS;
    private final String _image = RecipeContract.RecipeEntry.COLUMN_NAME_IMAGE;
    private final String _favourite = RecipeContract.RecipeEntry.COLUMN_NAME_FAVOURITE;

    // DbHelper constructor
    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate method to create the database
    public void onCreate(SQLiteDatabase db){
        db.execSQL(createTables());
    }

    // onUpgrade method to remove schema once DATABASE_VERSION is updated
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(dropTables());
        onCreate(db);
    }

    // onDowngrade to downgrade to previous database version
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    // Creates Recipes table with attributes
    private String createTables() {
        return "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" + RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY, " + _name + " varchar, " + _description + " varchar, " + _ingredients + " varchar, " + _instructions + " varchar, " + _image + " TEXT, " + _favourite + " INTEGER)";
    }

    // Drops Recipes table
    private String dropTables(){
        return "DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME;
    }

    // Method to insert new Recipe
    public long insertRecipe (Recipe recipe){
        // Initialisation of WritableDatabase and ContentValues
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Gets recipe and puts values inside ContentValues
        // Content Values is then used during insertion
        values.put(_name, recipe.getName());
        values.put(_description, recipe.getDescription());
        values.put(_ingredients, recipe.getIngredients());
        values.put(_instructions, recipe.getInstructions());
        values.put(_image, recipe.getImageAsString());
        values.put(_favourite, recipe.getFavourite());

        // Returns ID to display back to the user
        long id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, values);
        return id;
    }

    // Deletion of recipe
    public void deleteRecipe (Recipe recipe){
        //Initialisation of WritableDatabase
        SQLiteDatabase db = this.getWritableDatabase();

        // Deletes the record where id matches with recipe ID
        db.delete(RecipeContract.RecipeEntry.TABLE_NAME, "_id = ?", new String[] {String.valueOf(recipe.getId())});
    }

    // Method to adds recipe to favourites
    public void addRecipeToFavourites(Recipe recipe){
        // Initialisation of WritableDatabase and ContentValues
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Updates favourite value
        values.put(_favourite, recipe.getFavourite());

        // Update statement that updates the record that matches with recipe ID
        db.update(RecipeContract.RecipeEntry.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(recipe.getId())});
    }

    // Method to remove a recipe from Favourites
    public void removeRecipeFromFavourites(Recipe recipe){
        // Initialisation of WritableDatabase and ContentValues
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Updates favourite value
        values.put(_favourite, recipe.getFavourite());

        // Update statement that updates the record that matches with recipe ID
        db.update(RecipeContract.RecipeEntry.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(recipe.getId())});
    }

    // Method to get all recipes
    public ArrayList<Recipe> getRecipes() {
        // Initialisation of empty Recipe Arraylist and ReadableDatabase
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Projections array used to get the values from the database
        String[] projection = {
            BaseColumns._ID,
            _name,
            _description,
            _ingredients,
            _instructions,
            _image,
            _favourite
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = _name + " ASC";

        // Cursor that stores all of the rows from the query
        Cursor cursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);

        // Moves through the cursor and gets each attribute (of each record)
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(_name));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(_description));
            String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(_ingredients));
            String instructions = cursor.getString(cursor.getColumnIndexOrThrow(_instructions));
            String imageString = cursor.getString(cursor.getColumnIndexOrThrow(_image));
            int favourite = cursor.getInt(cursor.getColumnIndexOrThrow(_favourite));

            // Creates a nre empty recipe
            Recipe recipe = null;
            // Creates a bitmap by getting the image string from the database and converting it to BitMap
            Bitmap image = recipe.stringToBitmap(imageString);

            // Creates a new Recipe based on the values from the database
            recipe = new Recipe(id, name, description, ingredients, instructions, image, favourite);
            // Adds recipe to the ArrayList
            recipes.add(recipe);
        }
        // Closes the cursors
        cursor.close();

        // Returns the ArrayList
        return recipes;
    }

    // Method to update a specific recipe
    public void updateRecipe(Recipe recipe){
        // Initialisation of WritableDatabase and ContentValues
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Places the values of the new updated recipe inside the Content Values
        values.put(_name, recipe.getName());
        values.put(_description, recipe.getDescription());
        values.put(_ingredients, recipe.getIngredients());
        values.put(_instructions, recipe.getInstructions());
        values.put(_image, recipe.getImageAsString());
        values.put(_favourite, recipe.getFavourite());

        // Update method that is used to update the recipe inside the database
        db.update(RecipeContract.RecipeEntry.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(recipe.getId())});
    }

    public ArrayList<Recipe> getFavouriteRecipes() {
        // Initialisation of empty Recipe Arraylist and ReadableDatabase
        ArrayList<Recipe> favouriteRecipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Projections array used to get the values from the database
        String[] projection = {
                BaseColumns._ID,
                _name,
                _description,
                _ingredients,
                _instructions,
                _image,
                _favourite
        };

        // Filter results WHERE "id" = condition
        String selection = _favourite + " = ?";
        String[] selectionArgs = { "1" };

        // How you want the results sorted in the resulting cursor
        String sortOrder = _name + " ASC";

        // Cursor that stores all of the rows from the query
        Cursor cursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        // Moves through the cursor and gets each attribute (of each record)
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(_name));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(_description));
            String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(_ingredients));
            String instructions = cursor.getString(cursor.getColumnIndexOrThrow(_instructions));
            String imageString = cursor.getString(cursor.getColumnIndexOrThrow(_image));
            int favourite = cursor.getInt(cursor.getColumnIndexOrThrow(_favourite));

            // Creates a nre empty recipe
            Recipe recipe = null;
            // Creates a bitmap by getting the image string from the database and converting it to BitMap
            Bitmap image = recipe.stringToBitmap(imageString);

            // Creates a new Recipe based on the values from the database
            recipe = new Recipe(id, name, description, ingredients, instructions, image, favourite);
            // Adds recipe to the ArrayList
            favouriteRecipes.add(recipe);
        }
        // Closes the cursor
        cursor.close();

        // Returns the ArrayList
        return favouriteRecipes;
    }


}
