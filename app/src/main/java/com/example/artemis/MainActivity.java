package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText addressBar;
    private WebView viewer;
    private String tempUrl;
    private String titleFromWebView;
    private String home;
    private TextView xross;
    private TextView hdr;
    private FavDialog favDialog;
    private ArrayList<String> blockedList;
    FavDatabaseHelper favDatabaseHelper;
    HPDatabaseHelper hpDatabaseHelper;
    BlackListDatabaseHelper blackListDatabaseHelper;
    CurrentStateDatabaseHelper currentStateDatabaseHelper;
    static final String savedUrl = "url";
    String password;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressBar = findViewById(R.id.addressBar);
        viewer = findViewById(R.id.viewer);
        xross = findViewById(R.id.clear);
        hdr = findViewById(R.id.textView7);
        favDialog = new FavDialog();
        blackListDatabaseHelper = new BlackListDatabaseHelper(this);
        blockedList = new ArrayList<>();
        getBlockedSites();
        xrossInvisible(null);
        favDatabaseHelper = new FavDatabaseHelper(this);
        hpDatabaseHelper = new HPDatabaseHelper(this);
        currentStateDatabaseHelper = new CurrentStateDatabaseHelper(this);
        viewer.setWebViewClient(new WebViewClient());
        viewer.getSettings().setUseWideViewPort(true);
        viewer.getSettings().setLoadWithOverviewMode(true);
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
            if (!Objects.equals(getFromFavs.getString(Intent.EXTRA_RETURN_RESULT), "")) {
                tempUrl = getFromFavs.getString(Intent.EXTRA_RETURN_RESULT);
            } else {
                tempUrl = retrieveFromCurrentStateDB();
            }
            addressBar.setText(tempUrl);
            go(null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");
        if(password.equals("")){
            // If there is no password
            addToCurrentStateDB(viewer.getUrl());
            Intent goSettings = new Intent(this, Passwords.class);
            goSettings.putExtra(Intent.EXTRA_REFERRER, 1);
            startActivity(goSettings);
            xrossInvisible(null);
            finish();
        } else {
            // If there is a password
            addToCurrentStateDB(viewer.getUrl());
            Intent goSettings2 = new Intent(this, EnterPassword.class);
            startActivity(goSettings2);
            xrossInvisible(null);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void go(final View v) {
        if (v != null) {
            filterUrl(addressBar.getText().toString());
        }
        viewer.loadUrl(tempUrl);
        xrossInvisible(null);
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isBlocked(url)) {
                    viewer.loadUrl("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg");
                }
            }
            @Override
            public void onPageFinished(WebView view1, String url) {
                super.onPageFinished(view1, url);
                if (!viewer.getUrl().equals("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg")) {
                    hdr.setText(viewer.getTitle());
                    addressBar.setText(viewer.getUrl());
                } else {
                    hdr.setText(R.string.not_allowed);
                    addressBar.getText().clear();
                }
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isBlocked(url)) {
                    viewer.loadUrl("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg");
                }
            }
            @Override
            public void onPageFinished(WebView view1, String url) {
                super.onPageFinished(view1, url);
                if (!viewer.getUrl().equals("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg")) {
                    hdr.setText(viewer.getTitle());
                    addressBar.setText(viewer.getUrl());
                } else {
                    hdr.setText(R.string.not_allowed);
                }
            }
        });
        xrossInvisible(null);
    }

    public void gobackPage(View v) {
        if (viewer.canGoBack()) {
            viewer.goBack();
            viewer.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (isBlocked(url)) {
                        viewer.loadUrl("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg");
                    }
                }
                @Override
                public void onPageFinished(WebView view1, String url) {
                    super.onPageFinished(view1, url);
                    if (!viewer.getUrl().equals("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg")) {
                        hdr.setText(viewer.getTitle());
                        addressBar.setText(viewer.getUrl());
                    } else {
                        hdr.setText(R.string.not_allowed);
                    }
                }
            });
            xrossInvisible(null);
        }
    }

    public void goForwardPage(View v) {
        if (viewer.canGoForward()) {
            viewer.goForward();
            viewer.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (isBlocked(url)) {
                        viewer.loadUrl("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg");
                    }
                }
                @Override
                public void onPageFinished(WebView view1, String url) {
                    super.onPageFinished(view1, url);
                    if (!viewer.getUrl().equals("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg")) {
                        hdr.setText(viewer.getTitle());
                        addressBar.setText(viewer.getUrl());
                    } else {
                        hdr.setText(R.string.not_allowed);
                    }
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
        String homePage = "";
        try {
            homePage = hpDatabaseHelper.getHomePage();
        } catch (Exception e) {

        }
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isBlocked(url)) {
                    viewer.loadUrl("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg");
                }
            }
            @Override
            public void onPageFinished(WebView view1, String url) {
                super.onPageFinished(view1, url);
                if (!viewer.getUrl().equals("https://i.ibb.co/ZL7FtBd/Webp-net-resizeimage.jpg")) {
                    hdr.setText(viewer.getTitle());
                    addressBar.setText(viewer.getUrl());
                } else {
                    hdr.setText(R.string.not_allowed);
                }
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

    public void getBlockedSites() {
        SQLiteDatabase db = blackListDatabaseHelper.getReadableDatabase();
        String selectString = "SELECT * FROM bl_table";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                blockedList.add(filterBlocked(cursor
                        .getString(cursor.getColumnIndex("URL"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public String filterBlocked(String s) {
        if (s.toLowerCase().startsWith("http://")) {
            return s.substring(7);
        } else {
            if (s.toLowerCase().startsWith("https://")) {
                return s.substring(8);
            }  else {
                if (s.toLowerCase().startsWith("http://www.")) {
                    return s.substring(11);
                } else {
                    if (s.toLowerCase().startsWith("https://www.")) {
                        return s.substring(12);
                    } else {
                        if (s.toLowerCase().startsWith("www.")) {
                            return s.substring(4);
                        } else {
                            return s;
                        }
                    }
                }
            }
        }
    }

    public boolean isBlocked(String s) {
        boolean result = false;
        for (int i = 0; i < blockedList.size(); i++) {
            if (s.contains(blockedList.get(i))) {
                result =  true;
                break;
            }
        }
        return result;
    }

    public void addToCurrentStateDB(String s) {
        SQLiteDatabase db = currentStateDatabaseHelper.getWritableDatabase();
        currentStateDatabaseHelper.clearTable(db);
        ContentValues values = new ContentValues();
        values.put("current", s);
        db.insert("current_state", null, values);
        db.close();
    }

    public String retrieveFromCurrentStateDB() {
        SQLiteDatabase db = currentStateDatabaseHelper.getReadableDatabase();
        String selectString = "SELECT * FROM current_state";
        String returnCurrent = "";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                returnCurrent =  cursor.getString(cursor.getColumnIndex("current"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnCurrent;
    }
}
