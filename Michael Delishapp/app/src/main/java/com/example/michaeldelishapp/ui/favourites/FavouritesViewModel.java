package com.example.michaeldelishapp.ui.favourites;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.michaeldelishapp.backend.DbHelper;
import com.example.michaeldelishapp.backend.Recipe;

import java.util.ArrayList;
import java.util.List;

public class FavouritesViewModel extends ViewModel {

    private MutableLiveData<List<Recipe>> items;

    public FavouritesViewModel() {
        items = new MutableLiveData<>();
    }

    public MutableLiveData<List<Recipe>> getItems(Context context) {
        // Initialise a new ArrayList of type Recipe to store all recipes in it
        ArrayList<Recipe> recipes = new ArrayList<>();

        // Initialise a new instance of DbHelper
        DbHelper dbHelper = new DbHelper(context);
        // Gets all favourite recipes from the database
        recipes = dbHelper.getFavouriteRecipes();

        // Sets the MutableLiveData to the recipes gathered from the database
        items.setValue(recipes);
        // Returns back the MutableLiveData to the Adapter/Fragment
        return items;
    }
}