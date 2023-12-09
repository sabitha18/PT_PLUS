package com.pt_plus.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.RangeSlider.OnChangeListener;
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
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentTrainerFilter extends SuperFragment {
    private View appbar, appBarLayout;
    private CardView cardViewAPply,carviewClear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_trainer_filter, container, false);
        initView(view);
        return view;
    }


    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.carview_apply: {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", AppCons.TRAINER_LIST_TYPE_FILTER);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerList fragment2 = new FragmentTrainerList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                } case R.id.img_back: {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", AppCons.TRAINER_LIST_TYPE_FILTER);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerList fragment2 = new FragmentTrainerList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }case R.id.carview_clear: {
                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = false;


                    LocalCache.getCache().getTrainerFilterModel().genter.clear();
                    LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getTrainerFilterModel().serviceType.clear();
                    LocalCache.getCache().getTrainerFilterModel().price.clear();
                    LocalCache.getCache().getTrainerFilterModel().experience.clear();
                    LocalCache.getCache().getTrainerFilterModel().activities.clear();
                    LocalCache.getCache().getTrainerFilterModel().rating = null;
                    LocalCache.getCache().getTrainerFilterModel().availability.clear();
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.TRAINER_LIST_TYPE_ALL);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerList fragment2 = new FragmentTrainerList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }


            }
        }
    };

    TrainerService trainerService;

    FilterRecliclerviewListAdapter filterGridAdapterByTrainingType, filterGridAdapterByActivity, filterGridAdapterByIndoor, filterGridAdapterByGenter,getFilterGridAdapterByAvaliblity;

    private TextView txtFilterPrice,txtFIlterExp,txtFiltertByActivity;
    private RangeSlider rangeSliderPrice,rangeSliderFilterExp;
    private ImageView imgBack;
    private TextView txtFilterByAvailablity,txtFilterByindorOutDoor,txtFilterByTrainingType;
    private void initView(View view) {
        txtFilterByindorOutDoor = view.findViewById(R.id.txt_filter_by_indorr_out_door);
        txtFilterByAvailablity = view.findViewById(R.id.txt_filter_by_availability);
        txtFiltertByActivity = view.findViewById(R.id.txt_filter_by_activity);
        txtFilterByTrainingType = view.findViewById(R.id.txt_filter_by_training_type);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        carviewClear = view.findViewById(R.id.carview_clear);
        carviewClear.setOnClickListener(_click);
        txtFIlterExp = view.findViewById(R.id.txt_filter_exp);
        rangeSliderFilterExp = view.findViewById(R.id.slider_exp);
        txtFilterPrice = view.findViewById(R.id.txt_filter_price);
        rangeSliderPrice = view.findViewById(R.id.silder_price);
        if(LocalCache.getCache().getTrainerFilterModel().price.size() == 2){
            rangeSliderPrice.setValues(Float.parseFloat(LocalCache.getCache().getTrainerFilterModel().price.get(0)),Float.parseFloat(LocalCache.getCache().getTrainerFilterModel().price.get(1)));
            txtFilterPrice.setText(LocalCache.getCache().getTrainerFilterModel().price.get(0) +" - "+LocalCache.getCache().getTrainerFilterModel().price.get(1)+" KWD");
        }else {
            rangeSliderPrice.setValues(00.f,1000.0f);
            txtFilterPrice.setText("00 - 1000  KWD");
        }
        rangeSliderPrice.setValueFrom(0); // Minimum value of the range
        rangeSliderPrice.setValueTo(1000); // Maximum value of the range


        rangeSliderPrice.addOnChangeListener(new OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> price = rangeSliderPrice.getValues();
                LocalCache.getCache().getTrainerFilterModel().price.clear();
                LocalCache.getCache().getTrainerFilterModel().price.add(String.format("%.2f", price.get(0)));
                LocalCache.getCache().getTrainerFilterModel().price.add(1,String.format("%.2f", price.get(1)));
                txtFilterPrice.setText(String.format("%.2f", price.get(0)) +" - "+String.format("%.2f", price.get(1))+" KWD");
            }
        });



        if(LocalCache.getCache().getTrainerFilterModel().experience.size() == 2){
            rangeSliderFilterExp.setValues(Float.parseFloat(LocalCache.getCache().getTrainerFilterModel().experience.get(0)),Float.parseFloat(LocalCache.getCache().getTrainerFilterModel().experience.get(1)));
            txtFIlterExp.setText(LocalCache.getCache().getTrainerFilterModel().experience.get(0) +" - "+LocalCache.getCache().getTrainerFilterModel().experience.get(1)+" years");

        }else {

            rangeSliderFilterExp.setValues(00.f,20.0f);
            txtFIlterExp.setText("0 - 20 years");
        }
        rangeSliderFilterExp.setValueFrom(0); // Minimum value of the range
        rangeSliderFilterExp.setValueTo(20.f); // Maximum value of the range

        rangeSliderFilterExp.addOnChangeListener(new OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> price = rangeSliderFilterExp.getValues();
                LocalCache.getCache().getTrainerFilterModel().experience.clear();
                LocalCache.getCache().getTrainerFilterModel().experience.add(String.format("%.2f", price.get(0)));
                LocalCache.getCache().getTrainerFilterModel().experience.add(1,String.format("%.2f", price.get(1)));
                txtFIlterExp.setText(String.format("%.2f", price.get(0)) +" - "+String.format("%.2f", price.get(1))+" years");
            }
        });


        cardViewAPply = view.findViewById(R.id.carview_apply);
        cardViewAPply.setOnClickListener(_click);

        appbar = view.findViewById(R.id.appbar);
