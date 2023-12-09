package com.pt_plus.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;


import com.pt_plus.R;
import com.pt_plus.Utils.Callback.PopUpCallBack;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class UiUtils {

    private static UiUtils _self;

    private UiUtils() {
    }

    public static UiUtils getInstance() {
        if (_self == null) {
            _self = new UiUtils();
        }
        return _self;
    }


    public void showSimpleAlert(Context context, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }

    public void showSimpleAlertWithCopyButton(Context context, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setNegativeButton("Copy", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData myClip = ClipData.newPlainText("text", message);
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(context, "Text Copied",Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Ok", new OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }

   /* private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    AppLog.log(arg1+ (arg2+1)+ arg3+"");
                }
            }; */



    public static boolean isValidEmail(String email) {
        boolean isValidEmail = false;
        if (email != null && email.length() >= 4) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern pat = Pattern.compile(emailRegex);
            isValidEmail = pat.matcher(email).matches();
        }

        return isValidEmail;

    }

    public void showConfirm(final int id, String message, final PopUpCallBack callback, Activity activity) {

        final OnClickListener dialogClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        callback.onOK();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        callback.onCancel();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setPositiveButton(activity.getString(R.string.Yes), dialogClickListener)
                .setNegativeButton(R.string.No, dialogClickListener).show();
    }


    public Bitmap createBlurredBitmap(View view, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(25f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        return bitmap;
    }

}
