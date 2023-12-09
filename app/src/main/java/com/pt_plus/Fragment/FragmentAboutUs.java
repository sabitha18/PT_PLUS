package com.pt_plus.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppUtils;

import org.json.JSONObject;


public class FragmentAboutUs extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ImageView imgBack;
    private View appbar;
    private TextView txtappBarTitle,txtContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        initView(view);
        return view;
    }

    private GeneralService generalService;
    private ImageView imgABout;

    private void initView(View view) {
        txtContent = view.findViewById(R.id.txt_content);
        imgABout = view.findViewById(R.id.img_about);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);


        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(getFragmentActivity().getString(R.string.about_us));

        generalService = new GeneralService(getFragmentActivity(), _callBack);
        generalService.getAboutUs(ServiceNames.REQUEST_GET_GET_ABOUT_US);
        getFragmentActivity().showProgress(true);
    }

    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_GET_GET_ABOUT_US) {
                    Glide.with(getFragmentActivity())
                            .load(AppUtils.getStringValueFromJson(jsonObject,"image"))
                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .into(imgABout);
                    if(AppUtils.getStringValueFromJson(jsonObject,"content") != null && !AppUtils.getStringValueFromJson(jsonObject,"content").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject,"content").equalsIgnoreCase("null")){
                        txtContent.setText(AppUtils.getStringValueFromJson(jsonObject,"content"));
                    }

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