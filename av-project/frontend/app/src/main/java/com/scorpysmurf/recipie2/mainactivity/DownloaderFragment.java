package com.scorpysmurf.recipie2.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scorpysmurf.recipie2.MainActivity;
import com.scorpysmurf.recipie2.OnlineSavedRecipesActivity;
import com.scorpysmurf.recipie2.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class DownloaderFragment extends Fragment {
    View view;
    WebView webView;
    FloatingActionButton floatingActionButton,fabRefresh,fabHome;
    String currentUrl, latestUrl;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_downloader, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar);

        sharedPreferences = getActivity()
                .getApplicationContext()
                .getSharedPreferences("com.scorpysmurf.recipie2.mainactivity",Context.MODE_PRIVATE);


        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                currentUrl = url;

                sharedPreferences.edit().putString("lasturl",currentUrl).apply();

                progressBar.setAlpha(1);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setAlpha(0);
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(webView.canGoBack())
                            {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });

        latestUrl = sharedPreferences.getString("lasturl",null);

        if (latestUrl == null) {
            webView.loadUrl("https://www.allrecipes.com/");
        } else {
            webView.loadUrl(latestUrl);
        }

        floatingActionButton = view.findViewById(R.id.floating_action_button);
        fabHome = view.findViewById(R.id.fab_home);
        fabRefresh = view.findViewById(R.id.fab_refresh);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), OnlineSavedRecipesActivity.class);
                    startActivity(intent);

            }
        });

        floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String title = webView.getTitle();
                String url = currentUrl;

                MainActivity.urlTitles.add(title);
                MainActivity.urls.add(currentUrl);

                MainActivity.urlsArrayAdapter.notifyDataSetChanged();
                MainActivity.titlesArrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getActivity()
                        .getApplicationContext()
                        .getSharedPreferences("com.scorpysmurf.recipie2.mainactivity", Context.MODE_PRIVATE);
                HashSet<String> urlSet= new HashSet<>(MainActivity.urls);
                HashSet<String> titleSet = new HashSet<>(MainActivity.urlTitles);

                sharedPreferences.edit().putStringSet("urls",urlSet).apply();
                sharedPreferences.edit().putStringSet("titles",titleSet).apply();

                Toast.makeText(getActivity(), title + " " + getActivity().getString(R.string.saved), Toast.LENGTH_LONG).show();

                return true;
            }
        });

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("https://www.allrecipes.com/");
            }
        });

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(currentUrl);
            }
        });
    }
}