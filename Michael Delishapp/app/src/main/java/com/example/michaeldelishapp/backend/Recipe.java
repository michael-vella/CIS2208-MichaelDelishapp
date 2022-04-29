package com.example.michaeldelishapp.backend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Recipe implements Serializable {
    // Defining class attributes
    private long id;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    private String image;
    private int favourite;

    // Attributes used for minimising Bitmap
    private static final float PREFERRED_WIDTH = 250;
    private static final float PREFERRED_HEIGHT = 250;

    // Recipe Constructor
    public Recipe(long Id, String Name, String Description, String Ingredients, String Instructions, Bitmap Image, int Favourite){
        this.id = Id;
        this.name = Name;
        this.description = Description;
        this.ingredients = Ingredients;
        this.instructions = Instructions;
        this.image = bitmapToString(resizeBitmap(Image));
        this.favourite = Favourite;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getFavourite() { return favourite; }

    public Bitmap getImage() {
        return stringToBitmap(this.image);
    }

    public String getImageAsString() {
        return this.image;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setFavourite(int favourite) { this.favourite = favourite; }

    // Method to convert a bitmap into a string so that it will be stored in the database as a string
    // It is stored as a string for storage purpouses
    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    // Method to convert a string to a Bitmap
    // The string will be gathered from the database and converted to a Bitmap so that it can be displayed.
    static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    // Method to resize the Bitmap so that it can be saved inside the database
    public static Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = PREFERRED_WIDTH / width;
        float scaleHeight = PREFERRED_HEIGHT / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        /*bitmap.recycle();*/
        return resizedBitmap;
    }
}
