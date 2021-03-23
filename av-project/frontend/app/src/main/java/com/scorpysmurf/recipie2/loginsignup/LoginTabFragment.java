package com.scorpysmurf.recipie2.loginsignup;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scorpysmurf.recipie2.ForgotPasswordActivity;
import com.scorpysmurf.recipie2.MainActivity;
import com.scorpysmurf.recipie2.R;

import java.util.regex.Pattern;

public class LoginTabFragment extends Fragment {

    EditText email, pass;
    TextView forgotpass;
    Button login;
    float v = 0;
    ConstraintLayout constraintLayout;
    private FirebaseAuth mAuth;

    private final static Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.password);
        forgotpass = root.findViewById(R.id.forgot_password);
        login = root.findViewById(R.id.loginButton);
        constraintLayout = root.findViewById(R.id.login_bg);

        mAuth = FirebaseAuth.getInstance();

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
            }
        });

        email.setTranslationX(800);
        pass.setTranslationX(800);
        forgotpass.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        forgotpass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginMethod();

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

        pass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    loginMethod();

                }

                return false;
            }
        });

        return root;
    }

    private void loginMethod () {

        String emailInput = email.getText().toString().trim();
        String passInput = pass.getText().toString().trim();


        validEmail();
        validPassword();

        if (!validEmail() | !validPassword()) {
            return;
        } else {

            mAuth.signInWithEmailAndPassword(emailInput, passInput)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Progress", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                nextAct();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Progress", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
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
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError(getString(R.string.field_cant_be_empty));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError(getString(R.string.please_enter_a_valid_email_address));
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validPassword() {
        String passInput = pass.getText().toString().trim();

        if (passInput.isEmpty()) {
            pass.setError(getString(R.string.field_cant_be_empty));
            return false;
        } else if(pass.length()<6) {
            pass.setError(getString(R.string.short_pass));
            return false;
        }else if(!PASSWORD_PATTERN.matcher(passInput).matches()) {
            pass.setError(getString(R.string.weak_pass));
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

}
