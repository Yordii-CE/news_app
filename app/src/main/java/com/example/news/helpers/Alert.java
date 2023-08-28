package com.example.news.helpers;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.news.R;
import com.google.android.material.snackbar.Snackbar;

public class Alert {
    public static void message(View view, String message, int textColor) {
        int duration = Snackbar.LENGTH_SHORT;
        Snackbar snackbar = Snackbar.make(view, message, duration);

        // Establecer el color del texto del mensaje
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(textColor);

        snackbar.show();
    }

}
