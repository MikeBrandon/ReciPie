package com.scorpysmurf.recipie2.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scorpysmurf.recipie2.LoginSignupActivity;
import com.scorpysmurf.recipie2.MainActivity;
import com.scorpysmurf.recipie2.R;

import java.util.Locale;
import java.util.Set;

public class SettingsFragment extends Fragment {

    View view;
    Locale myLocale;
    Button btnEn, btnFr, btnEs;
    Button btnAlm1, btnAlm2, btnAlm3, btnAlm4;
    SharedPreferences sharedPreferences;
    MediaPlayer mediaPlayer;
    TextView logoutText;

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

        sharedPreferences = getActivity().getSharedPreferences("com.scorpysmurf.recipie2", Context.MODE_PRIVATE);

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

                Intent intent = new Intent(getActivity(), LoginSignupActivity.class);
                startActivity(intent);
                getActivity().finish();

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