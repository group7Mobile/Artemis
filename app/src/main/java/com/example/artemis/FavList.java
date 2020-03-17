package com.example.artemis;

import java.util.ArrayList;

public class FavList {

    private ArrayList<String> title;
    private ArrayList<String> url;
    private ArrayList<Boolean> hp;

    public FavList() {
        title = new ArrayList<>();
        url = new ArrayList<>();
        hp = new ArrayList<>();
    }

    public void pushTitle(String tt) {
        title.add(tt);
    }

    public void pushUrl(String link) {
        url.add(link);
    }

    public void pushHp(boolean hmp) {
        hp.add(hmp);
    }

    public void pushAll(String tt, String lk, boolean b) {
        boolean res = false;
        int i;
        if (!b) {
            for (i = 0; i < hp.size(); i++) {
                if (hp.get(i)) {
                    res = true;
                    break;
                }
            }
            if (res) {
                title.add(i, tt);
                url.add(i, lk);
                hp.add(i, b);
            } else {
                title.add(tt);
                url.add(lk);
                hp.add(b);
            }
        } else {
            title.add(tt);
            url.add(lk);
            hp.add(b);
        }
    }

    public String getHomePage() {
        String hpLink = "";
        for (int i = 0; i < hp.size(); i++) {
            if (hp.get(i)) {
                hpLink = url.get(i);
            }
        }
        return hpLink;
    }

    public ArrayList getTitleArray() {
        return title;
    }
}