//        appBarLayout = findViewById(R.id.appBarLayout);
        getFragmentActivity().setStatusBarHight(appbar);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);

        trainerService = new TrainerService(getFragmentActivity(), callBack);
        trainerService.getTrinerFilterServiceType(ServiceNames.REQUEST_ID_TRINER_FILTER_SERVICE_TYPE);
        trainerService.getMainCateGories(ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES);
        trainerService.getTrinerFilterCategories(ServiceNames.REQUEST_ID_TRINER_FILTER_CATEGORIES);


        RecyclerView galleryGridView = view.findViewById(R.id.gridview_filter_by_activity);
        filterGridAdapterByActivity = new FilterRecliclerviewListAdapter(getFragmentActivity(), null, _handler);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getFragmentActivity(), 2);
        galleryGridView.setLayoutManager(mLayoutManager);

        galleryGridView.setAdapter(filterGridAdapterByActivity);

        RecyclerView gridview_filter_indoor = view.findViewById(R.id.gridview_filter_indoor);
        filterGridAdapterByIndoor = new FilterRecliclerviewListAdapter(getFragmentActivity(), null, _handler);

        RecyclerView.LayoutManager gridview_filter_indoorLayoutManager = new GridLayoutManager(getFragmentActivity(), 2);
        gridview_filter_indoor.setLayoutManager(gridview_filter_indoorLayoutManager);
        gridview_filter_indoor.setAdapter(filterGridAdapterByIndoor);

        RecyclerView gridview_filter_trining = view.findViewById(R.id.gridview_filter_trining);
        filterGridAdapterByTrainingType = new FilterRecliclerviewListAdapter(getFragmentActivity(), null, _handler);

        RecyclerView.LayoutManager gridview_filter_triningLayoutManager = new GridLayoutManager(getFragmentActivity(), 2);
        gridview_filter_trining.setLayoutManager(gridview_filter_triningLayoutManager);
        gridview_filter_trining.setAdapter(filterGridAdapterByTrainingType);


        RecyclerView gridview_filter_by_genter = view.findViewById(R.id.gridview_filter_by_genter);
        filterGridAdapterByGenter = new FilterRecliclerviewListAdapter(getFragmentActivity(), null, _handler);
        RecyclerView.LayoutManager gridview_filter_by_genterLayoutManager = new GridLayoutManager(getFragmentActivity(), 2);
        gridview_filter_by_genter.setLayoutManager(gridview_filter_by_genterLayoutManager);
        gridview_filter_by_genter.setAdapter(filterGridAdapterByGenter);


        RecyclerView gridview_filter_by_avilablity = view.findViewById(R.id.gridview_filter_by_avilablity);
        getFilterGridAdapterByAvaliblity = new FilterRecliclerviewListAdapter(getFragmentActivity(), null, _handler);
        RecyclerView.LayoutManager gridview_filter_by_avilablityLayoutManager = new GridLayoutManager(getFragmentActivity(), 2);
        gridview_filter_by_avilablity.setLayoutManager(gridview_filter_by_avilablityLayoutManager);
        gridview_filter_by_avilablity.setAdapter(getFilterGridAdapterByAvaliblity);


        RecyclerView recyclerViewCenterList = view.findViewById(R.id.rcy_cate);
        filterCategoryListAdapter = new FilterCategoryListAdapter(getFragmentActivity(), null, _handler,false);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(filterCategoryListAdapter);
        getFragmentActivity().showProgress(true);
    }

    FilterCategoryListAdapter filterCategoryListAdapter;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
