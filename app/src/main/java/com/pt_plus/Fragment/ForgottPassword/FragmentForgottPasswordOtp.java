package com.pt_plus.Fragment.ForgottPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pt_plus.Fragment.SuperFragment;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.GenericKeyEvent;
import com.pt_plus.Utils.GenericTextWatcher;

import org.json.JSONObject;


public class FragmentForgottPasswordOtp extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgott_password_otp, container, false);
        initView(view);
        return view;
    }

    private View appBar;
    private ImageView imgBack;
    private TextView txtappBarTitle;
    private Button btnCountinue;
    private String email;

    private void initView(View view) {
        appBar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appBar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.forget_password);


        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        btnCountinue = view.findViewById(R.id.btn_continue);
        btnCountinue.setOnClickListener(_click);

        txtOtp1 = view.findViewById(R.id.txt_otp_1);
        txtOtp2 = view.findViewById(R.id.txt_otp_2);
        txtOtp3 = view.findViewById(R.id.txt_otp_3);
        txtOtp4 = view.findViewById(R.id.txt_otp_4);
        txtOtp5 = view.findViewById(R.id.txt_otp_5);
        txtOtp6 = view.findViewById(R.id.txt_otp_6);


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

        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                email = bundle.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        authService = new AuthService(getFragmentActivity(), _callBack);
    }

    private AuthService authService;

    private EditText txtOtp1, txtOtp2, txtOtp3, txtOtp4,txtOtp5,txtOtp6;

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.img_back: {
                    getFragmentActivity().getSupportFragmentManager().popBackStack();
                    break;
                }
                case R.id.btn_continue: {
                    validateAndSubmit();
                    break;
                }


            }
        }
    };
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            getFragmentActivity().showProgress(false);
            try {
                if (serviceId == ServiceNames.REQUEST_FORGOT_VERIFY_OTP) {
                    System.out.println("check 99999999    "+jsonObject);
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    if (AppUtils.getBooleanValueFromJson(jsonObject, "status")) {
                        String passcode = txtOtp1.getText().toString() + txtOtp2.getText().toString() + txtOtp3.getText().toString() + txtOtp4.getText().toString()+txtOtp5.getText().toString()+txtOtp6.getText().toString();

                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        bundle.putString("otp", passcode);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentSetNewPassword fragment2 = new FragmentSetNewPassword();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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
                    jsonObject.put("phone", email);
                    authService.forgottPasswordVerifyOtp(ServiceNames.REQUEST_FORGOT_VERIFY_OTP,jsonObject);
                    getFragmentActivity().showProgress(true);
                } else {
                    toastInFragment("This field is required");

                }

            } else {
                toastInFragment("This field is required");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}