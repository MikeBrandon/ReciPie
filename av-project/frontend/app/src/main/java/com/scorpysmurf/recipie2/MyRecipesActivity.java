package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;

public class MyRecipesActivity extends AppCompatActivity implements RecipeAdapter.ClickListener {

    RecyclerView myRecipesRecyclerView;
    public static RecyclerView.Adapter adapter;
    public static ArrayList<Recipe> recipes;
    public static Recipe selected;

    public static ArrayList<String> name, servings, prep, cook;
    public static ArrayList<String> directions, ingredients;
    public static ArrayAdapter nameAdapter, servingsAdapter, prepAdapter, cookAdapter;
    public static ArrayAdapter directionsAdapter, ingredientsAdapter;

    FloatingActionButton fabAddNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_recipes);

        myRecipesRecyclerView = findViewById(R.id.my_recipes_recycler_view);
        fabAddNew = findViewById(R.id.fab_add_new);

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRecipesActivity.this, RecipeEditorActivity.class);
                startActivity(intent);
            }
        });

        recipeDataControl();

        myRecipesRecyclerView();

    }

    private void recipeDataControl() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.myrecipes", Context.MODE_PRIVATE);
        HashSet<String> nameSet = (HashSet<String>) sharedPreferences.getStringSet("rName",null);
        HashSet<String> servingSet = (HashSet<String>) sharedPreferences.getStringSet("rServings",null);
        HashSet<String> prepSet = (HashSet<String>) sharedPreferences.getStringSet("rPrep",null);
        HashSet<String> cookSet = (HashSet<String>) sharedPreferences.getStringSet("rCook",null);
        HashSet<String> directionSet = (HashSet<String>) sharedPreferences.getStringSet("rDirections",null);
        HashSet<String> ingredientSet = (HashSet<String>) sharedPreferences.getStringSet("rIngredients",null);

        if (nameSet == null) {
            name = new ArrayList<>();
            servings = new ArrayList<>();
            prep = new ArrayList<>();
            cook = new ArrayList<>();
            directions = new ArrayList<>();
            ingredients = new ArrayList<>();
        } else {
            name = new ArrayList<String>(nameSet);
            servings = new ArrayList<String>(servingSet);
            prep = new ArrayList<String>(prepSet);
            cook = new ArrayList<String>(cookSet);
            directions = new ArrayList<String>(directionSet);
            ingredients = new ArrayList<String>(ingredientSet);
        }

        nameAdapter = new ArrayAdapter(this,R.layout.recipe_recycler_design,name);
        servingsAdapter = new ArrayAdapter(this,R.layout.recipe_recycler_design,servings);
        prepAdapter = new ArrayAdapter(this,R.layout.recipe_recycler_design,prep);
        cookAdapter = new ArrayAdapter(this,R.layout.recipe_recycler_design,cook);
        directionsAdapter = new ArrayAdapter(this,R.layout.recipe_recycler_design,directions);
        ingredientsAdapter = new ArrayAdapter(this,R.layout.recipe_recycler_design,ingredients);

    }

    private void myRecipesRecyclerView() {
        myRecipesRecyclerView.setHasFixedSize(true);
        myRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        recipes = new ArrayList<>();

        for(int i = 0;i<name.size();i++) {
            recipes.add(new Recipe(name.get(i),servings.get(i),prep.get(i),cook.get(i)));
        }

        adapter = new RecipeAdapter(recipes,this);
        myRecipesRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(int position) {
        selected = recipes.get(position);
        Intent intent = new Intent(this, RecipeViewActivity.class);
        intent.putExtra("recipeID",recipes.indexOf(selected));
        startActivity(intent);
    }
}