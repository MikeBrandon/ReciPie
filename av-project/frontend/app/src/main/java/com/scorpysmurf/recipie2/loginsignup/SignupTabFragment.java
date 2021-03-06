package com.scorpysmurf.recipie2.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scorpysmurf.recipie2.MainActivity;
import com.scorpysmurf.recipie2.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupTabFragment extends Fragment {

    EditText emailET, passET, confPassET, usernameET;
    Button signUp;
    private final static Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$");
    private final static Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z][A-Za-z0-9]*$");
    ConstraintLayout constraintLayout;

    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        emailET = root.findViewById(R.id.email);
        passET = root.findViewById(R.id.password);
        confPassET = root.findViewById(R.id.confirm_password);
        usernameET = root.findViewById(R.id.username);
        signUp = root.findViewById(R.id.sign_up_button);
        constraintLayout = root.findViewById(R.id.signup_bg);
        sharedPreferences = getActivity().getSharedPreferences("com.scorpysmurf.recipie2",Context.MODE_PRIVATE);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        fStore = FirebaseFirestore.getInstance();

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpMethod();

            }
        });

        confPassET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    signUpMethod();

                }

                return false;
            }
        });

        return root;
    }

    private void signUpMethod () {

        String emailInput = emailET.getText().toString().trim();
        String passInput = passET.getText().toString().trim();
        String uNameInput = usernameET.getText().toString().trim();

        validEmail();
        validPassword();
        validConfPassword();
        validUsername();

        if (!validEmail() | !validPassword() | !validConfPassword() | !validUsername()) {
            return;
        } else {

            mAuth.createUserWithEmailAndPassword(emailInput, passInput)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), getString(R.string.password_reset), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("OnFailure: ", e.getMessage());
                                    }
                                });

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Progress", "createUserWithEmail:success");

                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("uName",uNameInput);
                                user.put("email",emailInput);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Save Data:","Success");
                                    }
                                });

                                sharedPreferences.edit().putInt("loginType",1).apply();

                                nextAct();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Progress", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthException) {
                            if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                emailET.setError(getString(R.string.email_already_in_use));
                            }

                            Log.i("ERROR CODE: ", ((FirebaseAuthException) e).getErrorCode());
                        }
                    }
            });

        }
    }

    private void nextAct() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
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

    private boolean validPassword() {
        String passInput = passET.getText().toString().trim();

        if (passInput.isEmpty()) {
            passET.setError(getString(R.string.field_cant_be_empty));
            return false;
        } else if(passET.length()<6) {
            passET.setError(getString(R.string.short_pass));
            return false;
        } else if(!PASSWORD_PATTERN.matcher(passInput).matches()) {
            passET.setError(getString(R.string.weak_pass));
            return false;
        } else {
            passET.setError(null);
            return true;
        }
    }

    private boolean validConfPassword() {
        String passInput = confPassET.getText().toString().trim();

        if (passInput.isEmpty()) {
            confPassET.setError(getString(R.string.field_cant_be_empty));
            return false;
        } else if(confPassET.length()<6) {
            confPassET.setError(getString(R.string.short_pass));
            return false;
        } else if(!confPassET.getText().toString().equals(passET.getText().toString())) {
            confPassET.setError(getString(R.string.match_pass));
            return false;
        } else if(!PASSWORD_PATTERN.matcher(passInput).matches()) {
            confPassET.setError(getString(R.string.weak_pass));
            return false;
        } else {
            confPassET.setError(null);
            return true;
        }
    }

    private boolean validUsername() {
        String passInput = usernameET.getText().toString().trim();

        if (passInput.isEmpty()) {
            usernameET.setError(getString(R.string.field_cant_be_empty));
            return false;
        } else if (!USERNAME_PATTERN.matcher(passInput).matches()) {
            usernameET.setError(getString(R.string.no_symbol_or_spaces_allowed));
            return false;
        } else {
            usernameET.setError(null);
            return true;
        }
    }

}