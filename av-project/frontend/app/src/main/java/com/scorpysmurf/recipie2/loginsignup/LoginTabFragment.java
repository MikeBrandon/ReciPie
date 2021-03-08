package com.scorpysmurf.recipie2.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.scorpysmurf.recipie2.ForgotPasswordActivity;
import com.scorpysmurf.recipie2.R;

import java.util.regex.Pattern;

public class LoginTabFragment extends Fragment {

    EditText email, pass;
    TextView forgotpass;
    Button login;
    float v = 0;

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

                validEmail();
                validPassword();

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

        return root;
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
