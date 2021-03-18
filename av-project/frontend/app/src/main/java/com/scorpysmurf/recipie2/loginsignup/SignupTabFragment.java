package com.scorpysmurf.recipie2.loginsignup;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.scorpysmurf.recipie2.R;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Pattern;

public class SignupTabFragment extends Fragment {

    EditText emailET, phoneET, passET, confPassET;
    Button signUp;
    private final static Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$");
    ConstraintLayout constraintLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        emailET = root.findViewById(R.id.email);
        phoneET = root.findViewById(R.id.mobile_number);
        passET = root.findViewById(R.id.password);
        confPassET = root.findViewById(R.id.confirm_password);
        signUp = root.findViewById(R.id.sign_up_button);
        constraintLayout = root.findViewById(R.id.signup_bg);

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

        validEmail();
        validPassword();
        validConfPassword();
        validPhone();

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

    private boolean validPhone() {
        String passInput = phoneET.getText().toString().trim();

        if (passInput.isEmpty()) {
            phoneET.setError(getString(R.string.field_cant_be_empty));
            return false;
        } else {
            phoneET.setError(null);
            return true;
        }
    }

}
