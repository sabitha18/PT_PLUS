package com.pt_plus.Activitys;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.pt_plus.Adapter.FilterCategoryListAdapter;
import com.pt_plus.Adapter.FilterRecliclerviewListAdapter;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityTrainerFilter extends SuperActivity {
    private View appbar, appBarLayout;
    private CardView cardViewAPply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trainer_filter);

        cardViewAPply = findViewById(R.id.carview_apply);
        cardViewAPply.setOnClickListener(_click);

        appbar = findViewById(R.id.appbar);
//        appBarLayout = findViewById(R.id.appBarLayout);
        setStatusBarHight(appbar);
//        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) appBarLayout.getLayoutParams();
//        p.setScrollFlags(0);






        initView();
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.carview_apply: {
                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("result",result);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                    break;
                }


            }
        }
    };

    TrainerService trainerService;

    FilterRecliclerviewListAdapter filterGridAdapterByTrainingType, filterGridAdapterByActivity, filterGridAdapterByIndoor, filterGridAdapterByGenter;

    private void initView() {
        trainerService = new TrainerService(this, callBack);
        trainerService.getTrinerFilterServiceType(ServiceNames.REQUEST_ID_TRINER_FILTER_SERVICE_TYPE);
        trainerService.getMainCateGories(ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES);
        trainerService.getTrinerFilterCategories(ServiceNames.REQUEST_ID_TRINER_FILTER_CATEGORIES);


        RecyclerView galleryGridView = findViewById(R.id.gridview_filter_by_activity);
        filterGridAdapterByActivity = new FilterRecliclerviewListAdapter(this, null, _handler);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        galleryGridView.setLayoutManager(mLayoutManager);

        galleryGridView.setAdapter(filterGridAdapterByActivity);

        RecyclerView gridview_filter_indoor = findViewById(R.id.gridview_filter_indoor);
        filterGridAdapterByIndoor = new FilterRecliclerviewListAdapter(this, null, _handler);

        RecyclerView.LayoutManager gridview_filter_indoorLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridview_filter_indoor.setLayoutManager(gridview_filter_indoorLayoutManager);
        gridview_filter_indoor.setAdapter(filterGridAdapterByIndoor);

        RecyclerView gridview_filter_trining = findViewById(R.id.gridview_filter_trining);
        filterGridAdapterByTrainingType = new FilterRecliclerviewListAdapter(this, null, _handler);

        RecyclerView.LayoutManager gridview_filter_triningLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridview_filter_trining.setLayoutManager(gridview_filter_triningLayoutManager);
        gridview_filter_trining.setAdapter(filterGridAdapterByTrainingType);


        RecyclerView gridview_filter_by_genter = findViewById(R.id.gridview_filter_by_genter);
        filterGridAdapterByGenter = new FilterRecliclerviewListAdapter(this, null, _handler);
        RecyclerView.LayoutManager gridview_filter_by_genterLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridview_filter_by_genter.setLayoutManager(gridview_filter_by_genterLayoutManager);
        gridview_filter_by_genter.setAdapter(filterGridAdapterByGenter);


        RecyclerView recyclerViewCenterList = findViewById(R.id.rcy_cate);
        filterCategoryListAdapter = new FilterCategoryListAdapter(this, null, _handler,false);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(filterCategoryListAdapter);
    }

    FilterCategoryListAdapter filterCategoryListAdapter;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_TRINER_FILTER_SERVICE_TYPE) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
