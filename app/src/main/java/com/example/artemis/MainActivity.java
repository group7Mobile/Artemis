package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText addressBar;
    private WebView viewer;
    private String tempUrl;
    private String dialogUrl;
    private String titleFromWebView;
    private String home;
    private TextView xross;
    private TextView hdr;
    private FavDialog favDialog;
    private Filters filter;
    FilterWordsDBhelper filterWords;
    private ProgressBar pg;
    private ArrayList<String> blockedList;
    private ConstraintLayout constraintLayout; //for background colour
    FavDatabaseHelper favDatabaseHelper;
    HPDatabaseHelper hpDatabaseHelper;
    BlackListDatabaseHelper blackListDatabaseHelper;
    CurrentStateDatabaseHelper currentStateDatabaseHelper;
    private long backPressedTime;
    HistoryDBHelper historyDBHelper;
    static final String savedUrl = "url";
    String password;
    private ArrayList<String> filterWordsList;
    private String favouriteClicked;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressBar = findViewById(R.id.addressBar);
        viewer = findViewById(R.id.viewer);
        xross = findViewById(R.id.clear);
        hdr = findViewById(R.id.textView7);
        pg = findViewById(R.id.progressBar);
        pg.setVisibility(ProgressBar.GONE);
        favDialog = new FavDialog();
        dialogUrl = "";
        blackListDatabaseHelper = new BlackListDatabaseHelper(this);
        blockedList = new ArrayList<>();
        blockedList = blackListDatabaseHelper.retrieveLinksFromDatabase();
        xrossInvisible(null);
        favDatabaseHelper = new FavDatabaseHelper(this);
        hpDatabaseHelper = new HPDatabaseHelper(this);
        currentStateDatabaseHelper = new CurrentStateDatabaseHelper(this);
        historyDBHelper = new HistoryDBHelper(this);
        filterWords = new FilterWordsDBhelper(this);
        filterWordsList=new ArrayList<String>();
        viewer.setWebViewClient(new WebViewClient());
        Bundle getStateWebPage = getIntent().getExtras();
        if (getStateWebPage != null && getStateWebPage.getString(Intent.EXTRA_TEXT) != null) {
            tempUrl = retrieveFromCurrentStateDB();
            go(null);
        }
        viewer.setWebChromeClient(new ChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && pg.getVisibility() == ProgressBar.GONE) {
                    pg.setVisibility(ProgressBar.VISIBLE);
                }
                pg.setProgress(progress);
                if (progress == 100) {
                    pg.setVisibility(ProgressBar.GONE);
                }
            }

            public void onReceivedTitle (WebView view, String title) {
                hdr.setText(title);
                addressBar.setText(viewer.getUrl());
            }
        });
        viewer.getSettings().setUseWideViewPort(true);
        viewer.getSettings().setLoadWithOverviewMode(true);
        viewer.getSettings().setJavaScriptEnabled(true);
        viewer.getSettings().setBuiltInZoomControls(true);
        viewer.getSettings().setDisplayZoomControls(false);
        constraintLayout = findViewById(R.id.constraintLayout);
        //Gets the background theme from SharedPreferences:
        SharedPreferences sharedPref = getSharedPreferences("bg", Context.MODE_PRIVATE);
        int savedBg = sharedPref.getInt("bg", R.color.colorBG1);
        constraintLayout.setBackgroundColor(ContextCompat.getColor(this, savedBg));

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
        /////////////////////////////
