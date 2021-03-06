package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class RecipeEditorActivity extends AppCompatActivity {

    String name;
    String servings;
    String prep;
    String cook;

    ArrayList<String> directions = new ArrayList<>();
    ArrayList<String> ingredients = new ArrayList<>();
    ArrayAdapter directionsAdapter;
    ArrayAdapter ingredientsAdapter;

    TextView nameText, servingsText, prepText, cookText, ingredientText, directionText;
    ListView ingredientsListView, directionsListView;
    Button addIngredientBtn, addDirectionBtn, saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipe_editor);

        nameText = findViewById(R.id.edit_txt_name);
        servingsText = findViewById(R.id.edit_txt_servings);
        prepText = findViewById(R.id.edit_txt_prep);
        cookText = findViewById(R.id.edit_txt_cook);
        directionText = findViewById(R.id.edit_txt_new_direction);
        ingredientText = findViewById(R.id.edit_txt_new_ingredient);

        ingredientsListView = findViewById(R.id.list_view_ingredients);
        directionsListView = findViewById(R.id.list_view_directions);

        addDirectionBtn = findViewById(R.id.button_add_direction);
        addIngredientBtn = findViewById(R.id.button_add_ingredient);
        saveBtn = findViewById(R.id.button_save);

        directionsAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,directions);
        ingredientsAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,ingredients);

        directionsListView.setAdapter(directionsAdapter);
        ingredientsListView.setAdapter(ingredientsAdapter);

        directionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(RecipeEditorActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(RecipeEditorActivity.this.getString(R.string.are_you_sure))
                        .setMessage(RecipeEditorActivity.this.getString(R.string.delete_recipe))
                        .setPositiveButton(RecipeEditorActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String deletedDirection = directions.get(position);

                                directions.remove(position);
                                directionsAdapter.notifyDataSetChanged();

                                Toast.makeText(RecipeEditorActivity.this, deletedDirection + " " + RecipeEditorActivity.this.getString(R.string.deleted), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(RecipeEditorActivity.this.getString(R.string.no),null)
                        .show();

                return true;
            }
        });

        ingredientsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(RecipeEditorActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(RecipeEditorActivity.this.getString(R.string.are_you_sure))
                        .setMessage(RecipeEditorActivity.this.getString(R.string.delete_recipe))
                        .setPositiveButton(RecipeEditorActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String deletedDirection = ingredients.get(position);

                                ingredients.remove(position);
                                ingredientsAdapter.notifyDataSetChanged();

                                Toast.makeText(RecipeEditorActivity.this, deletedDirection + " " + RecipeEditorActivity.this.getString(R.string.deleted), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(RecipeEditorActivity.this.getString(R.string.no),null)
                        .show();

                return true;
            }
        });

        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.add(String.valueOf(ingredientText.getText()));
                ingredientsAdapter.notifyDataSetChanged();
                ingredientText.setText("");
            }
        });

        addDirectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directions.add(String.valueOf(directionText.getText()));
                directionsAdapter.notifyDataSetChanged();
                directionText.setText("");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = "" + nameText.getText();
                servings = "" + servingsText.getText();
                prep = "" + prepText.getText();
                cook = "" + cookText.getText();

                MyRecipesActivity.recipes.add(new Recipe(name,servings, prep, cook));
                MyRecipesActivity.adapter.notifyDataSetChanged();

                String sDirections = null, sIngredients = null;

                try {
                    sDirections = ObjectSerializer.serialize(directions);
                    sIngredients = ObjectSerializer.serialize(ingredients);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MyRecipesActivity.name.add(name);
                MyRecipesActivity.servings.add(servings);
                MyRecipesActivity.prep.add(prep);
                MyRecipesActivity.cook.add(cook);
                MyRecipesActivity.directions.add(sDirections);
                MyRecipesActivity.ingredients.add(sIngredients);

                MyRecipesActivity.nameAdapter.notifyDataSetChanged();
                MyRecipesActivity.servingsAdapter.notifyDataSetChanged();
                MyRecipesActivity.prepAdapter.notifyDataSetChanged();
                MyRecipesActivity.cookAdapter.notifyDataSetChanged();
                MyRecipesActivity.directionsAdapter.notifyDataSetChanged();
                MyRecipesActivity.ingredientsAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.myrecipes",MODE_PRIVATE);
                HashSet<String> nameSet = new HashSet<>(MyRecipesActivity.name);
                HashSet<String> servingSet = new HashSet<>(MyRecipesActivity.servings);
                HashSet<String> prepSet = new HashSet<>(MyRecipesActivity.prep);
                HashSet<String> cookSet = new HashSet<>(MyRecipesActivity.cook);
                HashSet<String> directionSet = new HashSet<>(MyRecipesActivity.directions);
                HashSet<String> ingredientSet = new HashSet<>(MyRecipesActivity.ingredients);

                sharedPreferences
                        .edit()
                        .putStringSet("rName",nameSet)
                        .putStringSet("rServings",servingSet)
                        .putStringSet("rPrep",prepSet)
                        .putStringSet("rCook",cookSet)
                        .putStringSet("rDirections",directionSet)
                        .putStringSet("rIngredients",ingredientSet)
                        .apply();

                Toast.makeText(RecipeEditorActivity.this, name + " " + RecipeEditorActivity.this.getString(R.string.saved), Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }
}