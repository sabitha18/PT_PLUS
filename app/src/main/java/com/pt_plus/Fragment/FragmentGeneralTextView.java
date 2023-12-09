package com.pt_plus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONObject;


public class FragmentGeneralTextView extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_text_view, container, false);
        initView(view);
        return view;
    }


    private View appbar;
    private TextView txtappBarTitle,txtText;
    private ImageView imgBack;
    private GeneralService generalService;


    private void initView(View view) {
        generalService = new GeneralService(getFragmentActivity(), _callBack);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText("");
        txtText = view.findViewById(R.id.txt_txet);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);


        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getLong("type") == AppCons.GENERAL_WEBVIEW_TYPE_PRIVACY) {
                generalService.getPrivacyPolicy(ServiceNames.REQUEST_ID_PRIVACY_POLICY);
            }else {
                txtappBarTitle.setText(R.string.terms_and_conditions);
                generalService.getTermsANdConditions(ServiceNames.REQUEST_GET_TERMS_CONDITIONS);
            }
        }

        getFragmentActivity().showProgress(true);

    }

    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_PRIVACY_POLICY) {
                    txtText.setText(AppUtils.getStringValueFromJson(jsonObject,"content"));
                    txtappBarTitle.setText(AppUtils.getStringValueFromJson(jsonObject,"title"));
                } else if (serviceId == ServiceNames.REQUEST_GET_TERMS_CONDITIONS) {
                    txtappBarTitle.setText(R.string.terms_and_conditions);
                    txtText.setText(AppUtils.getStringValueFromJson(jsonObject,"content"));
//                    txtappBarTitle.setText(AppUtils.getStringValueFromJson(jsonObject,"title"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getFragmentActivity().showProgress(false);
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
                    goback();
                    break;
                }


            }
        }
    };

    private void goback() {
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

}