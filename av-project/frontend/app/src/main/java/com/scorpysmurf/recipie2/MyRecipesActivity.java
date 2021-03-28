package com.scorpysmurf.recipie2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MyRecipesActivity extends AppCompatActivity implements RecipeAdapter.ClickListener {

    RecyclerView myRecipesRecyclerView;
    public static RecyclerView.Adapter adapter;
    public static ArrayList<Recipe> recipes;
    public static Recipe selected;

    public static ArrayList<String> name, servings, prep, cook;
    public static ArrayList<String> directions, ingredients;
    public static ArrayAdapter nameAdapter, servingsAdapter, prepAdapter, cookAdapter;
    public static ArrayAdapter directionsAdapter, ingredientsAdapter;

    FloatingActionButton fabAddNew, fabDownload, fabUpload;
    DocumentReference documentReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    ArrayList<String> urlArray;
    ArrayList<String> titleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_recipes);

        myRecipesRecyclerView = findViewById(R.id.my_recipes_recycler_view);
        fabAddNew = findViewById(R.id.fab_add_new);
        fabUpload = findViewById(R.id.fab_upload);
        fabDownload = findViewById(R.id.fab_download);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        documentReference = fStore.collection("recipes").document(user.getUid());

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRecipesActivity.this, RecipeEditorActivity.class);
                startActivity(intent);
            }
        });

        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> userUploadObject = new HashMap<>();
                userUploadObject.put("rpNames", Arrays.asList(name.toArray(new String[0])));
                userUploadObject.put("rpServings", Arrays.asList(servings.toArray(new String[0])));
                userUploadObject.put("rpPrep", Arrays.asList(prep.toArray(new String[0])));
                userUploadObject.put("rpCook", Arrays.asList(cook.toArray(new String[0])));
                userUploadObject.put("rpDirections", Arrays.asList(directions.toArray(new String[0])));
                userUploadObject.put("rpIngredients", Arrays.asList(ingredients.toArray(new String[0])));

                documentReference.set(userUploadObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MyRecipesActivity.this, getString(R.string.uploaded), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyRecipesActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        name = (ArrayList<String>) value.get("rpNames");
                        servings = (ArrayList<String>) value.get("rpServings");
                        prep = (ArrayList<String>) value.get("rpPrep");
                        cook = (ArrayList<String>) value.get("rpCook");
                        directions = (ArrayList<String>) value.get("rpDirections");
                        ingredients = (ArrayList<String>) value.get("rpIngredients");

                        try {
                            myRecipesRecyclerView();

                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.myrecipes", MODE_PRIVATE);
                            HashSet<String> nameSet = new HashSet<>(name);
                            HashSet<String> servingSet = new HashSet<>(servings);
                            HashSet<String> prepSet = new HashSet<>(prep);
                            HashSet<String> cookSet = new HashSet<>(cook);
                            HashSet<String> directionSet = new HashSet<>(directions);
                            HashSet<String> ingredientSet = new HashSet<>(ingredients);

                            sharedPreferences
                                    .edit()
                                    .putStringSet("rName", nameSet)
                                    .putStringSet("rServings", servingSet)
                                    .putStringSet("rPrep", prepSet)
                                    .putStringSet("rCook", cookSet)
                                    .putStringSet("rDirections", directionSet)
                                    .putStringSet("rIngredients", ingredientSet)
                                    .apply();
                        } catch (NullPointerException e){
                            Toast.makeText(MyRecipesActivity.this, getText(R.string.you_dont_have_any_saved_recipes), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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