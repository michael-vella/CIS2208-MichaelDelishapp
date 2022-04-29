package com.example.michaeldelishapp.ui.recipes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaeldelishapp.R;
import com.example.michaeldelishapp.RecipesAdapter;
import com.example.michaeldelishapp.backend.Recipe;
import com.example.michaeldelishapp.databinding.FragmentRecipesBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    // Initialisation of attributes
    private @NonNull FragmentRecipesBinding binding;
    private RecipesViewModel recipesViewModel;
    private RecipesAdapter adapter;
    private RecyclerView itemsView;
    private List<Recipe> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Setting up the ViewModel
        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);

        // Binding to the container
        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        // Gets the root to view
        View root = binding.getRoot();

        // Gets the items to be displayed
        itemsView = root.findViewById(R.id.recipes_list);
        // Calls the method to setup the RecyclerView
        setUpRecyclerView();
        // Calls the FetchItems method to insert items inside the RecyclerView
        fetchItems();
        NavController navController = NavHostFragment.findNavController(this);
        // Returns the root
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Method to FetchItems
    private void fetchItems() {
        // Getting the context
        Context context = this.getContext();
        // Passing context as parameter to update the Recycler View
        recipesViewModel.getItems(context).observe(getViewLifecycleOwner(), this::updateItemsList);
    }
    // Method to setUpRecyclerView
    private void setUpRecyclerView() {
        // Associates adapter with the RecipesAdapter, as well as all its data.
        adapter = new RecipesAdapter(items);
        itemsView.setAdapter(adapter);
        itemsView.setLayoutManager(new LinearLayoutManager(itemsView.getContext()));
    }
    // Method to update items inside the List
    private void updateItemsList(List<Recipe> newItems) {
        // Clear items to initialise new ArrayList (fixes duplicates error)
        items.clear();
        // Adds ArrayList to items
        items.addAll(newItems);
        adapter.notifyDataSetChanged();
    }
}

