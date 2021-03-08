package com.scorpysmurf.recipie2.mainactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scorpysmurf.recipie2.MainActivity;
import com.scorpysmurf.recipie2.OnlineSavedRecipesActivity;
import com.scorpysmurf.recipie2.R;

import java.util.ArrayList;
import java.util.HashSet;

public class MealsFragment extends Fragment {

    View view;

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> groceryArray;
    Button addItemButton;
    FloatingActionButton resetButton;
    EditText groceryTxt;

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meals, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("com.scorpysmurf.recipie2.groceries", Context.MODE_PRIVATE);

        listView = view.findViewById(R.id.groceries_list_view);
        resetButton = view.findViewById(R.id.fab_clear_groceries);
        addItemButton = view.findViewById(R.id.button_add_grocery);
        groceryTxt = view.findViewById(R.id.edit_txt_new_grocery);

        if (sharedPreferences.getStringSet("groceries",null) == null) {
            groceryArray = new ArrayList<>();
        } else {
            groceryArray = new ArrayList<>(sharedPreferences.getStringSet("groceries",null));
        }

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, groceryArray);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.delete_grocery))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String deletedTitle = groceryArray.get(position);

                                groceryArray.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                updateGroceryList();

                                Toast.makeText(getActivity(), deletedTitle + " " + getString(R.string.deleted), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(getString(R.string.no),null)
                        .show();

                return true;
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.grocery_clearance))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                groceryArray.clear();
                                arrayAdapter.notifyDataSetChanged();
                                updateGroceryList();

                                Toast.makeText(getActivity(), getString(R.string.grocery_clear) , Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(getString(R.string.no),null)
                        .show();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textGrocery = groceryTxt.getText().toString().trim();

                if (textGrocery.isEmpty()) {
                    groceryTxt.setError(getString(R.string.field_cant_be_empty));
                } else {
                    groceryArray.add(textGrocery);
                    arrayAdapter.notifyDataSetChanged();
                    updateGroceryList();

                    groceryTxt.setText("");
                }
            }
        });

    }

    public void updateGroceryList() {
        HashSet<String> grocerySet = new HashSet<>(groceryArray);
        sharedPreferences.edit().putStringSet("groceries",grocerySet).apply();
    }

}