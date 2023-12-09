package com.pt_plus.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.ProductGridAdapter;
import com.pt_plus.Dialog.BottomSheetDilaog.LangugeChageBoottomSheetDilaog;
import com.pt_plus.Dialog.BottomSheetDilaog.ProductSortBoottomSheetDilaog;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.Model.ProductFilterModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.TrainerFilterModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentProductLIst extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private StoreService storeService;
    private ProductGridAdapter productGridAdapter;
    private LinearLayout lnrFilter, lnrSortPrice;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        lnrSortPrice = view.findViewById(R.id.lnr_sort_price);
        lnrSortPrice.setOnClickListener(_click);

        lnrFilter = view.findViewById(R.id.lnr_filter);
        lnrFilter.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);


        storeService = new StoreService(getFragmentActivity(), callBack);
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                Long type = bundle.getLong("type");
                if (type == AppCons.PRODUCT_LIST_TYPE_BEST_SELLER) {
                    storeService.getAllBestSellerse(ServiceNames.REQUEST_ID_GET_ALL_BEST_SELLERS);
                    txtappBarTitle.setText(getString(R.string.shop_bestsellers));
                } else if (type == AppCons.PRODUCT_LIST_TYPE_NEW_ARRIVALS) {
                    storeService.getAllNewArrrivals(ServiceNames.REQUEST_ID_GET_ALL_NEW_ARRIVALS);
                    txtappBarTitle.setText(getString(R.string.new_arrivals));
                } else if (type == AppCons.PRODUCT_LIST_TYPE_CATEGORY) {
                    storeService.getProductByCateGory(ServiceNames.REQUEST_ID_GET_PRODUCT_BY_CATEGORY, bundle.getString("category_id"));
                    txtappBarTitle.setText(bundle.getString("category_name"));
                } else if (type == AppCons.PRODUCT_LIST_TYPE_TARINER_FAV_PRODUCTS) {
                    storeService.getTrainerFavrateProducts(ServiceNames.REQUEST_ID_TRAINER_FAV_PRODUCTS, new JSONObject().put("trainer_id", bundle.getString("trainer_id")));
                    txtappBarTitle.setText(bundle.getString("Favorite Products"));
                } else if (type == AppCons.PRODUCT_LIST_TYPE_RELATED_PRODUCTS) {
                    storeService.getTrainerFavrateProducts(ServiceNames.REQUEST_ID_TRAINER_FAV_PRODUCTS, new JSONObject().put("trainer_id", bundle.getString("product_id")));
                    txtappBarTitle.setText(bundle.getString("Related Products"));
                } else if (type == AppCons.PRODUCT_LIST_TYPE_CENTETR_PRODUCTS) {
                    storeService.getCenterProductList(ServiceNames.REQUEST_GET_CENTER_PRODUCT_LIST, new JSONObject().put("center_id", bundle.getString("center_id")));
                    txtappBarTitle.setText(bundle.getString("Center Products"));
                } else if (type == AppCons.PRODUCT_LIST_TYPE_FILTER) {
                    proceesFilter();
                }
                getFragmentActivity().showProgress(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        GridView galleryGridView = view.findViewById(R.id.gridview_products);
        productGridAdapter = new ProductGridAdapter(getContext(), _handler);
        productGridAdapter.setLayout(R.layout.layout_product_grid_list);
        galleryGridView.setNumColumns(2);
        galleryGridView.setAdapter(productGridAdapter);


    }

    private void proceesFilter() {
        try {
            JSONObject jsonObject = new JSONObject();
            ProductFilterModel productFilterModel = LocalCache.getCache().getProductFilterModel();


            if (productFilterModel.commonCardCategoryModel != null && productFilterModel.commonCardCategoryModel.size() > 0) {
                JSONArray genter = new JSONArray();
                for (CommonCardCategoryModel model : productFilterModel.commonCardCategoryModel) {
                    genter.put(model.id);
                }
                jsonObject.put("categories", genter);
            }

            if (productFilterModel.sort_price != null) {
                jsonObject.put("sort_price", productFilterModel.sort_price);
            }
            jsonObject.put("newest", productFilterModel.newest);
            jsonObject.put("featured", productFilterModel.featured);

            AppLogger.log("readasc        " + jsonObject);
            storeService.getProductByFilter(ServiceNames.REQUEST_GET_STORE_PRODUCT_FILTER, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_ALL_NEW_ARRIVALS) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        productGridAdapter.updateData(proccessProducts(jsonArray));
                    } else {
                        toastInFragment("Product list is empty");
                        goback();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_ALL_BEST_SELLERS) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        productGridAdapter.updateData(proccessProducts(jsonArray));
                    } else {
                        toastInFragment("Product list is empty");
                        goback();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_PRODUCT_BY_CATEGORY) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        productGridAdapter.updateData(proccessProducts(jsonArray));
                    } else {
                        toastInFragment("Product list is empty");
                        goback();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_RELATED_PRODUCTS) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        productGridAdapter.updateData(proccessProducts(jsonArray));
                    } else {
                        toastInFragment("Product list is empty");
                        goback();
                    }
                } else if (serviceId == ServiceNames.REQUEST_GET_CENTER_TRAINER_LIST) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        productGridAdapter.updateData(proccessProducts(jsonArray));
                    } else {
                        toastInFragment("Product list is empty");
                        goback();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_TRAINER_FAV_PRODUCTS) {

                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        productGridAdapter.updateData(proccessFavProductLIstProducts(jsonArray));
                    } else {
                        toastInFragment("Product list is empty");
                        goback();
                    }
                } else if (serviceId == ServiceNames.REQUEST_GET_STORE_PRODUCT_FILTER) {
                    AppLogger.log("tryuetwrqewyurteyue3432432");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        productGridAdapter.updateData(proccessFavProductLIstProducts(jsonArray));
                    } else {
                        toastInFragment("Product list is empty");
                        goback();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                }
                getFragmentActivity().showProgress(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<ProductModel> proccessFavProductLIstProducts(JSONArray jsonArray) {
        List<ProductModel> productModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                ProductModel productModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    productModel = new ProductModel();
                    productModel.productId = AppUtils.getStringValueFromJson(jsonObject, "product_id");
                    productModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                    productModel.price = AppUtils.getStringValueFromJson(jsonObject, "price");
                    productModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    productModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    productModelList.add(productModel);

                }

            }
            return productModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModelList;
    }

    private List<ProductModel> proccessProducts(JSONArray jsonArray) {
        List<ProductModel> productModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                ProductModel productModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    productModel = new ProductModel();
                    productModel.productId = AppUtils.getStringValueFromJson(jsonObject, "id");
                    productModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                    productModel.price = AppUtils.getStringValueFromJson(jsonObject, "price");
                    productModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    productModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    productModelList.add(productModel);

                }

            }
            return productModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModelList;
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                ProductModel productModel = null;
                try {
                    productModel = (ProductModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productModel != null) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("productModel", productModel);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductDetails fragment2 = new FragmentProductDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
////                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 4) {
                try {
                    ProductModel productModel = null;
                    try {
                        productModel = (ProductModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (productModel != null) {
                        String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");
                        if (access_token != null && !access_token.isEmpty()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("product_id", productModel.productId);
                            storeService.productAddToWishList(ServiceNames.REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST, jsonObject);
                        } else {
                            Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                            startActivityForResult(intent, 111);
                            getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                        }
                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (message.what == 5) {


                ProductModel productModel = null;
                try {
                    productModel = (ProductModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productModel != null) {
                    LocalCache.getCache().clearSelectedOrder();
                    Bundle bundle = new Bundle();
                    OrderModel orderModel = new OrderModel();
                    List<ProductModel> productModelList = new ArrayList<>();
                    productModelList.add(productModel);
                    orderModel.isSingleProductOrder = true;
                    productModel.qty = 1;
                    orderModel.productModelList = productModelList;
                    LocalCache.getCache().setSelectedOrder(orderModel);
                    bundle.putString("type", "buyNow");
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentCheckOut fragment2 = new FragmentCheckOut();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
                case R.id.lnr_filter: {
                    Bundle bundle = new Bundle();
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductFilter fragment2 = new FragmentProductFilter();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_sort_price: {
                    ProductSortBoottomSheetDilaog dialogFragment = new ProductSortBoottomSheetDilaog(getFragmentActivity(), new PopUpCallBack() {
                        @Override
                        public void onOK() {
                            proceesFilter();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
//                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");

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