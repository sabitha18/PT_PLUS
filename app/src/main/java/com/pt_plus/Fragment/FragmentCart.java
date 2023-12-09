package com.pt_plus.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import com.pt_plus.Adapter.CartListAdapter;
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
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentCart extends SuperFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private CardView cardViewCheckOut;
    private StoreService storeService;
    CartListAdapter cartListAdapter;
    private TextView txtSubtotal, txtDeliveryCharge, txtTotal, txtTotalQty;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        txtSubtotal = view.findViewById(R.id.txt_subtotal);
        txtDeliveryCharge = view.findViewById(R.id.txt_delivery_charge);
        txtTotal = view.findViewById(R.id.txt_total);
        txtTotalQty = view.findViewById(R.id.txt_total_qty);

        cardViewCheckOut = view.findViewById(R.id.carview_checkout);
        cardViewCheckOut.setOnClickListener(_click);


        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.cart);

        RecyclerView recyclerViewCart = view.findViewById(R.id.rcy_cart);
        cartListAdapter = new CartListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCart.setAdapter(cartListAdapter);


        storeService = new StoreService(getFragmentActivity(), callBack);

        String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");
        if (access_token != null && !access_token.isEmpty()) {
            storeService.getCart(ServiceNames.REQUEST_ID_GET_CART);
        } else {
            storeService.getGustCart(ServiceNames.REQUEST_ID_GET_GUST_CART, getFragmentActivity().deviceUID());
        }


        getFragmentActivity().showProgress(true);
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_GUST_CART || serviceId == ServiceNames.REQUEST_ID_GET_CART ) {
                    JSONArray jsonArray = jsonObject.has("item") ? jsonObject.getJSONArray("item") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        cartListAdapter.updateData(proccessProducts(jsonArray));
                    }else {
                        cartListAdapter.updateData(null);
                    }
                    JSONObject orderInfo = jsonObject.has("order_info") ? jsonObject.getJSONObject("order_info") : null;

                    if (orderInfo != null) {

                        txtSubtotal.setText(AppUtils.getStringValueFromJson(orderInfo, "currency") + " " + AppUtils.getDoubleValueFromJson(orderInfo, "sub_total"));
                        txtDeliveryCharge.setText(AppUtils.getStringValueFromJson(orderInfo, "currency") + " " + AppUtils.getDoubleValueFromJson(orderInfo, "delivery_charge"));
                        txtTotal.setText(AppUtils.getStringValueFromJson(orderInfo, "currency") + " " + AppUtils.getDoubleValueFromJson(orderInfo, "total"));

                    }else {
                        txtSubtotal.setText("00.00");
                        txtDeliveryCharge.setText("00.00");
                        txtTotal.setText("00.00");

                    }

                    getFragmentActivity().showProgress(false);
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_GUST_CART_UPDATE || serviceId ==ServiceNames.REQUEST_ID_GET_CART_UPDATE) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");
                    if (access_token != null && !access_token.isEmpty()) {
                        storeService.getCart(ServiceNames.REQUEST_ID_GET_CART);
                    } else {
                        storeService.getGustCart(ServiceNames.REQUEST_ID_GET_GUST_CART, getFragmentActivity().deviceUID());
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

    private List<ProductModel> proccessProducts(JSONArray jsonArray) {
        List<ProductModel> productModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                ProductModel productModel = null;
                int qty = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    productModel = new ProductModel();
                    productModel.productId = AppUtils.getStringValueFromJson(jsonObject, "product_id");
                    productModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                    productModel.price = AppUtils.getStringValueFromJson(jsonObject, "unit_price");
                    productModel.name = AppUtils.getStringValueFromJson(jsonObject, "product_name");
                    productModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    productModel.cartId = AppUtils.getStringValueFromJson(jsonObject, "id");
                    productModel.qty = Math.round(Double.valueOf(AppUtils.getStringValueFromJson(jsonObject, "quantity")));
                    productModelList.add(productModel);
                    qty += productModel.qty;
                }
                txtTotalQty.setText("( " + qty + " items )");
            }
            return productModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModelList;
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.carview_checkout: {
                    OrderModel orderModel = new OrderModel();
                    orderModel.productModelList =cartListAdapter.getAllItems();
                    LocalCache.getCache().setSelectedOrder(orderModel);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentCheckOut());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // In fragment class callback

    }

    private void goback() {
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {
                ProductModel productModel = null;
                try {
                    productModel = (ProductModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productModel != null) {
                    productModel.qty += 1;
                    gustCartUpdate(productModel);
                } else {
////                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }else if (message.what == 2) {
                ProductModel productModel = null;
                try {
                    productModel = (ProductModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productModel != null) {
                    productModel.qty -= 1;
                    gustCartUpdate(productModel);
                } else {
////                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }else if (message.what == 3) {
                ProductModel productModel = null;
                try {
                    productModel = (ProductModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productModel != null) {
                    productModel.qty = 0;
                    gustCartUpdate(productModel);
                } else {
////                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });

    private void gustCartUpdate(ProductModel productModel) {
        try {
            if (productModel != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("guest_id", getFragmentActivity().deviceUID());
                jsonObject.put("cart_id", productModel.cartId);
                jsonObject.put("quantity", productModel.qty);


                String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");

                if (access_token != null && !access_token.isEmpty()) {
                    storeService.cartUpdate(ServiceNames.REQUEST_ID_GET_CART_UPDATE,jsonObject);
                } else {
                    storeService.gustCartUpdate(ServiceNames.REQUEST_ID_GET_GUST_CART_UPDATE, jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}