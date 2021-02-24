package com.scorpysmurf.recipie2.loginsignup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.scorpysmurf.recipie2.R;

public class LoginTabFragment extends Fragment {

    EditText email, pass;
    TextView forgotpass;
    Button login;
    float v=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

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

        return root;
    }

}
