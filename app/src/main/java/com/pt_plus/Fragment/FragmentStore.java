package com.pt_plus.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.ProductListAdapter;
import com.pt_plus.Adapter.ShopByCategoryStoreListAdapter;
import com.pt_plus.Adapter.StoreSliderAdapter;
import com.pt_plus.Model.CategoryModel;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;


public class FragmentStore extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        initView(view);
        return view;
    }

    private RecyclerView recyclerViewBestSeller, recyclerViewNewArrivelProductList;
    private ProductListAdapter bestSellerproductListAdapter, newArrivalProductListAdapter;
    private LinearLayout lnrShopBycategorySeeAll, lnrShopBestSellerSeeAll, lnrNewArrivalsSeeAll;
    private StoreService storeService;

    private void initView(View view) {

        getFragmentActivity().initAppbar();
        getFragmentActivity().isDefaultAppBarShow(true);
        getFragmentActivity().isCartShow(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        getFragmentActivity().setDrawerLock(false);
        initSlider(view);


        lnrShopBestSellerSeeAll = view.findViewById(R.id.lnr_shop_best_seller_see_all);
        lnrShopBestSellerSeeAll.setOnClickListener(_click);
        lnrShopBycategorySeeAll = view.findViewById(R.id.lnr_shop_by_category_see_all);
        lnrShopBycategorySeeAll.setOnClickListener(_click);

        lnrNewArrivalsSeeAll = view.findViewById(R.id.lnr_new_arrivals_see_all);
        lnrNewArrivalsSeeAll.setOnClickListener(_click);
        recyclerViewBestSeller = view.findViewById(R.id.rcy_bestselller_products);
        bestSellerproductListAdapter = new ProductListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewBestSeller.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBestSeller.setAdapter(bestSellerproductListAdapter);


        initNewArrivals(view);
        initShopByCategory(view);

        storeService = new StoreService(getFragmentActivity(), callBack);
        storeService.getAllNewArrrivals(ServiceNames.REQUEST_ID_GET_ALL_NEW_ARRIVALS);
        storeService.getAllBestSellerse(ServiceNames.REQUEST_ID_GET_ALL_BEST_SELLERS);
        storeService.getCategories(ServiceNames.REQUEST_ID_GET_ALL_CATEGORIES);
        storeService.getStoreBanner(ServiceNames.REQUEST_ID_GET_STORE_BANNER);
        getFragmentActivity().showProgress(true);

        String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");
        if (access_token != null && !access_token.isEmpty()) {
            storeService.getCart(ServiceNames.REQUEST_ID_GET_CART);
        } else {
            storeService.getGustCart(ServiceNames.REQUEST_ID_GET_GUST_CART, getFragmentActivity().deviceUID());
        }
//        handleBack();
    }
    boolean doubleBackToExitPressedOnce = false;
    private void handleBack() {
        getFragmentActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
//                    super.onBackPressed();
                    getActivity().finish();
                    System.exit(0);
                    return;
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(getFragmentActivity(), getString(R.string.go_back_exit), Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        });
    }
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_ALL_NEW_ARRIVALS) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        newArrivalProductListAdapter.updateData(proccessProducts(jsonArray));
                    }
                    getFragmentActivity().showProgress(false);
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_ALL_BEST_SELLERS) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        bestSellerproductListAdapter.updateData(proccessProducts(jsonArray));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_STORE_BANNER) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        processBanner(jsonArray);


                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_ALL_CATEGORIES) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        shopByCategoryListAdapter.updateData(proccessCateGorys(jsonArray));

                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_SUB_CATEGORY) {
                    AppLogger.log("here response session");
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("categoryModel", selectedCategoryModel);
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
                        bundle.putString("category_id", selectedCategoryModel.categoryId);
                        bundle.putString("category_name", selectedCategoryModel.name);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentProductLIst fragment2 = new FragmentProductLIst();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_CART || serviceId == ServiceNames.REQUEST_ID_GET_GUST_CART) {
                    JSONArray jsonArray = jsonObject.has("item") ? jsonObject.getJSONArray("item") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        getFragmentActivity().setCartCount(jsonArray.length());
                    } else {
                        getFragmentActivity().setCartCount(0);
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<CategoryModel> proccessCateGorys(JSONArray jsonArray) {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                CategoryModel categoryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    categoryModel = new CategoryModel();
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
                    productModel.wishlisted = AppUtils.getBooleanValueFromJson(jsonObject, "wishlisted");
                    productModelList.add(productModel);

                }

            }
            return productModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModelList;
    }

    private List<HomeSliderModel> processBanner(JSONArray jsonArray) {
        List<HomeSliderModel> homeSliderModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                HomeSliderModel homeSliderModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                    homeSliderModel = new HomeSliderModel();
                    homeSliderModel.btnText = "SHOP NOW";
                    homeSliderModel.title = AppUtils.getStringValueFromJson(jsonObject, "title");
                    homeSliderModel.description = AppUtils.getStringValueFromJson(jsonObject, "description");
                    homeSliderModel.imgUrl = AppUtils.getStringValueFromJson(jsonObject, "image");
                    homeSliderModel.related_product_id = AppUtils.getStringValueFromJson(jsonObject, "related_product_id");
//
                    homeSliderModelList.add(homeSliderModel);
                }
                viewPagerAdapter.updateData(homeSliderModelList);
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];
                for (int i = 0; i < dotscount; i++) {
                    dots[i] = new ImageView(getFragmentActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.non_active_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                if (dots.length > 0) {
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.active_dot));
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.non_active_dot));
                        }
                        dots[position].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.active_dot));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new FragmentStore.The_slide_timer(), 2000, 3000);

            }
            return homeSliderModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeSliderModelList;
    }

    private RecyclerView recyclerViewShopByCategory;
    private ShopByCategoryStoreListAdapter shopByCategoryListAdapter;

    private void initShopByCategory(View view) {

        recyclerViewShopByCategory = view.findViewById(R.id.rcy_shop_by_categoty);
        shopByCategoryListAdapter = new ShopByCategoryStoreListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewShopByCategory.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewShopByCategory.setAdapter(shopByCategoryListAdapter);


    }

    private void initNewArrivals(View view) {
        recyclerViewNewArrivelProductList = view.findViewById(R.id.rcy_new_arrival_products);
        newArrivalProductListAdapter = new ProductListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewNewArrivelProductList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNewArrivelProductList.setAdapter(newArrivalProductListAdapter);


    }

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private StoreSliderAdapter viewPagerAdapter;

    private void initSlider(View v) {

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) v.findViewById(R.id.SliderDots);
        viewPagerAdapter = new StoreSliderAdapter(getFragmentActivity(), null, _handler);
        viewPager.setAdapter(viewPagerAdapter);


    }


    private class The_slide_timer extends TimerTask {
        @Override
        public void run() {
            if(getActivity() == null)
                return;
            getFragmentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() < dotscount - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }

    private CategoryModel selectedCategoryModel;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                CategoryModel categoryModel = null;
                try {
                    categoryModel = (CategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (categoryModel != null) {
                    storeService.getSubCategorys(ServiceNames.REQUEST_ID_GET_SUB_CATEGORY, categoryModel.categoryId);
                    selectedCategoryModel = categoryModel;


                    AppLogger.log("1111111             " + categoryModel);
                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 2) {

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
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }


            } else if (message.what == 3) {

                HomeSliderModel homeSliderModel = null;
                try {
                    homeSliderModel = (HomeSliderModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (homeSliderModel != null) {
                    ProductModel productModel = new ProductModel();
                    productModel.productId = homeSliderModel.related_product_id;
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
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
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
                    productModel.qty = 1;
                    productModelList.add(productModel);
                    orderModel.isSingleProductOrder = true;
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
                case R.id.lnr_shop_by_category_see_all: {
                    LocalCache.getCache().getProductFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getProductFilterModel().sort_price = null;
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentShopByCategory());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_shop_best_seller_see_all: {
                    LocalCache.getCache().getProductFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getProductFilterModel().sort_price = null;
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_BEST_SELLER);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductLIst fragment2 = new FragmentProductLIst();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_new_arrivals_see_all: {
                    LocalCache.getCache().getProductFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getProductFilterModel().sort_price = null;
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_NEW_ARRIVALS);
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
}