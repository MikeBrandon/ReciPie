package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;

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

    Dialog dialog;
    Boolean countdownIsActive = false;
    CountDownTimer countDownTimer;

    ImageView cross;
    SeekBar slider;
    TextView timerText;
    Button btnAction;

    Intent timerServiceIntent;

    MediaPlayer mediaPlayer;

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
        dialog = new Dialog(this);

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

                    new AlertDialog.Builder(RecipeViewActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(RecipeViewActivity.this.getString(R.string.are_you_sure))
                            .setMessage(RecipeViewActivity.this.getString(R.string.delete_recipe))
                            .setPositiveButton(RecipeViewActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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
                            })
                            .setNegativeButton(RecipeViewActivity.this.getString(R.string.no),null)
                            .show();

                }
            });

            fabTimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.setContentView(R.layout.timer_pop_up);
                    cross = dialog.findViewById(R.id.img_cross_close);
                    slider = dialog.findViewById(R.id.timer_slider);
                    timerText = dialog.findViewById(R.id.txt_time);
                    btnAction = dialog.findViewById(R.id.btn_timer);

                    cross.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    slider.setMax(3600);
                    slider.setProgress(1800);

                    slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            updateTimer(progress,timerText);

                            if(progress == 0) {
                                btnAction.setVisibility(View.INVISIBLE);
                            } else {
                                btnAction.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    btnAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!countdownIsActive) {

                                countdownIsActive = true;
                                slider.setEnabled(false);
                                btnAction.setText(getString(R.string.stop));

                                timerServiceIntent = new Intent(RecipeViewActivity.this,TimerService.class);
                                timerServiceIntent.putExtra("time",slider.getProgress());
                                startService(timerServiceIntent);

                                countDownTimer = new CountDownTimer(slider.getProgress() * 1000,1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        updateTimer((int) millisUntilFinished/1000,timerText);

                                    }

                                    @Override
                                    public void onFinish() {

                                        timerText.setText(getString(R.string._00_00));

                                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2",MODE_PRIVATE);
                                        int alm = sharedPreferences.getInt("alm",1);

                                        switch (alm) {
                                            case 2:
                                                mediaPlayer = MediaPlayer.create(RecipeViewActivity.this,R.raw.alm_buzzer);
                                                break;
                                            case 3:
                                                mediaPlayer = MediaPlayer.create(RecipeViewActivity.this,R.raw.alm_chime);
                                                break;
                                            case 4:
                                                mediaPlayer = MediaPlayer.create(RecipeViewActivity.this,R.raw.alm_jolly);
                                                break;
                                            default:
                                                mediaPlayer = MediaPlayer.create(RecipeViewActivity.this,R.raw.alm_default);
                                        }

                                        mediaPlayer.start();

                                        countdownIsActive = false;
                                        slider.setEnabled(true);
                                        slider.setProgress(1800);
                                        btnAction.setText(getString(R.string.start));
                                        timerText.setText(getString(R.string._30_00));
                                        stopService(timerServiceIntent);

                                    }
                                }.start();

                            } else {

                                countdownIsActive = false;
                                slider.setEnabled(true);
                                slider.setProgress(1800);
                                btnAction.setText(getString(R.string.start));
                                countDownTimer.cancel();
                                timerText.setText(getString(R.string._30_00));
                                stopService(timerServiceIntent);

                            }
                        }
                    });

                    dialog.show();
                }
            });
        }
    }

    private void updateTimer(int progress, TextView timerText) {

        int min = progress / 60;
        int sec = progress - min*60;

        String stSec = zeroFix(sec);
        String stMin = zeroFix(min);

        timerText.setText(stMin + getString(R.string.cln) + stSec);

    }

    private String zeroFix(int val) {
        if (val < 10) {
            return  getString(R.string._0) + val;
        } else {
            return Integer.toString(val);
        }
    }
}