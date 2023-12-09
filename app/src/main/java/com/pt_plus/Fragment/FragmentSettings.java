package com.pt_plus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Dialog.BottomSheetDilaog.CountryBoottomSheetDilaog;
import com.pt_plus.Dialog.BottomSheetDilaog.CurrencyBoottomSheetDilaog;
import com.pt_plus.Dialog.BottomSheetDilaog.LangugeChageBoottomSheetDilaog;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;

import org.json.JSONObject;


public class FragmentSettings extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ImageView imgBack;
    private View appbar;
    private TextView txtappBarTitle, txtContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }


    private LinearLayout lnrLanguage, lnrCountry, lnrCurrecny;
    private TextView txtLang;
    private Switch switchNotification;

    private void initView(View view) {

        txtLang = view.findViewById(R.id.txt_lang);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        switchNotification = view.findViewById(R.id.switch_notification);
        switchNotification.setOnClickListener(_click);

        lnrLanguage = view.findViewById(R.id.lnr_language);
        lnrLanguage.setOnClickListener(_click);
        lnrCountry = view.findViewById(R.id.lnr_country);
        lnrCountry.setOnClickListener(_click);

        lnrCurrecny = view.findViewById(R.id.lnr_currency);
        lnrCurrecny.setOnClickListener(_click);


        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(getFragmentActivity().getString(R.string.settings));

        if (getFragmentActivity().isEnglish()) {
            txtLang.setText("English");
        } else {
            txtLang.setText("عربي");
        }

        generalService = new GeneralService(getFragmentActivity(), _callBack);
setView();
    }

    private void setView() {
        try {
            JSONObject jsonObject = new JSONObject(PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_PROFILE, "").toString());
            AppLogger.log("dsfdasfdsfds             "+AppUtils.getLongValueFromJson(jsonObject,"notification_enabled"));
            if(jsonObject != null && jsonObject.has("notification_enabled") ){
if(AppUtils.getLongValueFromJson(jsonObject,"notification_enabled") == 1){
    switchNotification.setChecked(true);

}else {
    switchNotification.setChecked(false);
}

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    GeneralService generalService;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            if (serviceId == ServiceNames.REQUEST_ID_GET_NOTIFICATION_TOGGLE_STATUS) {
toastInFragment(AppUtils.getStringValueFromJson(jsonObject,"message"));
                AuthService authService = new AuthService(getFragmentActivity(), _callBack);
                authService.getProfile(ServiceNames.REQUEST_ID_GET_PROFILE);
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
                case R.id.lnr_language: {
                    LangugeChageBoottomSheetDilaog dialogFragment = new LangugeChageBoottomSheetDilaog(getFragmentActivity());
//                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");
                    break;
                }
                case R.id.lnr_country: {
                    CountryBoottomSheetDilaog dialogFragment = new CountryBoottomSheetDilaog(getFragmentActivity());
//                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");
                    break;
                }
                case R.id.lnr_currency: {
                    CurrencyBoottomSheetDilaog dialogFragment = new CurrencyBoottomSheetDilaog(getFragmentActivity());
//                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");
                    break;
                }
                case R.id.switch_notification: {
                    try {
                        AppLogger.log("dsfdasfdsfds tt   "+switchNotification.isChecked());
                        if (switchNotification.isChecked()) {
                            generalService.changeNotificationToggleStatus(ServiceNames.REQUEST_ID_GET_NOTIFICATION_TOGGLE_STATUS, new JSONObject().put("status", 1));
                        } else {
                            generalService.changeNotificationToggleStatus(ServiceNames.REQUEST_ID_GET_NOTIFICATION_TOGGLE_STATUS, new JSONObject().put("status", 0));
                        }
getFragmentActivity().showProgress(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }


            }
        }
    };

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}