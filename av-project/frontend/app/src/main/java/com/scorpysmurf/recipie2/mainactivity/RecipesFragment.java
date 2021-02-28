package com.scorpysmurf.recipie2.mainactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scorpysmurf.recipie2.OnlineSavedRecipesActivity;
import com.scorpysmurf.recipie2.R;

public class RecipesFragment extends Fragment {

    View view;
    FloatingActionButton fabDownloads;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipes, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabDownloads = view.findViewById(R.id.recipes_floating_action_button);
        fabDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), OnlineSavedRecipesActivity.class);
                startActivity(intent);

            }
        });
    }
}