/*        Bundle getFromFavs = getIntent().getExtras();
        if (getFromFavs != null) {
            if (!Objects.equals(getFromFavs.getString(Intent.EXTRA_RETURN_RESULT), "")) {
                tempUrl = getFromFavs.getString(Intent.EXTRA_RETURN_RESULT);
            } else {
                tempUrl = retrieveFromCurrentStateDB();
            }
            addressBar.setText(tempUrl);
            go(null);
        }*/
        //Check if a favourite was clicked and if so, go to favourite:
        goToFavourite();
        getBlockedSites();
        getFilterWords();
        ProfanityFilter.loadStaticList();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewer.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewer.restoreState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        getFilterWords();
        if (isFullScreen()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            if (viewer.canGoBack()) {
                gobackPage(null);
            } else {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                } else {
                    Toast.makeText(getBaseContext(), "Press back again to exit",
                            Toast.LENGTH_SHORT).show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void openDialog() {
        favDialog.setUrl(dialogUrl);
        favDialog.setTitle(hdr.getText().toString());
        titleFromWebView = hdr.getText().toString();
        favDialog.show(getSupportFragmentManager(), "fav dialog");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                favDialog.getDialog()
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (favDialog.getResult()) {
                             if (favDialog.isHp()) {
                                 addToHPDB(dialogUrl);
                             }
                             Handler handler1 = new Handler();
                             handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                home = retreiveFromHPDB();
                            }
                            },200);

                            Intent intent = new Intent(MainActivity.this,
                                    Favourites.class);
                            intent.putExtra(Intent.EXTRA_TITLE, titleFromWebView);
                            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, dialogUrl);
                            startActivity(intent);
                        }
                        favDialog.dismiss();
                    }
                });
            }
        }, 200);

    }

    public void settings(View v) {
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");
        addToCurrentStateDB(viewer.getUrl());
        if(password.equals("")){
            // If there is no password
            Intent goSettings = new Intent(this, Passwords.class);
            goSettings.putExtra(Intent.EXTRA_REFERRER, 1);
            startActivity(goSettings);
            xrossInvisible(null);
        } else {
            // If there is a password
            Intent goSettings2 = new Intent(this, EnterPassword.class);
            startActivity(goSettings2);
            xrossInvisible(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void go(final View v) {
        getBlockedSites();
        getBlockedSites();
        String url = addressBar.getText().toString();
        if (v != null) {
            if (ProfanityFilter.isBadString(url)) {

                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getBaseContext(), "Restricted word searched by the browser. Validate your age", duration);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
                addressBar.setText("");
                return;
            }
            historyDBHelper.addData(url);
            filterUrl(url);
        }
        String URLin= url;
        String text=getTextFromWWW(URLin);
        getFilterWords();
        if(checkPage(text,filterWordsList)){
            viewer.loadUrl(tempUrl);
            xrossInvisible(null);
            viewer.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    dialogUrl = url;
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
        }else hdr.setText(R.string.not_allowed);


    }

    public void refresh(View v) {
        getBlockedSites();
        getBlockedSites();
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
        getBlockedSites();
        getBlockedSites();
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
        getBlockedSites();
        getBlockedSites();
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
        getBlockedSites();
        getBlockedSites();
        if (retreiveFromHPDB() == null) {
            tempUrl = "";
        } else {
            tempUrl = "http://" + filterBlocked(retreiveFromHPDB());
        }
        go(null);
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

    public void cancelpageLoad(View v) {
        viewer.stopLoading();
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
        String selectString = "SELECT * FROM bl_table;";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                blockedList.add(cursor
                        .getString(cursor.getColumnIndex("Url")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
    public void getFilterWords() {
        SQLiteDatabase db = filterWords.getReadableDatabase();
        String selectString = "SELECT * FROM filter_table";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                filterWordsList.add(cursor.getString(cursor.getColumnIndex("words")));
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
            if (s.contains(blockedList.get(i)) || blockedList.get(i).contains(s)) {
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

    public void addToHPDB(String s) {
        SQLiteDatabase db = hpDatabaseHelper.getWritableDatabase();
        hpDatabaseHelper.clearTable(db);
        ContentValues values = new ContentValues();
        values.put("Url", s);
        db.insert("hp_table", null, values);
        db.close();
    }

    public String retreiveFromHPDB() {
        SQLiteDatabase db = hpDatabaseHelper.getReadableDatabase();
        String select = "SELECT * FROM hp_table;";
        String returnHP = "";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                returnHP = cursor.getString(cursor.getColumnIndex("Url"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnHP;
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

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public final boolean isFullScreen() {
        int flg = getWindow().getAttributes().flags;
        boolean flag = false;
        if ((flg & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            flag = true;
        }
        return flag;
    }
    public String getTextFromWWW(String URLin) {
        try {
            URL url = new URL(URLin);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String text = in.readLine();
            in.close();

            return text;

        } catch (Exception e) {
            return "Error > " + e.getMessage();

        }
    }
    public boolean checkPage(String text, ArrayList<String> filterWordsList){
        filterWordsList=this.filterWordsList;
        for(int i=0;i<filterWordsList.size();i++){
            String a = (String) filterWordsList.get(i);
            text=text.toLowerCase();
            if(text.contains(a)){
                return false;
            }
        }return true;
    }

    /*
     * Method for getting data passed from Favourites class when a favourite is clicked.
     */
    public void goToFavourite() {
        if (getIntent().hasExtra("favourites")) {
            favouriteClicked = getIntent().getStringExtra("favourites");

            /*if (favouriteClicked.startsWith("www")) {
                favouriteClicked = "https://" + favouriteClicked;
            } else if (!favouriteClicked.startsWith("http")) {
                favouriteClicked = "https://www." + favouriteClicked;
            }*/

            FavDatabaseHelper helper = new FavDatabaseHelper(this);
            String link = helper.retrieveLinkByTitle(favouriteClicked);

            if (link != null) {
                tempUrl = link;
                go(null);
            }
        }
    }
}
