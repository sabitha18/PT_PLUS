package com.pt_plus.Activitys;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppUtils;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class ActivityRegister extends SuperActivity {
    private View appBar;
    private ImageView imgBack;
    private TextView txtappBarTitle;
    private LinearLayout lnrLogin;
    private Button btnRegister;
    private EditText etPassword, etConfirmPassword, etName, etEmail, etMob, etDob;
    private CardView cardViewBottomSheetDone;
    private AuthService authService;

    private String fcmToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private BottomSheetDialog bottomSheetDialog;
    private CalendarView calendarView;

    private void initView() {
        appBar = findViewById(R.id.appbar);
        setStatusBarHight(appBar);
        txtappBarTitle = findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.register);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(_click);


        Intent intent = getIntent();

        // Retrieve the value associated with the "email" key
        if (intent != null && intent.hasExtra("fcm_token")) {
            fcmToken = intent.getStringExtra("fcm_token");

        }

        lnrLogin = findViewById(R.id.lnr_login);
        lnrLogin.setOnClickListener(_click);
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etMob = findViewById(R.id.et_mob);
        etDob = findViewById(R.id.et_dob);
        etDob.setOnClickListener(_click);

                bottomSheetDialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.layout_checkout_shedule_bottomsheet);
        TextView txtBottomSheetTitle = bottomSheetDialog.findViewById(R.id.txt_bottosheettite);
        calendarView = bottomSheetDialog.findViewById(R.id.calendarView);
        cardViewBottomSheetDone = bottomSheetDialog.findViewById(R.id.bottom_sheet_carview_done);
        cardViewBottomSheetDone.setOnClickListener(_click);
        txtBottomSheetTitle.setText("Choose Date of Birth");
        ImageView imgBottomSheetClose = bottomSheetDialog.findViewById(R.id.img_bottom_sheet_close);
        imgBottomSheetClose.setOnClickListener(_click);

        authService = new AuthService(this, _callback);

        etPassword.setOnTouchListener(new View.OnTouchListener() {
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
        });

        etConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etConfirmPassword.getRight() - etConfirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (isConfirmPasswordShow) {
                            isConfirmPasswordShow = false;
                            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_hide, 0);
                        } else {
                            isConfirmPasswordShow = true;
                            etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                            etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_show, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }

    boolean isPasswordShow = false;
    boolean isConfirmPasswordShow = false;
    private final CallBack _callback = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_REGISTER_USER) {
                    if (statusCode == 201) {
                        JSONObject error = jsonObject.has("errors")?jsonObject.getJSONObject("errors"):null;
                        if(jsonObject != null){
                            if(error.has("email")){
                                toast(error.getJSONArray("email").getString(0));
                            }
                            if(error.has("phone")){
                                toast(error.getJSONArray("phone").getString(0));
                            }
                            if(error.has("password")){
                                toast(error.getJSONArray("password").getString(0));
                            }
                            if(error.has("name")){
                                toast(error.getJSONArray("name").getString(0));
                            }
                        }
                    }else {
                        toast(AppUtils.getStringValueFromJson(jsonObject,"message"));
                        //finish();
                        System.out.println(" check ----   "+jsonObject);
                        Intent intent = new Intent(ActivityRegister.this, ActivityOTP.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("email",etMob.getText().toString());
                        openScreenAnimation(intent, ActivityRegister.this, false);

                    }

                }
            }catch (Exception e){
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
                case R.id.btn_register: {
                    validateAndSubmitRegister();
                    break;
                }
                case R.id.lnr_login: {
                    finish();
                    break;
                }
                case R.id.img_back: {
                    finish();
                    break;
                }
                case R.id.et_dob: {
                    bottomSheetDialog.show();
                    break;
                }
                case R.id.img_bottom_sheet_close: {
                    bottomSheetDialog.dismiss();
                    break;
                }
                case R.id.bottom_sheet_carview_done: {
                    Calendar selectedDate = calendarView.getFirstSelectedDate();

                    Date date = selectedDate.getTime();
                    etDob.setText(AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
                    bottomSheetDialog.dismiss();
                    break;
                }


            }
        }
    };

    private void validateAndSubmitRegister() {
        try {
            JSONObject jsonObject = new JSONObject();
            boolean isValid = true;
            if (TextUtils.isEmpty(etName.getText().toString())) {
                etPassword.setError("This field is required");
                isValid = false;
            } else {
                etPassword.setError(null);
                jsonObject.put("name", etName.getText().toString());
            }

            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                etEmail.setError("This field is required");
                isValid = false;
            } else {
                etEmail.setError(null);
                jsonObject.put("email", etEmail.getText().toString());
            }

            if (TextUtils.isEmpty(etMob.getText().toString())) {
                etMob.setError("This field is required");
                isValid = false;
            } else {
                etMob.setError(null);
                jsonObject.put("phone", etMob.getText().toString());
            }

            if (TextUtils.isEmpty(etDob.getText().toString())) {
                etDob.setError("This field is required");
                isValid = false;
            } else {
                etDob.setError(null);
                jsonObject.put("dob", etDob.getText().toString());
            }
            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                etPassword.setError("This field is required");
                isValid = false;
            } else {
                etPassword.setError(null);
                jsonObject.put("password", etPassword.getText().toString());
            }

            if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError("This field is required");
                isValid = false;
            } else {
                etConfirmPassword.setError(null);
                jsonObject.put("password_confirmation", etConfirmPassword.getText().toString());
            }
            if (isValid) {
                jsonObject.put("fcm_token", fcmToken);
                jsonObject.put("user_agent", "android");
                authService.registerUser(ServiceNames.REQUEST_ID_REGISTER_USER, jsonObject);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}