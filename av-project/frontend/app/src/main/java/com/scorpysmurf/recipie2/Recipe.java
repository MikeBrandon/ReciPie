package com.scorpysmurf.recipie2;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class Recipe {

    String name;
    String servings;
    String prep;
    String cook;
    ArrayList<String> directions;
    ArrayList<String> ingredients;
    int totalTime;

    public Recipe(String name, String servings, String prep, String cook) {
        this.name = name;
        this.servings = servings;
        this.prep = prep;
        this.cook = cook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getPrep() {
        return prep;
    }

    public void setPrep(String prep) {
        this.prep = prep;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public int getTotalTime() {
        totalTime = Integer.parseInt(prep) + Integer.parseInt(cook);
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void saveRecipe(String name, String description, String directions, String nutrition, String notes, int rating, int servings, int prep, int cook, int totalTime, SharedPreferences sharedPreferences) {

    }
}
