package com.pt_plus.Fragment.ForgottPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
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

import org.json.JSONObject;


public class FragmentSetNewPassword extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_new_password, container, false);
        initView(view);
        return view;
    }


    private View appBar;
    private ImageView imgBack;
    private TextView txtappBarTitle;
    private Button btnSubmit;
    private EditText etPassword, etConfirmPassword;
    private String email, otp;

    private void initView(View view) {
        appBar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appBar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.forget_password);


        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        etPassword = view.findViewById(R.id.et_password);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        btnSubmit = view.findViewById(R.id.btn_reset_password);
        btnSubmit.setOnClickListener(_click);
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                email = bundle.getString("email");
                otp = bundle.getString("otp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        authService = new AuthService(getFragmentActivity(), _callBack);
    }

    private AuthService authService;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            getFragmentActivity().showProgress(false);
            try {
                if (serviceId == ServiceNames.REQUEST_FORGOT_SET_PASSWORD) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    if (AppUtils.getBooleanValueFromJson(jsonObject, "status")) {


                        getFragmentActivity().finish();
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

                case R.id.img_back: {
                    getFragmentActivity().getSupportFragmentManager().popBackStack();
                    break;
                }
                case R.id.btn_reset_password: {
                    validateAndSubmit();
                    break;
                }


            }
        }
    };

    private void validateAndSubmit() {
        try {
            JSONObject jsonObject = new JSONObject();
            boolean isValid = true;
            if (TextUtils.isEmpty(etPassword.getText().toString()) && TextUtils.isEmpty(etPassword.getText().toString()) && !etPassword.getText().toString().equalsIgnoreCase(etConfirmPassword.getText().toString())) {
                etPassword.setError("This field is required");
                isValid = false;
            } else {
                etPassword.setError(null);
                jsonObject.put("password", etPassword.getText().toString());
            }

            jsonObject.put("otp", otp);
            jsonObject.put("phone", email);

            if (isValid) {
                authService.forgottSetPassword(ServiceNames.REQUEST_FORGOT_SET_PASSWORD, jsonObject);
                getFragmentActivity().showProgress(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}