//                        filterGridAdapterByActivity.updateData(proccessFilterJsonArray(getFilterJsonArray(jsonArray, "Online / Offline"),LocalCache.getCache().getTrainerFilterModel().activities));
//                        filterGridAdapterByIndoor.updateData(proccessFilterJsonArray(getFilterJsonArray(jsonArray, "Indoor / Outdoor"),LocalCache.getCache().getTrainerFilterModel().activities));
//                        filterGridAdapterByTrainingType.updateData(proccessFilterJsonArray(getFilterJsonArray(jsonArray, "Training Type"),LocalCache.getCache().getTrainerFilterModel().serviceType));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
//                        filterGridAdapterByGenter.updateData(proccesMainCategory(jsonArray,LocalCache.getCache().getTrainerFilterModel().categories));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_TRINER_FILTER_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
//                        filterCategoryListAdapter.updateData(processGategories(jsonArray,LocalCache.getCache().getTrainerFilterModel().categories));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                FilterModel filterModel = null;
                try {
                    filterModel = (FilterModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (filterModel != null) {

                    if (filterModel.group.equalsIgnoreCase("Online / Offline")) {

                        List<FilterModel> filterModelList = filterGridAdapterByActivity.getAllItems();
                        if (filterModelList != null && filterModelList.size() > 0) {
//
                            if (filterModelList.contains(filterModel)) {
                                if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                    LocalCache.getCache().getTrainerFilterModel().activities.remove(filterModel.id);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
//                                    LocalCache.getCache().getTrainerFilterModel().activities.add(filterModel.id);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                }
                            }
                            filterGridAdapterByActivity.notifyDataSetChanged();
                        }
                    } else if (filterModel.group.equalsIgnoreCase("Indoor / Outdoor")) {

                        List<FilterModel> filterModelList = filterGridAdapterByIndoor.getAllItems();
                        if (filterModelList != null && filterModelList.size() > 0) {
//
                            if (filterModelList.contains(filterModel)) {
                                if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                }
                            }
                            filterGridAdapterByIndoor.notifyDataSetChanged();
                        }
                    } else if (filterModel.group.equalsIgnoreCase("Training Type")) {

                        List<FilterModel> filterModelList = filterGridAdapterByTrainingType.getAllItems();
                        if (filterModelList != null && filterModelList.size() > 0) {
//
                            if (filterModelList.contains(filterModel)) {
                                if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                    LocalCache.getCache().getTrainerFilterModel().serviceType.remove(filterModel.id);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
//                                    LocalCache.getCache().getTrainerFilterModel().serviceType.add(filterModel.id);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                }
                            }
                            filterGridAdapterByTrainingType.notifyDataSetChanged();
                        }
                    } else if (filterModel.group.equalsIgnoreCase("genter")) {

                        List<FilterModel> filterModelList = filterGridAdapterByGenter.getAllItems();
                        if (filterModelList != null && filterModelList.size() > 0) {
//
                            if (filterModelList.contains(filterModel)) {
                                if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                }
                            }
                            filterGridAdapterByGenter.notifyDataSetChanged();
                        }
                    }

                } else {
////                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });


    private JSONArray getFilterJsonArray(JSONArray jsonArray, String type) {
        JSONArray seleccedArray = null;
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (AppUtils.getStringValueFromJson(jsonObject, "type").equalsIgnoreCase(type)) {
                        return jsonObject.getJSONArray("data");

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seleccedArray;
    }


    private List<CommonCardCategoryModel> processGategories(JSONArray jsonArray,List<String> selected) {
        List<CommonCardCategoryModel> filterModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CommonCardCategoryModel categoryModel = new CommonCardCategoryModel();
                    categoryModel.categoryId = AppUtils.getStringValueFromJson(jsonObject, "id");
                    categoryModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    JSONArray arr = jsonObject.has("sub_categories") ? jsonObject.getJSONArray("sub_categories") : null;
                    if (arr != null && arr.length() > 0) {
                        categoryModel.subCat = processMainCategory(arr,selected);
                    }
                    for (String str :selected){
                        if(str.equalsIgnoreCase(categoryModel.id)){
                            categoryModel.isSelected = true;
                        }
                    }

                    filterModelList.add(categoryModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterModelList;
    }

    private List<CommonCardCategoryModel> processMainCategory(JSONArray jsonArray,List<String> selected) {
        List<CommonCardCategoryModel> commonCardCategoryModels = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                CommonCardCategoryModel commonCardCategoryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    commonCardCategoryModel = new CommonCardCategoryModel();
                    commonCardCategoryModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    commonCardCategoryModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
//                    commonCardCategoryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");

                    for (String str :selected){
                        AppLogger.log("333333333333333333333   444433  "+commonCardCategoryModel.id);
                        if(str.equalsIgnoreCase(commonCardCategoryModel.id)){
                            commonCardCategoryModel.isSelected = true;
                        }
                    }

                    commonCardCategoryModels.add(commonCardCategoryModel);

                }

            }
            return commonCardCategoryModels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonCardCategoryModels;
    }


    private List<FilterModel> proccesMainCategory(JSONArray jsonArray,List<String> selected) {
        List<FilterModel> filterModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FilterModel filterModel = new FilterModel();
                    filterModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    filterModel.title = AppUtils.getStringValueFromJson(jsonObject, "name");
                    filterModel.group = "genter";
                    for (String str :selected){
                        if(str.equalsIgnoreCase(filterModel.id)){
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

    private List<FilterModel> proccessFilterJsonArray(JSONArray jsonArray,List<String> selected) {
        List<FilterModel> filterModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FilterModel filterModel = new FilterModel();
                    filterModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    filterModel.title = AppUtils.getStringValueFromJson(jsonObject, "title");
                    filterModel.group = AppUtils.getStringValueFromJson(jsonObject, "group");
                    for (String str :selected){

                        if(str.equalsIgnoreCase(filterModel.id)){

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}