package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

    TextView nameText, servingsText, prepText, cookText, directionText;
    ListView ingredientsListView, directionsListView;
    Button addIngredientBtn, addDirectionBtn, saveBtn;

    AutoCompleteTextView ingredientText;

    ConstraintLayout constraintLayout;

    private static final String[] INGREDIENTS = IngredientSuggestions.INGRIDIENTS;

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

        constraintLayout = findViewById(R.id.add_recipe_bg);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(),0);
            }
        });

        directionsAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,directions);
        ingredientsAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,ingredients);

        directionsListView.setAdapter(directionsAdapter);
        ingredientsListView.setAdapter(ingredientsAdapter);

        ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(RecipeEditorActivity.this, android.R.layout.simple_list_item_1,INGREDIENTS);
        ingredientText.setAdapter(autoAdapter);

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
                addIngredient();
            }
        });

        addIngredientBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER & event.getAction() == KeyEvent.ACTION_DOWN) {
                    addIngredient();
                }

                return false;
            }
        });

        addDirectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDirection();
            }
        });

        addDirectionBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER & event.getAction() == KeyEvent.ACTION_DOWN) {
                    addDirection();
                }

                return false;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validName() | !validCook() | !validPrep() | !validServings() | !validDirections() | !validIngredients()){

                    Toast.makeText(RecipeEditorActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();

                } else {

                    name = nameText.getText().toString().trim();
                    servings = servingsText.getText().toString().trim();
                    prep = prepText.getText().toString().trim();
                    cook = cookText.getText().toString().trim();

                    MyRecipesActivity.recipes.add(new Recipe(name, servings, prep, cook));
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

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.myrecipes", MODE_PRIVATE);
                    HashSet<String> nameSet = new HashSet<>(MyRecipesActivity.name);
                    HashSet<String> servingSet = new HashSet<>(MyRecipesActivity.servings);
                    HashSet<String> prepSet = new HashSet<>(MyRecipesActivity.prep);
                    HashSet<String> cookSet = new HashSet<>(MyRecipesActivity.cook);
                    HashSet<String> directionSet = new HashSet<>(MyRecipesActivity.directions);
                    HashSet<String> ingredientSet = new HashSet<>(MyRecipesActivity.ingredients);

                    sharedPreferences
                            .edit()
                            .putStringSet("rName", nameSet)
                            .putStringSet("rServings", servingSet)
                            .putStringSet("rPrep", prepSet)
                            .putStringSet("rCook", cookSet)
                            .putStringSet("rDirections", directionSet)
                            .putStringSet("rIngredients", ingredientSet)
                            .apply();

                    Toast.makeText(RecipeEditorActivity.this, name + " " + RecipeEditorActivity.this.getString(R.string.saved), Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });
    }

    private void addDirection() {
        String dirTxt = directionText.getText().toString().trim();
        if (dirTxt == null) {
            directions.add(directionText.getText().toString().trim());
            directionsAdapter.notifyDataSetChanged();
            directionText.setText("");
        } else {
            directionText.setError(getString(R.string.field_cant_be_empty));
        }
    }

    private void addIngredient() {
        String ingTxt = ingredientText.getText().toString().trim();
        if (ingTxt == null) {
            ingredients.add(ingredientText.getText().toString().trim());
            ingredientsAdapter.notifyDataSetChanged();
            ingredientText.setText("");
        } else {
            ingredientText.setError(getString(R.string.field_cant_be_empty));
        }
    }

    private boolean validName () {
        String txtName = nameText.getText().toString().trim();

        if(txtName.isEmpty()) {
            nameText.setError(getString(R.string.field_cant_be_empty));
            return false;
        }
        return true;

    }

    private boolean validServings () {
        String txtServings = servingsText.getText().toString().trim();

        if(txtServings.isEmpty()) {
            servingsText.setError(getString(R.string.field_cant_be_empty));
            return false;
        }
        return true;
    }

    private boolean validPrep () {
        String txtPrep = prepText.getText().toString().trim();

        if(txtPrep.isEmpty()) {
            prepText.setError(getString(R.string.field_cant_be_empty));
            return false;
        }
        return true;

    }

    private boolean validCook () {
        String txtCook = cookText.getText().toString().trim();

        if(txtCook.isEmpty()) {
            cookText.setError(getString(R.string.field_cant_be_empty));
            return false;
        }
        return true;
    }

    private boolean validIngredients () {
        ArrayList<String> txtIngredients = ingredients;

        if(txtIngredients.isEmpty()) {
            ingredientText.setError(getString(R.string.add_ingredients));
            return false;
        }
        return true;

    }

    private boolean validDirections () {
        ArrayList<String> txtDirections = directions;

        if(txtDirections.isEmpty()) {
            directionText.setError(getString(R.string.add_directions));
            return false;
        }
        return true;
    }

}