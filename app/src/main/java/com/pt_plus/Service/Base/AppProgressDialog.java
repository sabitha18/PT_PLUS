package com.pt_plus.Service.Base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.pt_plus.R;


public class AppProgressDialog extends ProgressDialog {

    public static ProgressDialog ctor(Context context) {
        AppProgressDialog dialog = new AppProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public AppProgressDialog(Context context) {
        super(context);
    }

    public AppProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_progress_bar);
        ProgressBar progressBar = findViewById(R.id.progress);
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
        //animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //animation.stop();
    }
}