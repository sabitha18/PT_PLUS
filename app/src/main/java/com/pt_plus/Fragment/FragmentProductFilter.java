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
import com.pt_plus.Service.StoreService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentProductFilter extends SuperFragment {
    private View appbar, appBarLayout;
    private CardView cardViewAPply, carViewClear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_filter, container, false);
        initView(view);
        return view;
    }


    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.carview_apply: {
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_FILTER);

                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductLIst fragment2 = new FragmentProductLIst();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.img_back: {
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_FILTER);

                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductLIst fragment2 = new FragmentProductLIst();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    break;
                }
                case R.id.carview_clear: {
                    LocalCache.getCache().getProductFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getProductFilterModel().sort_price = null;

                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_FILTER);

                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductLIst fragment2 = new FragmentProductLIst();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    break;
                }


            }
        }
    };

    StoreService storeService;

    FilterRecliclerviewListAdapter filterGridAdapterByTrainingType, filterGridAdapterByActivity, filterGridAdapterByIndoor, filterGridAdapterByGenter, getFilterGridAdapterByAvaliblity;

    private TextView txtFilterPrice, txtFIlterExp;
    private RangeSlider rangeSliderPrice, rangeSliderFilterExp;
    private ImageView imgBack;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        carViewClear = view.findViewById(R.id.carview_clear);
        carViewClear.setOnClickListener(_click);
        txtFIlterExp = view.findViewById(R.id.txt_filter_exp);
        rangeSliderFilterExp = view.findViewById(R.id.slider_exp);
        txtFilterPrice = view.findViewById(R.id.txt_filter_price);
        rangeSliderPrice = view.findViewById(R.id.silder_price);


        cardViewAPply = view.findViewById(R.id.carview_apply);
        cardViewAPply.setOnClickListener(_click);

        appbar = view.findViewById(R.id.appbar);
//        appBarLayout = findViewById(R.id.appBarLayout);
        getFragmentActivity().setStatusBarHight(appbar);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);


        storeService = new StoreService(getFragmentActivity(), callBack);
        storeService.getStoreFilterCategories(ServiceNames.REQUEST_GET_STORE_FILTER_CATEGORIES);


        RecyclerView recyclerViewCenterList = view.findViewById(R.id.rcy_cate);
        filterCategoryListAdapter = new FilterCategoryListAdapter(getFragmentActivity(), null, _handler, true);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(filterCategoryListAdapter);
        getFragmentActivity().showProgress(true);
    }

    FilterCategoryListAdapter filterCategoryListAdapter;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_GET_STORE_FILTER_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        filterCategoryListAdapter.updateData(processGategories(jsonArray, LocalCache.getCache().getProductFilterModel().commonCardCategoryModel));
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


            // Your code logic goes here.
            return true;
        }
    });


    private List<CommonCardCategoryModel> processGategories(JSONArray jsonArray, List<CommonCardCategoryModel> selected) {
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
                        categoryModel.subCat = processMainCategory(arr, selected);
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

    private List<CommonCardCategoryModel> processMainCategory(JSONArray jsonArray, List<CommonCardCategoryModel> selected) {
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


                    for (CommonCardCategoryModel str : selected) {
                        if (str.equals(commonCardCategoryModel)) {
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


}