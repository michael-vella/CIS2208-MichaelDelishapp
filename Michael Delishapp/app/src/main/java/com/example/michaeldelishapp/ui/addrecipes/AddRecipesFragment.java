package com.example.michaeldelishapp.ui.addrecipes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.michaeldelishapp.R;
import com.example.michaeldelishapp.backend.DbHelper;
import com.example.michaeldelishapp.backend.Recipe;
import com.example.michaeldelishapp.databinding.FragmentAddrecipesBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddRecipesFragment extends Fragment {

    // Attributes initialisation
    private FragmentAddrecipesBinding binding;
    private Bitmap bitmapImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Implemented from https://stackoverflow.com/questions/21192386/android-fragment-onclick-button-method
        View root = inflater.inflate(R.layout.fragment_addrecipes, container, false);
        // Getting attributes from views
        // Initialising Buttons to reference buttons created in addrecipes.xml
        Button buttonAddRecipe = (Button) root.findViewById(R.id.btnAddRecipe);
        Button buttonAddPhoto = (Button) root.findViewById(R.id.btnAddPhoto);

        // Initialising EditTexts to reference EditTexts created in addrecipes.xml
        EditText name = (EditText) root.findViewById(R.id.txtName);
        EditText description = (EditText)root.findViewById(R.id.txtDescription);
        EditText ingredients = (EditText)root.findViewById(R.id.txtIngredients);
        EditText instructions = (EditText)root.findViewById(R.id.txtInstructions);

        // Adds OnClick method for AddRecipe button
        buttonAddRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Initialising DbHelper
                DbHelper dbHelper = new DbHelper(getActivity().getApplicationContext());

                // Gets the values of the TextViews from the Fragment
                String nameValue = name.getText().toString();
                String descriptionValue = description.getText().toString();
                String ingredientsValue = ingredients.getText().toString();
                String instructionsValue = instructions.getText().toString();
                String imageString = "";
                if (bitmapImage != null){
                    imageString = bitmapToString(bitmapImage);
                }

                // Saving to database
                // Check whether they are not null
                // If null, output error message
                if (nameValue.equals("") || descriptionValue.equals("") || ingredientsValue.equals("") || instructionsValue.equals("") || imageString.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"Please insert all fields and photo.",Toast.LENGTH_SHORT).show();
                } else {
                    // We use -1 for ID as a dummy
                    // 0 for favourite so that it is not added to favourites
                    // Favourites: 0 not favourite, 1 set to favourite
                    Recipe recipe = new Recipe(-1, nameValue, descriptionValue, ingredientsValue, instructionsValue, bitmapImage, 0);

                    // Outputs confirmation to the user
                    long id = dbHelper.insertRecipe(recipe);
                    Toast.makeText(getActivity().getApplicationContext(),"Inserted with id: " +id + ".",Toast.LENGTH_SHORT).show();

                    // Removes the text inside the fields for a fresh view
                    name.setText(null);
                    description.setText(null);
                    ingredients.setText(null);
                    instructions.setText(null);
                    bitmapImage = null;
                }
            }
        });

        // Adds OnClick method for AddPhoto button
        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                someActivityResultLauncher.launch(intent);
            }
        });

        // Returns the fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                            bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            Toast.makeText(getActivity().getApplicationContext(),"Successfully added image",Toast.LENGTH_SHORT).show();
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