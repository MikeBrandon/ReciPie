package com.scorpysmurf.recipie2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class OnlineSavedRecipesActivity extends AppCompatActivity {

    ListView listView;
    public static ArrayAdapter arrayAdapter;
    FloatingActionButton fabHelp, fabDownload, fabUpload;
    DocumentReference documentReference;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_saved_recipes);

        listView = findViewById(R.id.online_saves_list_view);
        fabHelp = findViewById(R.id.fab_help);
        fabDownload = findViewById(R.id.fab_download);
        fabUpload = findViewById(R.id.fab_upload);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        documentReference = fStore.collection("users").document(user.getUid());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Animation animation = android.view.animation.AnimationUtils.loadAnimation(fabHelp.getContext(),R.anim.shake);
                animation.setDuration(200L);
                fabHelp.startAnimation(animation);

            }
        },5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Animation animation = android.view.animation.AnimationUtils.loadAnimation(fabHelp.getContext(),R.anim.shake);
                animation.setDuration(200L);
                fabHelp.startAnimation(animation);

            }
        },10000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Animation animation = android.view.animation.AnimationUtils.loadAnimation(fabHelp.getContext(),R.anim.shake);
                animation.setDuration(200L);
                fabHelp.startAnimation(animation);

            }
        },15000);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,MainActivity.urlTitles);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),OnlineRecipeViewActivity.class);
                intent.putExtra("recipeID",position);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(OnlineSavedRecipesActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(OnlineSavedRecipesActivity.this.getString(R.string.are_you_sure))
                        .setMessage(OnlineSavedRecipesActivity.this.getString(R.string.delete_recipe))
                        .setPositiveButton(OnlineSavedRecipesActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String deletedTitle = MainActivity.urlTitles.get(position);

                                MainActivity.urls.remove(position);
                                MainActivity.urlTitles.remove(position);
                                MainActivity.urlsArrayAdapter.notifyDataSetChanged();
                                MainActivity.titlesArrayAdapter.notifyDataSetChanged();
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.mainactivity", Context.MODE_PRIVATE);
                                HashSet<String> urlSet= new HashSet<>(MainActivity.urls);
                                HashSet<String> titleSet = new HashSet<>(MainActivity.urlTitles);

                                sharedPreferences.edit().putStringSet("urls",urlSet).apply();
                                sharedPreferences.edit().putStringSet("titles",titleSet).apply();

                                Toast.makeText(OnlineSavedRecipesActivity.this, deletedTitle + " " + OnlineSavedRecipesActivity.this.getString(R.string.deleted), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(OnlineSavedRecipesActivity.this.getString(R.string.no),null)
                        .show();

                return true;
            }
        });

        fabHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OnlineSavedRecipesActivity.this, OnlineSavedRecipesActivity.this.getString(R.string.long_to_delete), Toast.LENGTH_SHORT).show();
            }
        });

        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uName = user.getUid();
                String email = user.getEmail();

                Map<String,Object> userUploadObject = new HashMap<>();
                userUploadObject.put("uName",uName);
                userUploadObject.put("email",email);

                String[] titleArray = MainActivity.urlTitles.toArray(new String[0]);
                String[] urlArray = MainActivity.urls.toArray(new String[0]);

                userUploadObject.put("urlTitles", titleArray);
                userUploadObject.put("urls", urlArray);

                documentReference.set(userUploadObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OnlineSavedRecipesActivity.this, getString(R.string.updated), Toast.LENGTH_SHORT).show();
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

                        MainActivity.urls = (ArrayList<String>) value.get("urls");
                        MainActivity.urlTitles = (ArrayList<String>) value.get("urlTitles");

                        MainActivity.urlsArrayAdapter.notifyDataSetChanged();
                        MainActivity.titlesArrayAdapter.notifyDataSetChanged();
                        arrayAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(OnlineSavedRecipesActivity.this,OnlineSavedRecipesActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.mainactivity", Context.MODE_PRIVATE);
                HashSet<String> urlSet= new HashSet<>(MainActivity.urls);
                HashSet<String> titleSet = new HashSet<>(MainActivity.urlTitles);

                sharedPreferences.edit().putStringSet("urls",urlSet).apply();
                sharedPreferences.edit().putStringSet("titles",titleSet).apply();

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Toast.makeText(OnlineSavedRecipesActivity.this, getString(R.string.updated), Toast.LENGTH_SHORT).show();

    }
}