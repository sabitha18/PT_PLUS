package com.pt_plus.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.UiUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONObject;


public class FragmentOrderDetails extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private StoreService storeService;
    private ImageView imgProduct;
    private LinearLayout lnrMinaView;
    private Button btnCancelOrder;
    String orderId = null, from="";
    private TextView txtProductName, txtQty, txtPrice, txtDate, txtOrderID, txtOrderDate, txtOrderTime, txtOrderAddress;

    private void initView(View view) {
        btnCancelOrder = view.findViewById(R.id.btn_cancel_order);
        btnCancelOrder.setOnClickListener(_click);
        lnrMinaView = view.findViewById(R.id.lnr_main_view);
        lnrMinaView.setVisibility(View.GONE);
        txtOrderAddress = view.findViewById(R.id.txt_order_address);
        txtOrderTime = view.findViewById(R.id.txt_order_time);
        txtOrderDate = view.findViewById(R.id.txt_order_date);
        txtOrderID = view.findViewById(R.id.txt_order_id);
        txtDate = view.findViewById(R.id.txt_date);
        txtPrice = view.findViewById(R.id.txt_price);
        txtQty = view.findViewById(R.id.txt_qty);
        txtProductName = view.findViewById(R.id.txt_product_name);
        imgProduct = view.findViewById(R.id.img_product);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText("");

        storeService = new StoreService(getFragmentActivity(), _callback);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderId = bundle.getString("id");
            from = bundle.getString("from");
            if (orderId != null) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", orderId);

                    storeService.getOrderDetails(ServiceNames.REQUEST_ID_GET_ORDER_DETAILS, jsonObject);
                    getFragmentActivity().showProgress(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private final CallBack _callback = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_ORDER_DETAILS) {
                    Glide.with(getFragmentActivity())
                            .load(AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img"))
                            .into(imgProduct);
                    if (AppUtils.getBooleanValueFromJson(jsonObject, "cancellation")) {
                        btnCancelOrder.setVisibility(View.GONE);
                    }
                    txtProductName.setText(AppUtils.getStringValueFromJson(jsonObject, "product_name"));
                    txtProductName.setText(AppUtils.getStringValueFromJson(jsonObject, "product_name"));

                    txtDate.setText(AppDateUtils.getInstance().formateDate(AppUtils.getStringValueFromJson(jsonObject, "ordered_on")));
                    txtOrderDate.setText(AppDateUtils.getInstance().formateDate(AppUtils.getStringValueFromJson(jsonObject, "ordered_on")));
                    txtOrderTime.setText(AppDateUtils.convertDateToOtherFormat(AppUtils.getStringValueFromJson(jsonObject, "ordered_on"), AppCons.DATE_FORAMTE, "h:mm a"));

                    txtOrderID.setText("Order# " + AppUtils.getStringValueFromJson(jsonObject, "order_number"));
                    txtPrice.setText(AppUtils.getStringValueFromJson(jsonObject, "currency") + " " + AppUtils.getStringValueFromJson(jsonObject, "amount"));

                    jsonObject = jsonObject.has("shipping_address") ? jsonObject.getJSONObject("shipping_address") : null;
                    txtOrderAddress.setText(
                            AppUtils.getStringValueFromJson(jsonObject, "name") + " , " +
                                    AppUtils.getStringValueFromJson(jsonObject, "building") + " , " +
                                    AppUtils.getStringValueFromJson(jsonObject, "block") + " , " +
                                    AppUtils.getStringValueFromJson(jsonObject, "area") + " , " +
                                    AppUtils.getStringValueFromJson(jsonObject, "street") + " , " +
                                    AppUtils.getStringValueFromJson(jsonObject, "state") + " , " +
                                    AppUtils.getStringValueFromJson(jsonObject, "country") + " , " +
                                    AppUtils.getStringValueFromJson(jsonObject, "pincode")
                    );
                    lnrMinaView.setVisibility(View.VISIBLE);

                } else if (serviceId == ServiceNames.REQUEST_ID_CANCEL_ORDER) {
                    AppLogger.log("4werqwrwerwerfdsfsfgsfg");
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    JSONObject input = new JSONObject();
                    input.put("id", orderId);
                    storeService.getOrderDetails(ServiceNames.REQUEST_ID_GET_ORDER_DETAILS, input);
                    getFragmentActivity().showProgress(true);

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

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.btn_cancel_order: {
                    UiUtils.getInstance().showConfirm(11, "Are you sure you want to Cancel this Order ?", new PopUpCallBack() {
                        @Override
                        public void onOK() {
                            try {
                                cancelOrder();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancel() {

                        }
                    }, getFragmentActivity());

                    break;
                }


            }
        }
    };

    private void cancelOrder() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", orderId);
            storeService.cancelOrder(ServiceNames.REQUEST_ID_CANCEL_ORDER, jsonObject);
            getFragmentActivity().showProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goback() {
        if (from != null&&from.equalsIgnoreCase("order")) {
            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lnr_content, new FragmentOrderHistory());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            getFragmentActivity().getSupportFragmentManager().popBackStack();
        }

    }
}