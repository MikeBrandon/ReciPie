package com.scorpysmurf.recipie2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameTV, emailTV, verifyTV;
    ImageView profilePic;
    Button resetPassBtn, logoutBtn, deleteBtn;
    private final static Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$");

    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    AccessToken accessToken;
    boolean isLoggedIn;

    FloatingActionButton fabHelp;

    StorageReference storageReference, profileRef;

    SharedPreferences sharedPreferences;
    int loginType;

    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        logoutBtn = findViewById(R.id.logout_button);
        deleteBtn = findViewById(R.id.delete_button);
        userNameTV = findViewById(R.id.user_text);
        emailTV = findViewById(R.id.email_text);
        resetPassBtn = findViewById(R.id.reset_pass_button);
        verifyTV = findViewById(R.id.verify_text);
        profilePic = findViewById(R.id.imageView);
        fabHelp = findViewById(R.id.fab_help);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        profileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
                profilePic.setBackground(getDrawable(R.drawable.prof_pic2_bg));
            }
        });

        if (user.isEmailVerified()) {
            verifyTV.setVisibility(View.GONE);
        }

        documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                try {
                    userNameTV.setText(value.getString("uName"));
                    emailTV.setText(value.getString("email"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        sharedPreferences = getSharedPreferences("com.scorpysmurf.recipie2", Context.MODE_PRIVATE);

        loginType = sharedPreferences.getInt("loginType",0);

        if (loginType == 2) {
            deleteBtn.setEnabled(false);
            resetPassBtn.setEnabled(false);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);

            }
        });

        fabHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ProfileActivity.this, getString(R.string.click_profile), Toast.LENGTH_SHORT).show();

            }
        });

        verifyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileActivity.this, getString(R.string.password_reset), Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("OnFailure: ", e.getMessage());
                            }
                        });

            }
        });

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPass = new EditText(v.getContext());
                resetPass.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle(getString(R.string.reset_password));
                passwordResetDialog.setMessage(getString(R.string.enter_new_password));
                passwordResetDialog.setTitle(getString(R.string.reset_password));
                passwordResetDialog.setView(resetPass);

                passwordResetDialog.setPositiveButton(getString(R.string.yes),((dialog, which) -> {
                    String pass = resetPass.getText().toString();

                    if (!PASSWORD_PATTERN.matcher(pass).matches()) {

                        Toast.makeText(ProfileActivity.this, getString(R.string.weak_pass), Toast.LENGTH_SHORT).show();

                    } else {

                        user.updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                successPopup();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }))
                        .setNegativeButton(getString(R.string.no),(dialog, which) -> {

                        });

                passwordResetDialog.create().show();

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    }
                });

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
                                            sharedPreferences.edit().putInt("loginType",0).apply();

                                            DocumentReference toDelete = fStore.collection("users").document(userID);

                                            toDelete.delete();

                                            Intent i = new Intent(new Intent(ProfileActivity.this,LoginSignupActivity.class));
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            i.putExtra("deleted",1);
                                            startActivity(i);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {

                Uri imageUri = data.getData();
                uploadImage(imageUri);

            }
        }

    }

    private void uploadImage(Uri imageUri) {

        StorageReference fileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void successPopup() {
        Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.password_reset_pop);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },5000);
    }
}