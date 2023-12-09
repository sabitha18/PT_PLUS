package com.pt_plus.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pt_plus.Adapter.ProductGridAdapter;
import com.pt_plus.Adapter.SubCategoryGridAdapter;
import com.pt_plus.Model.CategoryModel;
import com.pt_plus.Model.ProductModel;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSubCategoryLIst#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSubCategoryLIst extends SuperFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSubCategoryLIst() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentShopByCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSubCategoryLIst newInstance(String param1, String param2) {
        FragmentSubCategoryLIst fragment = new FragmentSubCategoryLIst();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_category_list, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private StoreService storeService;
    private SubCategoryGridAdapter subCategoryGridAdapter;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);


        storeService = new StoreService(getFragmentActivity(), callBack);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            CategoryModel categoryModel = (CategoryModel) bundle.getSerializable("categoryModel");
            if (categoryModel != null) {
                storeService.getSubCategorys(ServiceNames.REQUEST_ID_GET_SUB_CATEGORY,categoryModel.categoryId);
                txtappBarTitle.setText(categoryModel.name);
            }
            getFragmentActivity().showProgress(true);
            // handle your code here.
        }



        GridView galleryGridView = view.findViewById(R.id.gridview_sub_category_list);
        subCategoryGridAdapter = new SubCategoryGridAdapter(getContext(), _handler);
        subCategoryGridAdapter.setLayout(R.layout.layout_sub_category_grid_list);
        galleryGridView.setNumColumns(3);
        galleryGridView.setAdapter(subCategoryGridAdapter);


    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject,int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_SUB_CATEGORY) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        subCategoryGridAdapter.updateData(proccessCateGorys(jsonArray));
                    }
                }
                getFragmentActivity().showProgress(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage,int statusCode) {

        }
    };
    private List<CategoryModel> proccessCateGorys(JSONArray jsonArray){
        List<CategoryModel> categoryModelList = new ArrayList<>();
        try {
            if(jsonArray != null && jsonArray.length() > 0){

                CategoryModel categoryModel = null;
                for (int i= 0;i<jsonArray.length() ; i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    categoryModel = new CategoryModel();
                    categoryModel.categoryId = AppUtils.getStringValueFromJson(jsonObject,"id");

                    categoryModel.name = AppUtils.getStringValueFromJson(jsonObject,"name");
                    categoryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject,"thumbnail_img");
                    categoryModelList.add(categoryModel);

                }

            }
            return  categoryModelList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  categoryModelList;
    }


    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                CategoryModel categoryModel= null;
                try {
                    categoryModel = (CategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (categoryModel != null) {

                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_CATEGORY);
                    bundle.putString("category_id", categoryModel.categoryId);
                    bundle.putString("category_name", categoryModel.name);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductLIst fragment2 = new FragmentProductLIst();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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