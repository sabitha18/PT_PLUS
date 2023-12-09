package com.pt_plus.Fragment.ForgottPassword;

import android.content.Intent;
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

import com.pt_plus.Activitys.ActivityHome;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Activitys.ActivityRegister;
import com.pt_plus.Fragment.FragmentCart;
import com.pt_plus.Fragment.FragmentTrainerList;
import com.pt_plus.Fragment.SuperFragment;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONObject;


public class FragmentForgotPasswordEmail extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password_email, container, false);
        initView(view);
        return view;
    }

    private View appBar;
    private ImageView imgBack;
    private TextView txtappBarTitle;
    private EditText etEmail;
    private Button btnCountinue;

    private void initView(View view) {
        appBar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appBar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.forget_password);


        etEmail = view.findViewById(R.id.et_email);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        btnCountinue = view.findViewById(R.id.btn_continue);
        btnCountinue.setOnClickListener(_click);

        authService = new AuthService(getFragmentActivity(),_callBack);
    }
private AuthService authService;
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.img_back: {
                    getFragmentActivity().finish();
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
            boolean isValid = true;
            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                etEmail.setError("This field is required");
                isValid = false;
            } else {
                etEmail.setError(null);
                jsonObject.put("phone", etEmail.getText().toString());
            }
            if (isValid) {
                authService.getForgottPasswrodOtp(ServiceNames.REQUEST_GET_FORGOT_PASSWORD_OTP,jsonObject);
                getFragmentActivity().showProgress(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            getFragmentActivity().showProgress(false);
            try {
                if(serviceId == ServiceNames.REQUEST_GET_FORGOT_PASSWORD_OTP){
                    System.out.println("check 6689   "+jsonObject);
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject,"message"));
                    if(AppUtils.getBooleanValueFromJson(jsonObject,"status")){


                        Bundle bundle = new Bundle();
                        bundle.putString("email", etEmail.getText().toString());
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentForgottPasswordOtp fragment2 = new FragmentForgottPasswordOtp();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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
}