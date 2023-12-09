package com.pt_plus.Fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Adapter.GeneralTabListAdapter;
import com.pt_plus.Model.CategoryModel;
import com.pt_plus.Model.CenterFilterModel;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FragmentCenterList extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center_list, container, false);
        initView(view);
        return view;
    }

    private View appbar;
    private ImageView imgBack;
    RadioGroup radioGroup;
    private TextView txtappBarTitle, txtNodata;
    private CenterService centerService;
    private TrainerService trainerService;

    private void initView(View view) {
        txtNodata = view.findViewById(R.id.txt_no_data);
        RecyclerView rcy_genter = view.findViewById(R.id.rcy_genter);
        getFragmentActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 110);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText("GYM Centers");
        generalTabListAdapter = new GeneralTabListAdapter(getFragmentActivity(), null, _handler);
        rcy_genter.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcy_genter.setAdapter(generalTabListAdapter);

        RecyclerView recyclerViewCenterList = view.findViewById(R.id.rcy_center_list);
        commonCardCategoryListAdapter = new CommonCardCategoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(commonCardCategoryListAdapter);
        trainerService = new TrainerService(getFragmentActivity(), callBack);
        trainerService.getMainCateGories(ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES);
        centerService = new CenterService(getFragmentActivity(), callBack);


        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                Long type = bundle.getLong("type");
                if (type == AppCons.CENTER_LIST_TYPE_CATEGORY) {
                    CommonCardCategoryModel commonCardCategoryModel = (CommonCardCategoryModel) bundle.getSerializable("CommonCardCategoryModel");
                    if (commonCardCategoryModel != null) {
                        txtappBarTitle.setText(commonCardCategoryModel.name);
                        getCenterList();

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCenterList();


    }

    private void getCenterList() {
        try {
            JSONObject jsonObject = new JSONObject();
            AppLogger.log("dfadsfadsfasdfdads");
            CenterFilterModel centerFilterModel = LocalCache.getCache().getCenterFilterModel();
            if (centerFilterModel.category != null && !centerFilterModel.category.id.isEmpty()) {
                jsonObject.put("category", centerFilterModel.category.id);
            }
            if (centerFilterModel.main_category != null && !centerFilterModel.main_category.id.isEmpty()) {
                jsonObject.put("main_category", centerFilterModel.main_category.id);
            }
            if (centerFilterModel.type != null && !centerFilterModel.type.id.isEmpty()) {
                jsonObject.put("type", centerFilterModel.type.id);
            }

            centerService.getCenterList(ServiceNames.REQUEST_ID_GET_CENTER_LIST, jsonObject);
            getFragmentActivity().showProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    GeneralTabListAdapter generalTabListAdapter;
    CommonCardCategoryListAdapter commonCardCategoryListAdapter;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_CENTER_LIST) {
//
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {


                        commonCardCategoryListAdapter.updateData(processMainCategory(jsonArray));
                        txtNodata.setVisibility(View.GONE);
                    } else {
                        if (commonCardCategoryListAdapter.getItemCount() > 0) {
                            commonCardCategoryListAdapter.getAllItems().clear();
                        }
                        commonCardCategoryListAdapter.notifyDataSetChanged();

                        txtNodata.setVisibility(View.VISIBLE);
                    }
                    getFragmentActivity().showProgress(false);
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        generalTabListAdapter.updateData(proccesMainCategory(jsonArray, LocalCache.getCache().getCenterFilterModel().main_category));
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

    private List<FilterModel> proccesMainCategory(JSONArray jsonArray, FilterModel selected) {
        List<FilterModel> filterModelList = new ArrayList<>();
        try {
            FilterModel filterModel = null;
            filterModel = new FilterModel();
            filterModel.id = "";
            filterModel.title = "All";
            filterModel.group = "genter";
            if (selected != null && selected.id.isEmpty()) {
                filterModel.isSelected = true;
            }
            filterModelList.add(filterModel);

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    filterModel = new FilterModel();
                    filterModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    filterModel.title = AppUtils.getStringValueFromJson(jsonObject, "name");
                    filterModel.group = "genter";
                    if (selected != null) {
                        if (selected.equals(filterModel)) {
                            filterModel.isSelected = true;
                        }
                    }


                    filterModelList.add(filterModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterModelList;
    }

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

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

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
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                CommonCardCategoryModel commonCardCategoryModel = null;
                try {
                    commonCardCategoryModel = (CommonCardCategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (commonCardCategoryModel != null) {


                    Bundle bundle = new Bundle();
                    bundle.putString("id", commonCardCategoryModel.id);

                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentCenterDetails fragment2 = new FragmentCenterDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 124) {


                FilterModel filterModel = null;
                try {
                    filterModel = (FilterModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (filterModel != null) {
                    LocalCache.getCache().getTrainerFilterModel().genter.clear();
                    List<FilterModel> list = generalTabListAdapter.getAllItems();


                    for (FilterModel item : list) {
                        list.get(list.indexOf(item)).isSelected = false;
                    }
                    if (!filterModel.id.isEmpty()) {
                        LocalCache.getCache().getCenterFilterModel().main_category = filterModel;
                    }

                    list.get(list.indexOf(filterModel)).isSelected = true;
                    generalTabListAdapter.notifyDataSetChanged();
                    getCenterList();


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });

}