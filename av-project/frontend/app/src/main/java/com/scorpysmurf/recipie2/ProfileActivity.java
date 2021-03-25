package com.scorpysmurf.recipie2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.share.Share;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameTV, emailTV;
    ImageView profilePic;
    Button resetPassBtn, logoutBtn, deleteBtn;

    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    AccessToken accessToken;
    boolean isLoggedIn;

    SharedPreferences sharedPreferences;
    int loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        logoutBtn = findViewById(R.id.logout_button);
        deleteBtn = findViewById(R.id.delete_button);
        userNameTV = findViewById(R.id.username);
        emailTV = findViewById(R.id.email_text);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                userNameTV.setText(value.getString("uName"));
                emailTV.setText(value.getString("email"));

            }
        });

        sharedPreferences = getSharedPreferences("com.scorpysmurf.recipie2", Context.MODE_PRIVATE);

        loginType = sharedPreferences.getInt("loginType",0);

        if (loginType == 2) {
            deleteBtn.setEnabled(false);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null) {

                    accessToken = AccessToken.getCurrentAccessToken();
                    isLoggedIn = accessToken != null && !accessToken.isExpired();

                    if (isLoggedIn) {
                        LoginManager.getInstance().logOut();
                    }

                    FirebaseAuth.getInstance().signOut();

                    sharedPreferences.edit().putInt("loginType",0).apply();
                }

                Intent i = new Intent(new Intent(ProfileActivity.this,LoginSignupActivity.class));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null) {

                    new AlertDialog.Builder(ProfileActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getString(R.string.are_you_sure))
                            .setMessage(getString(R.string.delete_acc))
                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProfileActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    sharedPreferences.edit().putInt("loginType",0).apply();

                                                    Intent i = new Intent(new Intent(ProfileActivity.this,LoginSignupActivity.class));
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);

                                                }
                                            }, 2500);

                                        }
                                    });
                                }
                            })
                            .setNegativeButton(getString(R.string.no), null)
                            .show();

                }

            }
        });

    }
}