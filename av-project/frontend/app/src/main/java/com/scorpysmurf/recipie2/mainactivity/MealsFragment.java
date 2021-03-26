package com.scorpysmurf.recipie2.mainactivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.scorpysmurf.recipie2.IngredientSuggestions;
import com.scorpysmurf.recipie2.ProfileActivity;
import com.scorpysmurf.recipie2.R;
import com.scorpysmurf.recipie2.ReminderBroadcastReceiver;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

public class MealsFragment extends Fragment {

    View view;

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> groceryArray;
    Button addItemButton;
    FloatingActionButton resetButton;
    AutoCompleteTextView groceryTxt;
    ConstraintLayout constraintLayout;
    int waitTimeInHours = 18;
    ImageView profileBtn;
    FirebaseAuth firebaseAuth;

    SharedPreferences sharedPreferences;

    private static final String[] INGREDIENTS = IngredientSuggestions.INGRIDIENTS;

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
        constraintLayout = view.findViewById(R.id.groceries_bg);
        profileBtn = view.findViewById(R.id.profile_pic);

        firebaseAuth = FirebaseAuth.getInstance();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileBtn);
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,INGREDIENTS);
        groceryTxt.setAdapter(autoAdapter);

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

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(getActivity(), getString(R.string.logged_in), Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(i);
                }

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

                addItem();

            }
        });

        groceryTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    addItem();

                }

                return false;
            }
        });

    }

    private void addItem() {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Intent intent = new Intent(getActivity(), ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),0,intent,0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        long timeAtActivityExit = System.currentTimeMillis();

        long waitTimeMillis = waitTimeInHours * 3600 * 1000;

        alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtActivityExit+waitTimeMillis,pendingIntent);

    }

    public void updateGroceryList() {
        HashSet<String> grocerySet = new HashSet<>(groceryArray);
        sharedPreferences.edit().putStringSet("groceries",grocerySet).apply();
    }

}