package com.example.michaeldelishapp.backend;

import android.provider.BaseColumns;

public class RecipeContract {
    private RecipeContract(){}

    // Inner class that defines table contents
    public static class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_INGREDIENTS = "ingredients";
        public static final String COLUMN_NAME_INSTRUCTIONS = "instructions";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_FAVOURITE = "favourite";
    }
}
