package com.pt_plus.Fragment;

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
import android.widget.TextView;

import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Model.CategoryModel;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FragmentShopByCategory extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_by_category, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private RecyclerView recyclerViewShopByCategory;
    private CommonCardCategoryListAdapter commonCardCategoryListAdapter;
    private StoreService storeService;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(getFragmentActivity().getString(R.string.shop_by_category));


        recyclerViewShopByCategory = view.findViewById(R.id.rcy_shop_by_categoty);
        commonCardCategoryListAdapter = new CommonCardCategoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewShopByCategory.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewShopByCategory.setAdapter(commonCardCategoryListAdapter);

        commonCardCategoryListAdapter.notifyDataSetChanged();

        storeService = new StoreService(getFragmentActivity(), callBack);

        storeService.getCategories(ServiceNames.REQUEST_ID_GET_ALL_CATEGORIES);
        getFragmentActivity().showProgress(true);
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject,int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_ALL_CATEGORIES) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        commonCardCategoryListAdapter.updateData(proccessCateGorys(jsonArray));

                    }
                    getFragmentActivity().showProgress(false);
                }else if (serviceId == ServiceNames.REQUEST_ID_GET_SUB_CATEGORY) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("categoryModel", selectedCategoryModal);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentSubCategoryLIst fragment2 = new FragmentSubCategoryLIst();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_CATEGORY);
                        bundle.putString("category_id", selectedCategoryModal.categoryId);
                        bundle.putString("category_name", selectedCategoryModal.name);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentProductLIst fragment2 = new FragmentProductLIst();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage,int statusCode) {

        }
    };

    private List<CommonCardCategoryModel> proccessCateGorys(JSONArray jsonArray) {
        List<CommonCardCategoryModel> categoryModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                CommonCardCategoryModel categoryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    categoryModel = new CommonCardCategoryModel();
                    categoryModel.categoryId = AppUtils.getStringValueFromJson(jsonObject, "id");

                    categoryModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    categoryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    categoryModelList.add(categoryModel);

                }

            }
            return categoryModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryModelList;
    }

    private CategoryModel selectedCategoryModal;
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

                    selectedCategoryModal = new CategoryModel();
                    selectedCategoryModal.categoryId = commonCardCategoryModel.categoryId;
                    selectedCategoryModal.name = commonCardCategoryModel.name;

                    storeService.getSubCategorys(ServiceNames.REQUEST_ID_GET_SUB_CATEGORY, selectedCategoryModal.categoryId);


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });


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
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}