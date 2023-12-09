package com.pt_plus.Fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.ProductDetailsImagesListAdapter;
import com.pt_plus.Adapter.ProductDitailsSliderAdapter;
import com.pt_plus.Adapter.ProductListAdapter;
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

import ru.nikartm.support.ImageBadgeView;


public class FragmentProductDetails extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    ViewPager viewPager;
    private CardView cardViewAddToCart;
    private StoreService storeService;
    private ProductDitailsSliderAdapter productDitailsSliderAdapter;
    private ProductDetailsImagesListAdapter productDetailsImagesListAdapter;
    private TextView txtProductName, txtPrice, txtDiscription, txtQty;
    private ProductListAdapter productListAdapter;
    private ImageView imgAddQty, imgLessQty, imgFav;
    private ImageBadgeView imageBadgeView;
    private LinearLayout lnrAddToWishList, lnrSeeALlRelatedProduct;

    private void initView(View view) {
        lnrSeeALlRelatedProduct = view.findViewById(R.id.lnr_see_all_related_product);
        lnrSeeALlRelatedProduct.setOnClickListener(_click);
        imgFav = view.findViewById(R.id.img_fav);
        txtQty = view.findViewById(R.id.txt_qty);
        txtDiscription = view.findViewById(R.id.txt_description);
        txtProductName = view.findViewById(R.id.txt_product_name);
        txtPrice = view.findViewById(R.id.txt_price);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        lnrAddToWishList = view.findViewById(R.id.lnr_add_to_whist_list);
        lnrAddToWishList.setOnClickListener(_click);

        imageBadgeView = view.findViewById(R.id.img_cart);
        imageBadgeView.setOnClickListener(_click);
        imgAddQty = view.findViewById(R.id.img_add_qty);
        imgAddQty.setOnClickListener(_click);

        imgLessQty = view.findViewById(R.id.img_less_qty);
        imgLessQty.setOnClickListener(_click);

        cardViewAddToCart = view.findViewById(R.id.carview_add_to_cart);
        cardViewAddToCart.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        storeService = new StoreService(getFragmentActivity(), callBack);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ProductModel productModel = (ProductModel) bundle.getSerializable("productModel");
            if (productModel != null) {
                storeService.getProductDetails(ServiceNames.REQUEST_ID_GET_PRODUCT_DETAILS, productModel.productId);

            }
            getFragmentActivity().showProgress(true);
            // handle your code here.
        }
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        RecyclerView recyclerViewShopByCategory = view.findViewById(R.id.rcy_product_images);
        productDetailsImagesListAdapter = new ProductDetailsImagesListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewShopByCategory.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewShopByCategory.setAdapter(productDetailsImagesListAdapter);


        try {
//            viewPager.getCurrentItem()
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    Log.d("KOT", "dfadsfdssadfdsfsdafdsafds");
                    productDetailsImagesListAdapter.currentPostion(viewPager.getCurrentItem());
                    productDetailsImagesListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        RecyclerView recyclerViewRelatedProducts = view.findViewById(R.id.rcy_related_products);
        productListAdapter = new ProductListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewRelatedProducts.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRelatedProducts.setAdapter(productListAdapter);


        getCart();


    }

    private void getCart() {
        String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");
        if (access_token != null && !access_token.isEmpty()) {
            storeService.getCart(ServiceNames.REQUEST_ID_GET_CART);
        } else {
            storeService.getGustCart(ServiceNames.REQUEST_ID_GET_GUST_CART, getFragmentActivity().deviceUID());
        }
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 2) {

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


            } else if (message.what == 4) {
                try {
                    ProductModel productModel = null;
                    try {
                        productModel = (ProductModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (productModel != null) {
                        addToWishList(productModel.productId);

                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }else if (message.what == 5) {


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
            }else if (message.what == 4) {
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
                case R.id.img_add_qty: {
                    productQty(1);
                    break;
                }
                case R.id.img_less_qty: {
                    productQty(-1);
                    break;
                }
                case R.id.carview_add_to_cart: {


                    validateAndSubmitAddtoCart();
                    break;
                }
                case R.id.img_cart: {


                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentCart());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_add_to_whist_list: {


                    addToWishList(selectedProductModal.productId);
                    break;
                } case R.id.lnr_see_all_related_product: {


                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_RELATED_PRODUCTS);
                    bundle.putString("product_id", selectedProductModal.productId);
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

    private void addToWishList(String productId) {
        try {
            String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");
            if (access_token != null && !access_token.isEmpty()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("product_id", productId);
                storeService.productAddToWishList(ServiceNames.REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST, jsonObject);
            } else {
                Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                startActivityForResult(intent, 111);
                getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateAndSubmitAddtoCart() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", selectedProductModal.productId);
            jsonObject.put("guest_id", getFragmentActivity().deviceUID());
            int qty = Integer.valueOf(getFragmentActivity().getTxtFrom(txtQty));

            if (qty > 0) {
                jsonObject.put("quantity", qty);
                String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");

                if (access_token != null && !access_token.isEmpty()) {
                    storeService.addToCard(ServiceNames.REQUEST_ID_GET_ADD_TO_CART, jsonObject);
                } else {
                    storeService.guestAddToCard(ServiceNames.REQUEST_ID_GET_GUEST_ADD_TO_CART, jsonObject);
                }

            } else {
                toastInFragment("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void productQty(int action) {
        try {
            int qty = Integer.valueOf(getFragmentActivity().getTxtFrom(txtQty));
            if (action == 1) {
                qty += action;
            } else {
                if (qty != 0) {
                    qty += action;
                }
                if (qty == 0) {
                    qty = 1;
                }
            }
            txtQty.setText(qty + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_PRODUCT_DETAILS) {
                    AppLogger.log("here response session");

                    if (jsonObject != null) {
                        proccessProducts(jsonObject);
                        JSONArray images = jsonObject.has("gallery") ? jsonObject.getJSONArray("gallery") : null;
                        JSONArray relatedProduts = jsonObject.has("related_products") ? jsonObject.getJSONArray("related_products") : null;
                        if (images != null) {
                            processImages(images);
                        }
                        if (relatedProduts != null && relatedProduts.length() > 0) {
                            productListAdapter.updateData(proccessRelatedProduct(relatedProduts));
                        }
//                        productGridAdapter.updateData(proccessProducts(jsonArray));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_GUEST_ADD_TO_CART || serviceId == ServiceNames.REQUEST_ID_GET_ADD_TO_CART) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    getCart();
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_CART || serviceId == ServiceNames.REQUEST_ID_GET_GUST_CART) {
                    JSONArray jsonArray = jsonObject.has("item") ? jsonObject.getJSONArray("item") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        imageBadgeView.setBadgeValue(jsonArray.length());
                    } else {
                        imageBadgeView.setBadgeValue(0);
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

    private List<ProductModel> proccessRelatedProduct(JSONArray jsonArray) {
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

    private void processImages(JSONArray jsonArray) {
        List<HomeSliderModel> homeSliderModels = new ArrayList<>();
        try {

            if (jsonArray != null && jsonArray.length() > 0) {

                HomeSliderModel homeSliderModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    homeSliderModel = new HomeSliderModel();
                    homeSliderModel.imgUrl = AppUtils.getStringValueFromJson(jsonObject, "image");
                    homeSliderModels.add(homeSliderModel);
                }
                productDitailsSliderAdapter = new ProductDitailsSliderAdapter(getFragmentActivity(), homeSliderModels);
                viewPager.setAdapter(productDitailsSliderAdapter);
                productDetailsImagesListAdapter.updateData(homeSliderModels);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProductModel selectedProductModal;

    private void proccessProducts(JSONObject jsonObject) {

        try {
            if (jsonObject != null) {


                selectedProductModal = new ProductModel();
                selectedProductModal.productId = AppUtils.getStringValueFromJson(jsonObject, "id");
                selectedProductModal.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                selectedProductModal.price = AppUtils.getStringValueFromJson(jsonObject, "price");
                selectedProductModal.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                selectedProductModal.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                selectedProductModal.stock = AppUtils.getLongValueFromJson(jsonObject, "stock");
                selectedProductModal.description = AppUtils.getStringValueFromJson(jsonObject, "description");
                selectedProductModal.brand = AppUtils.getStringValueFromJson(jsonObject, "brand");
                selectedProductModal.wishlisted = AppUtils.getBooleanValueFromJson(jsonObject, "wishlisted");
                txtPrice.setText(selectedProductModal.currency + " " + selectedProductModal.price);
                txtProductName.setText(selectedProductModal.name);
                txtDiscription.setText(selectedProductModal.description);


                if (selectedProductModal.wishlisted) {
                    int tintColor = ContextCompat.getColor(getFragmentActivity(), R.color.red); // Replace with your desired tint color resource
                    imgFav.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);

                } else {
                    int tintColor = ContextCompat.getColor(getFragmentActivity(), R.color.white); // Replace with your desired tint color resource
                    imgFav.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void goback() {
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}