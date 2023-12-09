package com.pt_plus.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentCenterCategory extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private CenterService centerServeice;
    CommonCardCategoryListAdapter commonCardCategoryListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center_category, container, false);
        initView(view);

        return view;
    }

    private View appbar, imgBack;
    private TextView txtappBarTitle;

    private void initView(View v) {
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        getFragmentActivity().setDrawerLock(false);
        getFragmentActivity().isCartShow(false);

        appbar = v.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = v.findViewById(R.id.appbar_title);
        imgBack = v.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        RecyclerView recyclerViewCenterList = v.findViewById(R.id.rcy_center_list);
        commonCardCategoryListAdapter = new CommonCardCategoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(commonCardCategoryListAdapter);

        centerServeice = new CenterService(getFragmentActivity(), callBack);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            CommonCardCategoryModel commonCardCategoryModel = (CommonCardCategoryModel) bundle.getSerializable("CommonCardCategoryModel");
            if (commonCardCategoryModel != null) {
                txtappBarTitle.setText(commonCardCategoryModel.name);
                centerServeice.getCenterCategories(ServiceNames.REQUEST_ID_GET_CENTER_CATEGORIES, commonCardCategoryModel.id);
                getFragmentActivity().showProgress(true);
            }
            // handle your code here.
        }

    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_CENTER_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        commonCardCategoryListAdapter.updateData(processMainCategory(jsonArray));
                    } else {
                        toastInFragment("No Data Found");
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_CENTER_SUB_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("CommonCardCategoryModel", selectedCommonCardCategoryModel);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentCenterSubCategory fragment2 = new FragmentCenterSubCategory();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putLong("type", AppCons.CENTER_LIST_TYPE_CATEGORY);
                        bundle.putSerializable("CommonCardCategoryModel", selectedCommonCardCategoryModel);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentCenterList fragment2 = new FragmentCenterList();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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

    private List<CommonCardCategoryModel> processMainCategory(JSONArray jsonArray) {
        List<CommonCardCategoryModel> commonCardCategoryModels = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                CommonCardCategoryModel commonCardCategoryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    commonCardCategoryModel = new CommonCardCategoryModel();
                    commonCardCategoryModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    commonCardCategoryModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    commonCardCategoryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");


                    commonCardCategoryModels.add(commonCardCategoryModel);

                }

            }
            return commonCardCategoryModels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonCardCategoryModels;
    }


    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
              /*  case R.id.lnr_trainer_see_all: {

                    break;
                }*/
                case R.id.img_back: {
                    goback();
                    break;
                }

            }
        }
    };
    private CommonCardCategoryModel selectedCommonCardCategoryModel;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                CommonCardCategoryModel CommonCardCategoryModel = null;
                try {
                    CommonCardCategoryModel = (CommonCardCategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CommonCardCategoryModel != null) {
                    FilterModel filterModel = new FilterModel();
                    filterModel.id = CommonCardCategoryModel.id;
                    filterModel.title = CommonCardCategoryModel.name;
                    LocalCache.getCache().getCenterFilterModel().category = filterModel;
                    selectedCommonCardCategoryModel = CommonCardCategoryModel;
                    centerServeice.getCenterSubCategories(ServiceNames.REQUEST_ID_GET_CENTER_SUB_CATEGORIES, CommonCardCategoryModel.id);

                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });

    private void goback() {
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

}