package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashSet;

public class OnlineSavedRecipesActivity extends AppCompatActivity {

    ListView listView;
    public static ArrayAdapter arrayAdapter;
    FloatingActionButton fabHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_saved_recipes);

        listView = findViewById(R.id.online_saves_list_view);
        fabHelp = findViewById(R.id.fab_help);

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
    }
}