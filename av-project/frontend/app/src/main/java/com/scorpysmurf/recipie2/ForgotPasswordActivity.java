package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    Dialog dialog;
    EditText emailET, passET, confPassET;
    Button resetBtn;

    private final static Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailET = findViewById(R.id.email);
        passET = findViewById(R.id.password);
        confPassET = findViewById(R.id.confirm_password);
        resetBtn = findViewById(R.id.sign_up_button);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validEmail() & validPassword() & validConfPassword()) {
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
}