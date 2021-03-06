package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class RecipeViewActivity extends AppCompatActivity {

    ArrayList<Recipe> recipes;

    public static String name;
    public static String servings;
    public static String prep;
    public static String cook;

    public static int recipeID;

    String sDirections,sIngredients;

    public static ArrayList<String> directions;
    public static ArrayList<String> ingredients;
    public static ArrayAdapter directionAdapter;
    public static ArrayAdapter ingredientAdapter;

    int totalTime;

    public static TextView txtName, txtServings, txtPrep, txtCook, txtTotal;
    public static ListView listIngredient, listDirections;
    FloatingActionButton fabTimer, fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipe_view);

        Intent intent = getIntent();
        recipeID = intent.getIntExtra("recipeID",-1);

        txtName = findViewById(R.id.recipe_name_title);
        txtServings = findViewById(R.id.servings_text_recipe);
        txtPrep = findViewById(R.id.prep_text_recipe);
        txtCook = findViewById(R.id.cook_text_recipe);
        txtTotal = findViewById(R.id.total_recipe);
        listIngredient = findViewById(R.id.ingredients_list_view);
        listDirections = findViewById(R.id.directions_list_view);
        fabTimer = findViewById(R.id.fab_timer);
        fabDelete = findViewById(R.id.fab_delete);

        if(recipeID != -1) {
            name = MyRecipesActivity.name.get(recipeID);
            servings = MyRecipesActivity.servings.get(recipeID);
            prep = MyRecipesActivity.prep.get(recipeID);
            cook = MyRecipesActivity.cook.get(recipeID);
            sDirections = MyRecipesActivity.directions.get(recipeID);
            sIngredients = MyRecipesActivity.ingredients.get(recipeID);
            totalTime = Integer.parseInt(prep) + Integer.parseInt(cook);

            try {
                directions = (ArrayList<String>) ObjectSerializer.deserialize(sDirections);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                ingredients = (ArrayList<String>) ObjectSerializer.deserialize(sIngredients);
            } catch (IOException e) {
                e.printStackTrace();
            }

            txtName.setText(name);
            txtServings.setText(servings);
            txtPrep.setText(prep);
            txtCook.setText(cook);
            txtTotal.setText(String.valueOf(totalTime));

            directionAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,directions);
            ingredientAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,ingredients);

            listDirections.setAdapter(directionAdapter);
            listIngredient.setAdapter(ingredientAdapter);

            fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyRecipesActivity.name.remove(recipeID);
                    MyRecipesActivity.cook.remove(recipeID);
                    MyRecipesActivity.prep.remove(recipeID);
                    MyRecipesActivity.ingredients.remove(recipeID);
                    MyRecipesActivity.directions.remove(recipeID);
                    MyRecipesActivity.servings.remove(recipeID);

                    MyRecipesActivity.recipes.remove(recipeID);
                    MyRecipesActivity.adapter.notifyDataSetChanged();

                    MyRecipesActivity.nameAdapter.notifyDataSetChanged();
                    MyRecipesActivity.cookAdapter.notifyDataSetChanged();
                    MyRecipesActivity.prepAdapter.notifyDataSetChanged();
                    MyRecipesActivity.ingredientsAdapter.notifyDataSetChanged();
                    MyRecipesActivity.directionsAdapter.notifyDataSetChanged();
                    MyRecipesActivity.servingsAdapter.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.myrecipes",MODE_PRIVATE);

//                    sharedPreferences.edit().remove("rName").apply();
//                    sharedPreferences.edit().remove("rServings").apply();
//                    sharedPreferences.edit().remove("rPrep").apply();
//                    sharedPreferences.edit().remove("rCook").apply();
//                    sharedPreferences.edit().remove("rDirections").apply();
//                    sharedPreferences.edit().remove("rIngredients").apply();

                    sharedPreferences.edit().clear().apply();

                    sharedPreferences.edit()
                            .putStringSet("rName",new HashSet<String>(MyRecipesActivity.name))
                            .putStringSet("rServings",new HashSet<String>(MyRecipesActivity.servings))
                            .putStringSet("rPrep",new HashSet<String>(MyRecipesActivity.prep))
                            .putStringSet("rCook",new HashSet<String>(MyRecipesActivity.cook))
                            .putStringSet("rDirections",new HashSet<String>(MyRecipesActivity.directions))
                            .putStringSet("rIngredients",new HashSet<String>(MyRecipesActivity.ingredients))
                            .apply();

                    Toast.makeText(RecipeViewActivity.this, name + " " + getString(R.string.deleted), Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();

                        }
                    },2500);

                }
            });

            fabTimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}