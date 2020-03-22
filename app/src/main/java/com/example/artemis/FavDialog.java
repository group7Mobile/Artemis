package com.example.artemis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.artemis.R;

public class FavDialog extends AppCompatDialogFragment {

    private EditText titleBox;
    private TextView question;
    private CheckBox homePage;
    private boolean result;
    private String title;
    private String url;
    private boolean hp;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_favorite, null);
        titleBox = view.findViewById(R.id.title);
        question = view.findViewById(R.id.question);
        homePage = view.findViewById(R.id.checkBox);
        String urlVisual;
        try {
            if (getUrl().length() > 35) {
                urlVisual = getUrl().substring(0, 35) + "...";
            } else {
                urlVisual = getUrl();
            }
        } catch (Exception e) {
            urlVisual = getUrl();
        }
        String qst = "Do you want to add\n " + urlVisual + " \nin favorites?";
        question.setText(qst);
        titleBox.setText(title);
        builder.setView(view).setTitle("fav_dlg")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(false);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(true);
                        setTitleBox();
                        setHp();
                    }
                });
        return builder.create();
    }

    public void setTitleBox() {
        title = titleBox.getText().toString();
    }

    public void setUrl(String link) {
        url = link;
    }

    public void setHp() {
        hp = (homePage.isChecked());
    }

    public void setResult(boolean b) {
        result = b;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean isHp() {
        return hp;
    }

    public void setTitle(String s) {
        title = s;
    }

    public boolean getResult() {
        return result;
    }


}
