package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText addressBar;
    private WebView viewer;
    private String tempUrl;
    private String titleFromWebView;
    private String home;
    private TextView xross;
    private TextView hdr;
    private FavDialog favDialog;
    FavDatabaseHelper favDatabaseHelper;
    HPDatabaseHelper hpDatabaseHelper;
    static final String savedUrl = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressBar = findViewById(R.id.addressBar);
        viewer = findViewById(R.id.viewer);
        xross = findViewById(R.id.clear);
        hdr = findViewById(R.id.textView7);
        favDialog = new FavDialog();
        xrossInvisible(null);
        favDatabaseHelper = new FavDatabaseHelper(this);
        hpDatabaseHelper = new HPDatabaseHelper(this);
        viewer.setWebViewClient(new WebViewClient());
        viewer.getSettings().setUseWideViewPort(true);
        viewer.getSettings().setJavaScriptEnabled(true);
        if (savedInstanceState != null) {
            home = savedInstanceState.getString(savedUrl);
        }
        addressBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    filterUrl(addressBar.getText().toString());
                    go(null);

                }

                return false;
            }
        });
        Bundle getFromFavs = getIntent().getExtras();
        if (getFromFavs != null) {
            tempUrl = getFromFavs.getString(Intent.EXTRA_RETURN_RESULT);
            addressBar.setText(tempUrl);
            go(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void openDialog() {
        refresh(null);
        favDialog.setUrl(viewer.getUrl());
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                favDialog.setTitle(viewer.getTitle());
                titleFromWebView = viewer.getTitle();
                favDialog.show(getSupportFragmentManager(), "fav dialog");
                final FragmentManager fm = favDialog.getFragmentManager();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fm.executePendingTransactions();
                        favDialog.getDialog()
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (favDialog.getResult()) {
                                     if (favDialog.isHp()) {
                                     try {
                                     hpDatabaseHelper.rePopulate(viewer.getUrl());
                                     } catch (Exception e) {
                                     hpDatabaseHelper.addData(viewer.getUrl());
                                     }
                                     }

                                     Handler handler1 = new Handler();
                                     handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                    home = hpDatabaseHelper.getHomePage();
                                    }
                                    },200);
                                    Intent intent = new Intent(MainActivity.this,
                                            Favourites.class);
                                    intent.putExtra(Intent.EXTRA_TITLE, titleFromWebView);
                                    intent.putExtra(Intent.EXTRA_PROCESS_TEXT, viewer.getUrl());
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }, 200);
            }
        });
    }


    public void settings(View v) {
        Intent goSettings = new Intent(this, Settings.class);
        startActivity(goSettings);
        xrossInvisible(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void go(View v) {
        if (v != null) {
            filterUrl(addressBar.getText().toString());
        }
        viewer.loadUrl(tempUrl);
        xrossInvisible(null);
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view1, String url) {
                super.onPageFinished(view1, url);
                hdr.setText(viewer.getTitle());
            }
        });
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager inm = (InputMethodManager)
                    getSystemService((Activity.INPUT_METHOD_SERVICE));
            if (inm != null) {
                inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void refresh(View v) {
        viewer.reload();
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hdr.setText(viewer.getTitle());
            }
        });
        xrossInvisible(null);
    }

    public void gobackPage(View v) {
        if (viewer.canGoBack()) {
            viewer.goBack();
            viewer.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    hdr.setText(viewer.getTitle());
                }
            });
            xrossInvisible(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void favorite(View v) {
        openDialog();
    }

    public void homeP(View v) {
        String homePage = hpDatabaseHelper.getHomePage();
        if (homePage.equals("")) {
            try {
                SharedPreferences getSavedHP;
                getSavedHP = getSharedPreferences("SAVED_URL", Context.MODE_PRIVATE);
                home = getSavedHP.getString("MEM-URL", "");
            } catch (Exception e) {
            }
        } else {
            home = homePage;
        }
        viewer.loadUrl(home);
        addressBar.setText(home);
        xrossInvisible(null);
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hdr.setText(viewer.getTitle());
            }
        });
    }

    public String filterUrl(String link) {
        int count = 0;
        tempUrl = link;
        tempUrl = addressBar.getText().toString();
        if (tempUrl.toLowerCase().startsWith("http://")) {
            tempUrl.substring(7);
        }

        if (tempUrl.toLowerCase().startsWith("https://")) {
            tempUrl.substring(9);
        }

        for (int i = 0; i < tempUrl.length(); i++) {
            if (String.valueOf(tempUrl.charAt(i)).equals(".")) {
                count++;
            }
            if (String.valueOf(tempUrl.charAt(i)).equals(" ")) {
                count = 0;
                break;
            }
        }

        if (count == 0) {
            tempUrl = "www.google.com/#q=" + tempUrl;
        } else {
            if (!tempUrl.toLowerCase().startsWith("ww")) {
                tempUrl = "www." + tempUrl;
            }
        }
        tempUrl = "http://" + tempUrl;
        return tempUrl;
    }

    public void clearAddressBar(View v) {
        addressBar.getText().clear();
    }

    public void xrossVisible(View v) {
        xross.animate().alpha(1.0f).setDuration(0);
    }

    public void xrossInvisible(View v) {
        xross.animate().alpha(0.0f).setDuration(0);
    }
}
