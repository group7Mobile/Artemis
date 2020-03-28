package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void blackList(View v){
        Intent goBlackList = new Intent(Settings.this, BlackList.class);
        startActivity(goBlackList);

    }

    public void screenTime(View v){
        Intent goScreenTime = new Intent(Settings.this, ScreenTime.class);
        startActivity(goScreenTime);

    }
    public void homePage(View v){
        Intent gohomePage = new Intent(Settings.this, HomePage.class);
        startActivity(gohomePage);

    }
    public void homePageWidgets(View v) {
        Intent gohomePageWidgets = new Intent(Settings.this, HomePageWidgets.class);
        startActivity(gohomePageWidgets);
    }
    public void timeTable(View v) {
        Intent goTimetable = new Intent(Settings.this, TimeTable.class);
        startActivity(goTimetable);
    }

    public void shortcuts(View v) {
        Intent goShortcuts = new Intent(Settings.this, Shortcuts.class);
        startActivity(goShortcuts);
    }

    public void background(View v) {
        Intent goBackground = new Intent(Settings.this, Background.class);
        startActivity(goBackground);
    }

    public void favourites(View v) {
        Intent goRecycler = new Intent(this, Favourites.class);
        startActivity(goRecycler);

        /*Intent goFavourites = new Intent(Settings.this, Favourites.class);
        goFavourites.putExtra(Intent.EXTRA_TEXT, 2);
        startActivity(goFavourites);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void passwords(View v) {
        Intent goPasswords = new Intent(Settings.this, Passwords.class);
        goPasswords.putExtra(Intent.EXTRA_REFERRER, 2);
        startActivity(goPasswords);
    }

    public void popUpBlocking(View v) {
        Intent goPopUp = new Intent(Settings.this, PopUpBlocking.class);
        startActivity(goPopUp);
    }

    public void privacy(View v) {
        Intent goPrivacy = new Intent(Settings.this, Privacy.class);
        startActivity(goPrivacy);
    }

    public void alertSettings(View v) {
        Intent goAlert = new Intent(Settings.this, AlertSettings.class);
        startActivity(goAlert);
    }

    public void filters(View v) {
        Intent goFilters = new Intent(Settings.this, Filters.class);
        startActivity(goFilters);
    }

    public void about(View v) {
        Intent goAbout = new Intent(Settings.this, About.class);
        startActivity(goAbout);
    }

    public void account(View v) {
        Intent goAccount = new Intent(Settings.this, AccountPage.class);
        startActivity(goAccount);
    }
    public void mainPage(View v) {
        finish();
    }
}