//                vdsfdsdssad
                if (serviceId == ServiceNames.REQUEST_ID_TRINER_FILTER_SERVICE_TYPE) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        txtFiltertByActivity.setText(R.string.filter_by_online_offline);
                        txtFilterByAvailablity.setText(R.string.filter_by_availability_new);
                        txtFilterByindorOutDoor.setText(R.string.filter_by_indoor_outdoor_new);
                        txtFilterByTrainingType.setText(R.string.filter_by_training_type_new);
                        filterGridAdapterByActivity.updateData(proccessFilterJsonArray(getFilterJsonArray(jsonArray, getString(R.string.filter_by_online_offline)),LocalCache.getCache().getTrainerFilterModel().activities));
                        filterGridAdapterByIndoor.updateData(proccessFilterJsonArray(getFilterJsonArray(jsonArray, getString(R.string.filter_by_indoor_outdoor_new)),LocalCache.getCache().getTrainerFilterModel().activities));
                        filterGridAdapterByTrainingType.updateData(proccessFilterJsonArray(getFilterJsonArray(jsonArray,  getString(R.string.filter_by_training_type_new)),LocalCache.getCache().getTrainerFilterModel().serviceType));

                        getFilterGridAdapterByAvaliblity.updateData(proccessFilterJsonArray(getFilterJsonArray(jsonArray,  getString(R.string.filter_by_availability_new)),LocalCache.getCache().getTrainerFilterModel().availability));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        filterGridAdapterByGenter.updateData(proccesMainCategory(jsonArray,LocalCache.getCache().getTrainerFilterModel().genter));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_TRINER_FILTER_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        filterCategoryListAdapter.updateData(processGategories(jsonArray,LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel));
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

                    if (filterModel.group.equalsIgnoreCase(getString(R.string.filter_by_online_offline))) {

                        List<FilterModel> filterModelList = filterGridAdapterByActivity.getAllItems();
                        if (filterModelList != null && filterModelList.size() > 0) {
//
                            if (filterModelList.contains(filterModel)) {
                                if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                    LocalCache.getCache().getTrainerFilterModel().serviceType.remove(filterModel);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
                                    LocalCache.getCache().getTrainerFilterModel().serviceType.add(filterModel);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                }
                            }
                            filterGridAdapterByActivity.notifyDataSetChanged();
                        }
                    } else if (filterModel.group.equalsIgnoreCase(getString(R.string.filter_by_indoor_outdoor_new))) {

                        List<FilterModel> filterModelList = filterGridAdapterByIndoor.getAllItems();
                        if (filterModelList != null && filterModelList.size() > 0) {
//
                            if (filterModelList.contains(filterModel)) {
                                if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                    LocalCache.getCache().getTrainerFilterModel().serviceType.remove(filterModel);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
                                    LocalCache.getCache().getTrainerFilterModel().serviceType.add(filterModel);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                }
                            }
                            filterGridAdapterByIndoor.notifyDataSetChanged();
                        }
                    } else if (filterModel.group.equalsIgnoreCase(getString(R.string.filter_by_training_type_new))) {

                        List<FilterModel> filterModelList = filterGridAdapterByTrainingType.getAllItems();
                        if (filterModelList != null && filterModelList.size() > 0) {
//
                            if (filterModelList.contains(filterModel)) {
                                if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                    LocalCache.getCache().getTrainerFilterModel().serviceType.remove(filterModel);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
                                    LocalCache.getCache().getTrainerFilterModel().serviceType.add(filterModel);
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
                                    LocalCache.getCache().getTrainerFilterModel().genter.remove(filterModel);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                } else {
                                    LocalCache.getCache().getTrainerFilterModel().genter.add(filterModel);
                                    filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                }
                            }
                            filterGridAdapterByGenter.notifyDataSetChanged();
                        }
                    }else if (filterModel.group.equalsIgnoreCase(getString(R.string.filter_by_availability_new))) {

                    List<FilterModel> filterModelList = getFilterGridAdapterByAvaliblity.getAllItems();
                    if (filterModelList != null && filterModelList.size() > 0) {
//
                        if (filterModelList.contains(filterModel)) {
                            if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {
                                LocalCache.getCache().getTrainerFilterModel().availability.remove(filterModel);
                                filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                            } else {
                                LocalCache.getCache().getTrainerFilterModel().availability.add(filterModel);
                                filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                            }
                        }
                        getFilterGridAdapterByAvaliblity.notifyDataSetChanged();
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


    private List<CommonCardCategoryModel> processGategories(JSONArray jsonArray,List<CommonCardCategoryModel> selected) {
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
//                    for (String str :selected){
//                        if(str.equalsIgnoreCase(categoryModel.id)){
//                            categoryModel.isSelected = true;
//                        }
//                    }
                    filterModelList.add(categoryModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterModelList;
    }

    private List<CommonCardCategoryModel> processMainCategory(JSONArray jsonArray,List<CommonCardCategoryModel> selected) {
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


                    for (CommonCardCategoryModel str :selected){
                        if(str.equals(commonCardCategoryModel)){
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


    private List<FilterModel> proccesMainCategory(JSONArray jsonArray,List<FilterModel> selected) {
        List<FilterModel> filterModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FilterModel filterModel = new FilterModel();
                    filterModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    filterModel.title = AppUtils.getStringValueFromJson(jsonObject, "name");
                    filterModel.group = "genter";
                    AppLogger.log("444444444          "+   selected.size());
                    for (FilterModel str :selected){
                        AppLogger.log("666666666          "+   str.title);
                        if(str.equals(filterModel)){

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

    private List<FilterModel> proccessFilterJsonArray(JSONArray jsonArray,List<FilterModel> selected) {
        List<FilterModel> filterModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FilterModel filterModel = new FilterModel();
                    filterModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    filterModel.title = AppUtils.getStringValueFromJson(jsonObject, "title");
                    filterModel.group = AppUtils.getStringValueFromJson(jsonObject, "group");
                    for (FilterModel str :selected){

                        if(str.equals(filterModel)){
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


}