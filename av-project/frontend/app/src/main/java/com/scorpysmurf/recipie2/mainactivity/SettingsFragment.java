package com.scorpysmurf.recipie2.mainactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.scorpysmurf.recipie2.LoginSignupActivity;
import com.scorpysmurf.recipie2.MainActivity;
import com.scorpysmurf.recipie2.ProfileActivity;
import com.scorpysmurf.recipie2.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    View view;
    Locale myLocale;
    Button btnEn, btnFr, btnEs;
    Button btnAlm1, btnAlm2, btnAlm3, btnAlm4;
    SharedPreferences sharedPreferences;
    MediaPlayer mediaPlayer;
    TextView logoutText;
    int loginType;
    public static CallbackManager callbackManager;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnEn = view.findViewById(R.id.btn_en);
        btnEs = view.findViewById(R.id.btn_es);
        btnFr = view.findViewById(R.id.btn_fr);

        btnAlm1 = view.findViewById(R.id.btn_alarm_1);
        btnAlm2 = view.findViewById(R.id.btn_alarm_2);
        btnAlm3 = view.findViewById(R.id.btn_alarm_3);
        btnAlm4 = view.findViewById(R.id.btn_alarm_4);

        logoutText = view.findViewById(R.id.logout_text);
        callbackManager = CallbackManager.Factory.create();
        user = FirebaseAuth.getInstance().getCurrentUser();

        sharedPreferences = getActivity().getSharedPreferences("com.scorpysmurf.recipie2", Context.MODE_PRIVATE);
        loginType = sharedPreferences.getInt("loginType",0);

        if (loginType == 0) {
            logoutText.setText(getString(R.string.login));
        } else {
            logoutText.setText(getString(R.string.profile));
        }

        btnEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });

        btnEs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("es");
            }
        });

        btnFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("fr");
            }
        });

        btnAlm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.alm_default);
                mediaPlayer.start();
                sharedPreferences.edit().putInt("alm",1).apply();
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        btnAlm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.alm_buzzer);
                mediaPlayer.start();
                sharedPreferences.edit().putInt("alm",2).apply();
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        btnAlm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.alm_chime);
                mediaPlayer.start();
                sharedPreferences.edit().putInt("alm",3).apply();
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        btnAlm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.alm_jolly);
                mediaPlayer.start();
                sharedPreferences.edit().putInt("alm",4).apply();
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loginType == 0) {
                    Intent intent = new Intent(getActivity(), LoginSignupActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }
}