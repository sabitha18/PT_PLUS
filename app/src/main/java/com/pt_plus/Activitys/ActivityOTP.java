package com.pt_plus.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pt_plus.Fragment.ForgottPassword.FragmentForgotPasswordEmail;
import com.pt_plus.Fragment.ForgottPassword.FragmentForgottPasswordOtp;
import com.pt_plus.Fragment.ForgottPassword.FragmentSetNewPassword;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.GenericKeyEvent;
import com.pt_plus.Utils.GenericTextWatcher;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class ActivityOTP extends SuperActivity {
    private View appBar;
    private ImageView imgBack;
    private TextView txtappBarTitle;
    private EditText txtOtp1,txtOtp2,txtOtp3,txtOtp4,txtOtp5,txtOtp6;
    private Button btnContinue;
    private String phone_no;
    private AuthService authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initView();
    }

    private void initView() {
        appBar = findViewById(R.id.appbar);
        setStatusBarHight(appBar);
        txtappBarTitle = findViewById(R.id.appbar_title);
        txtappBarTitle.setText("Verify OTP");

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(_click);

        txtOtp1 = findViewById(R.id.txt_otp_1);
        txtOtp2 = findViewById(R.id.txt_otp_2);
        txtOtp3 = findViewById(R.id.txt_otp_3);
        txtOtp4 = findViewById(R.id.txt_otp_4);
        txtOtp5 = findViewById(R.id.txt_otp_5);
        txtOtp6 = findViewById(R.id.txt_otp_6);


        txtOtp1.addTextChangedListener(new GenericTextWatcher(txtOtp1, txtOtp2));
        txtOtp2.addTextChangedListener(new GenericTextWatcher(txtOtp2, txtOtp3));
        txtOtp3.addTextChangedListener(new GenericTextWatcher(txtOtp3, txtOtp4));
        txtOtp4.addTextChangedListener(new GenericTextWatcher(txtOtp4, txtOtp5));
        txtOtp5.addTextChangedListener(new GenericTextWatcher(txtOtp5, txtOtp6));
        txtOtp6.addTextChangedListener(new GenericTextWatcher(txtOtp6, null));

        txtOtp2.setOnKeyListener(new GenericKeyEvent(txtOtp1, txtOtp2));

        txtOtp2.setOnKeyListener(new GenericKeyEvent(txtOtp2, txtOtp1));
        txtOtp3.setOnKeyListener(new GenericKeyEvent(txtOtp3, txtOtp2));
        txtOtp4.setOnKeyListener(new GenericKeyEvent(txtOtp4, txtOtp3));
        txtOtp5.setOnKeyListener(new GenericKeyEvent(txtOtp5, txtOtp4));
        txtOtp6.setOnKeyListener(new GenericKeyEvent(txtOtp6, txtOtp5));

        Intent intent = getIntent();

        // Retrieve the value associated with the "email" key
        if (intent != null && intent.hasExtra("email")) {
            phone_no = intent.getStringExtra("email");

        }

        authService = new AuthService(this, _callback);
    }
    private final CallBack _callback = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            showProgress(false);
            try {
                if (serviceId == ServiceNames.REQUEST_FORGOT_VERIFY_OTP) {
                    System.out.println(" one---------");


                    toast("Registration successful!");
                    if (AppUtils.getBooleanValueFromJson(jsonObject, "status")) {
                        System.out.println(" two---------"+jsonObject);
                        String passcode = txtOtp1.getText().toString() + txtOtp2.getText().toString() + txtOtp3.getText().toString() + txtOtp4.getText().toString()+txtOtp5.getText().toString()+txtOtp6.getText().toString();
                        String access_token = AppUtils.getStringValueFromJson(jsonObject,"access_token");
                        PrefUtils.saveToPrefs(ActivityOTP.this, PrefKeys.PREF_SESSION,access_token);
//                        toast(AppUtils.getStringValueFromJson(jsonObject, "message"));
                        authService.getProfile(ServiceNames.REQUEST_ID_GET_PROFILE);
                        setSideNav();
                        Intent intent = new Intent(ActivityOTP.this, ActivityHome.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        openScreenAnimation(intent,ActivityOTP.this,true);
//
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

                case R.id.lnr_login:
                case R.id.img_back: {
                    finish();
                    break;
                }
                case R.id.btn_continue: {
                    validateAndSubmit();
                    break;
                }


            }
        }
    };

    private void validateAndSubmit() {
        try {
            JSONObject jsonObject = new JSONObject();
            if (txtOtp1.getText().toString() != null &&
                    txtOtp2.getText().toString() != null &&
                    txtOtp2.getText().toString() != null &&
                    txtOtp4.getText().toString() != null
            ) {
                String passcode = "";
                passcode = txtOtp1.getText().toString() + txtOtp2.getText().toString() + txtOtp3.getText().toString() + txtOtp4.getText().toString()+txtOtp5.getText().toString()+txtOtp6.getText().toString();

                if (passcode.length() == 6) {
                    jsonObject.put("otp", passcode);
                    jsonObject.put("phone", phone_no);
                    authService.registerVerifyOtp(ServiceNames.REQUEST_FORGOT_VERIFY_OTP,jsonObject);
                    showProgress(true);
                } else {
                    toast(AppUtils.getStringValueFromJson(jsonObject,"This field is required"));

                }

            } else {
                toast(AppUtils.getStringValueFromJson(jsonObject,"This field is required"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}