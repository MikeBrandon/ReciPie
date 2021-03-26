package com.scorpysmurf.recipie2.mainactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.scorpysmurf.recipie2.MyRecipesActivity;
import com.scorpysmurf.recipie2.OnlineSavedRecipesActivity;
import com.scorpysmurf.recipie2.ProfileActivity;
import com.scorpysmurf.recipie2.R;
import com.squareup.picasso.Picasso;

public class RecipesFragment extends Fragment {

    View view;
    FloatingActionButton fabDownloads;
    ImageView btnViewRecipes;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipes, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabDownloads = view.findViewById(R.id.recipes_floating_action_button);
        btnViewRecipes = view.findViewById(R.id.button_view_recipe);

        firebaseAuth = FirebaseAuth.getInstance();

        fabDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), OnlineSavedRecipesActivity.class);
                startActivity(intent);

            }
        });

        btnViewRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MyRecipesActivity.class);
                startActivity(intent);

            }
        });
    }
}