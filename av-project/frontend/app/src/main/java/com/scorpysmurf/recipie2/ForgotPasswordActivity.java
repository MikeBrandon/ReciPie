package com.scorpysmurf.recipie2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    Dialog dialog;
    EditText emailET;
    Button resetBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailET = findViewById(R.id.email);
        resetBtn = findViewById(R.id.sign_up_button);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = emailET.getText().toString().trim();

                if (!validEmail()) {
                    return;
                }

                sendEmail(text);

            }
        });
    }

    private boolean validEmail() {
        String emailInput = emailET.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailET.setError(getString(R.string.field_cant_be_empty));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailET.setError(getString(R.string.please_enter_a_valid_email_address));
            return false;
        }else {
            emailET.setError(null);
            return true;
        }
    }

    private void successPopup() {
        dialog = new Dialog(ForgotPasswordActivity.this);
        dialog.setContentView(R.layout.password_reset_popup);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },5000);
    }

    private void sendEmail(String text) {
        fAuth.sendPasswordResetEmail(text).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                successPopup();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof FirebaseAuthException) {
                    if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
                    }

                    Log.i("ERROR CODE: ", ((FirebaseAuthException) e).getErrorCode());

                }
            }
        });
    }
}