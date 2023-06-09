package com.openbanking.pfm.sdk.demo.utils;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

public class AlertUtils {
    public static void show(Context context, String title, String message) {
        Log.d("SERVICE", message);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, view) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    public static void confirm(Context context, String title, String message, OnAlertClickListener click) {
        Log.d("SERVICE", message);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialog, view) -> {
                    click.click();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancelar", (dialog, view) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    public interface OnAlertClickListener {
        void click();
    }
}
