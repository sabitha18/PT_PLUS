package com.pt_plus.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;

import org.json.JSONObject;

public class ActivityLogin extends SuperActivity {
    private View appBar;
    private ImageView imgBack;
    private TextView txtappBarTitle;
    private LinearLayout lnrRegister;
    private Button btnLogin;
    private EditText etPassword, etEmail;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }
private TextView txtForgottPassword;
    private void initView() {
        initFcmToken();
        appBar = findViewById(R.id.appbar);
        setStatusBarHight(appBar);
        txtappBarTitle = findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.login_page);

        lnrRegister = findViewById(R.id.lnr_register);
        lnrRegister.setOnClickListener(_click);

        txtForgottPassword = findViewById(R.id.txt_forgot_password);
        txtForgottPassword.setOnClickListener(_click);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(_click);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);

        authService = new AuthService(this, _callback);

        if(isEnglish()){
            etPassword.setOnTouchListener(_englishTouc);
        }else {
            etPassword.setOnTouchListener(_arbicTouc);
        }

    }
    String fcmToken;
    private void initFcmToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        fcmToken =task.getResult();
                        AppLogger.log("toke   ac         " + fcmToken);
                    }
                });
    }

    private final View.OnTouchListener _arbicTouc = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;


            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                    // your action here
                    if (isPasswordShow) {
                        isPasswordShow = false;
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_hide, 0, 0, 0);
                    } else {
                        isPasswordShow = true;
                        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                        etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_show, 0, 0, 0);
                    }

                    return true;
                }
            }
            return false;
        }
    };
    private final View.OnTouchListener _englishTouc = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;


            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    if (isPasswordShow) {
                        isPasswordShow = false;
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_hide, 0);
                    } else {
                        isPasswordShow = true;
                        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_show, 0);
                    }

                    return true;
                }
            }
            return false;
        }
    };

    boolean isPasswordShow = false;
    private final CallBack _callback = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_LOGIN_USER) {
                    if (statusCode == 201) {
                        JSONObject error = jsonObject.has("errors") ? jsonObject.getJSONObject("errors") : null;
                        if (jsonObject != null) {
                            toast(AppUtils.getStringValueFromJson(jsonObject, "message"));

                        }
                    } else {

                        String access_token = AppUtils.getStringValueFromJson(jsonObject,"access_token");
                        PrefUtils.saveToPrefs(ActivityLogin.this,PrefKeys.PREF_SESSION,access_token);
//                        toast(AppUtils.getStringValueFromJson(jsonObject, "message"));
                        authService.getProfile(ServiceNames.REQUEST_ID_GET_PROFILE);
                        setSideNav();
                        finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lnr_register: {
                    Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                    intent.putExtra("fcm_token",fcmToken);
                    openScreenAnimation(intent, ActivityLogin.this, false);
                    break;
                }case R.id.txt_forgot_password: {
                    Intent intent = new Intent(ActivityLogin.this, ActivityForgottPassword.class);
                    openScreenAnimation(intent, ActivityLogin.this, false);
                    break;
                }
                case R.id.btn_login: {
                    validateAndSubmitRegister();
                    break;
                }
                case R.id.img_back: {
                    finish();
                    break;
                }


            }
        }
    };

    private void validateAndSubmitRegister() {
        try {
            JSONObject jsonObject = new JSONObject();
            boolean isValid = true;
            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                etEmail.setError("This field is required");
                isValid = false;
            } else {
                etEmail.setError(null);
                jsonObject.put("username", etEmail.getText().toString());
            }


            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                etPassword.setError("This field is required");
                isValid = false;
            } else {
                etPassword.setError(null);
                jsonObject.put("password", etPassword.getText().toString());
            }

            jsonObject.put("fcm_token", fcmToken);
            jsonObject.put("user_agent", "android");
            if (isValid) {
                authService.loginUser(ServiceNames.REQUEST_ID_LOGIN_USER, jsonObject);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}