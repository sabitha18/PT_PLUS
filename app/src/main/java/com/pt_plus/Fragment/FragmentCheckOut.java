package com.pt_plus.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.applandeo.materialcalendarview.CalendarView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class FragmentCheckOut extends SuperFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle, txtDeliveryDate, txtShippingAddress;
    private View appbar;
    private CardView cardViewCheckOut;
    private Button btnShedule, btnAddNewAddress;
    private BottomSheetDialog bottomSheetDialog;
    private RadioButton radioKnet, radioVisa;
    private CheckBox chk24Delvery, chk4delivery;
    private Dialog dialognewAddress;
    private Chip chipHome, chipWork, chipOffice, chipOther;
    private CardView cardViewBottomSheetDone;


    private CalendarView calendarView;
    private OrderModel selectedOrderModal;
    String fromPage;
    private StoreService storeService;
    public TextView txtSubTotal, txtDeliveryCharge, txtTotal, txtTotalItems;

    private void initView(View view) {
        txtShippingAddress = view.findViewById(R.id.txt_shipping_Address);
        txtSubTotal = view.findViewById(R.id.txt_sub_total);
        txtDeliveryDate = view.findViewById(R.id.txt_delivery_date);
        txtTotalItems = view.findViewById(R.id.txt_total_items);
        txtTotal = view.findViewById(R.id.txt_total);
        txtDeliveryCharge = view.findViewById(R.id.txt_delivery_charge);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromPage = bundle.getString("type");
            if (fromPage.equalsIgnoreCase("buyNow")) {

            }
        }
        selectedOrderModal = LocalCache.getCache().getSelectedOrder();
        if (selectedOrderModal != null) {

            if (selectedOrderModal.orderDate != null) {
                txtDeliveryDate.setText(selectedOrderModal.orderDate);
            }
            if (selectedOrderModal.addressModel != null) {
                txtShippingAddress.setText(selectedOrderModal.addressModel.streetName + "," + selectedOrderModal.addressModel.Area + "," + selectedOrderModal.addressModel.block);
                ProceesDelveryCharge();
            }
        }


        chk24Delvery = view.findViewById(R.id.chk_24_delivery);
        chk24Delvery.setOnClickListener(_click);

        chk4delivery = view.findViewById(R.id.chk_4_delivery);
        chk4delivery.setOnClickListener(_click);

        btnAddNewAddress = view.findViewById(R.id.btn_add_new_address);
        btnAddNewAddress.setOnClickListener(_click);


        radioVisa = view.findViewById(R.id.radio_visa);
        radioVisa.setOnClickListener(_click);
        radioKnet = view.findViewById(R.id.radio_knet);
        radioKnet.setOnClickListener(_click);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        cardViewCheckOut = view.findViewById(R.id.carview_checkout);
        cardViewCheckOut.setOnClickListener(_click);

        btnShedule = view.findViewById(R.id.btn_shedule);
        btnShedule.setOnClickListener(_click);


        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.check_out);


        bottomSheetDialog = new BottomSheetDialog(getFragmentActivity(), R.style.CustomBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.layout_checkout_shedule_bottomsheet);
        TextView txtBottomSheetTitle = bottomSheetDialog.findViewById(R.id.txt_bottosheettite);
        txtBottomSheetTitle.setText("Choose Delivery Date");
        ImageView imgBottomSheetClose = bottomSheetDialog.findViewById(R.id.img_bottom_sheet_close);
        imgBottomSheetClose.setOnClickListener(_click);
        calendarView = bottomSheetDialog.findViewById(R.id.calendarView);
        cardViewBottomSheetDone = bottomSheetDialog.findViewById(R.id.bottom_sheet_carview_done);
        cardViewBottomSheetDone.setOnClickListener(_click);
        dialognewAddress = new Dialog(getFragmentActivity());
        dialognewAddress.setContentView(R.layout.layout_add_new_address_dilaog);
        Objects.requireNonNull(dialognewAddress.getWindow())
                .setLayout((int) (getFragmentActivity().getScreenWidth(getFragmentActivity()) * .95), ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialognewAddress.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ImageView imgNewAddresClose = dialognewAddress.findViewById(R.id.img_new_address_close);
        imgNewAddresClose.setOnClickListener(_click);

        chipWork = dialognewAddress.findViewById(R.id.chip_work);
        chipOffice = dialognewAddress.findViewById(R.id.chip_officer);
        chipOther = dialognewAddress.findViewById(R.id.chip_other);
        chipHome = dialognewAddress.findViewById(R.id.chip_home);
        chipHome.setOnClickListener(_click);
        chipOffice.setOnClickListener(_click);
        chipOther.setOnClickListener(_click);
        chipWork.setOnClickListener(_click);

        chk24Delvery.setChecked(true);
        storeService = new StoreService(getFragmentActivity(), callBack);

        processData();
    }

    private void processData() {
        try {
            if (selectedOrderModal.productModelList != null && selectedOrderModal.productModelList.size() > 0) {
                double total = 0;
                for (ProductModel productModel : selectedOrderModal.productModelList) {
                    total += productModel.qty * Double.valueOf(productModel.price);
                }
                txtSubTotal.setText("KWD " + total);
                txtTotal.setText("KWD " + total);
                txtTotalItems.setText("( " + selectedOrderModal.productModelList.size() + " items )");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_SINGLE_PRODUCT_DELIVERY_CHARGE || serviceId == ServiceNames.REQUEST_ID_GET_GUST_DELIVERY_CHARGE || serviceId == ServiceNames.REQUEST_ID_GET_DELIVERY_CHARGE_SINGLE_PRODUCT || serviceId == ServiceNames.REQUEST_ID_GET_DELIVERY_CHARGE) {
                    JSONObject jsonObject1 = jsonObject.has("data") ? jsonObject.getJSONObject("data") : null;
                    if (jsonObject1 != null) {
                        setTotal(jsonObject1);
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_GUST_SINGLE_PRODUCT_CHECKOUT || serviceId == ServiceNames.REQUEST_ID_GET_CHECKOUT || serviceId == ServiceNames.REQUEST_ID_GET_SINGLE_PRODUCT_CHECKOUT || serviceId == ServiceNames.REQUEST_ID_GET_STORE_CHECKOUT) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));

                    JSONObject paymentUrl = jsonObject.has("payment_url")?jsonObject.getJSONObject("payment_url"):null;
                    if(paymentUrl != null){
                        JSONObject data = jsonObject.has("data") ? jsonObject.getJSONObject("data") : null;
                        Bundle bundle = new Bundle();
                        bundle.putString("paymentUrl", AppUtils.getStringValueFromJson(paymentUrl, "paymentURL"));
                        bundle.putString("from", "cart");
                        bundle.putString("orderId", AppUtils.getStringValueFromJson(data,"order_id"));
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentPaymentWebView fragment2 = new FragmentPaymentWebView();
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
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private void setTotal(JSONObject jsonObject) {
        try {
            txtTotal.setText(AppUtils.getStringValueFromJson(jsonObject, "currency") + " " + AppUtils.getDoubleValueFromJson(jsonObject, "total"));
            txtSubTotal.setText(AppUtils.getStringValueFromJson(jsonObject, "currency") + " " + AppUtils.getDoubleValueFromJson(jsonObject, "sub_total"));
            txtDeliveryCharge.setText(AppUtils.getStringValueFromJson(jsonObject, "currency") + " " + AppUtils.getDoubleValueFromJson(jsonObject, "delivery_charge"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void radioSetChecked(Chip view) {
        chipHome.setChecked(false);
        chipOffice.setChecked(false);
        chipOther.setChecked(false);
        chipWork.setChecked(false);
        view.setChecked(true);
    }

    private void radioSetChecked(RadioButton view) {
        radioVisa.setChecked(false);
        radioKnet.setChecked(false);
        view.setChecked(true);
    }

    private void checkBoxSetChecked(CheckBox view) {
        chk4delivery.setChecked(false);
        chk24Delvery.setChecked(false);
        view.setChecked(true);


    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chip_work: {
                    radioSetChecked(chipWork);
                    break;
                }
                case R.id.chip_officer: {
                    radioSetChecked(chipOffice);
                    break;
                }
                case R.id.chip_other: {
                    radioSetChecked(chipOther);
                    break;
                }
                case R.id.chip_home: {
                    radioSetChecked(chipHome);
                    break;
                }
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.btn_shedule: {
                    bottomSheetDialog.show();
                    break;
                }
                case R.id.btn_add_new_address: {
//                    dialognewAddress.show();

                    if (getFragmentActivity().isUserLoged()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("FROM", "CHECKOUT");
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentSavedAddresses fragment2 = new FragmentSavedAddresses();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("FROM", "CHECKOUT");
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentAddAddress fragment2 = new FragmentAddAddress();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }


                    break;
                }
                case R.id.radio_knet: {
                    radioSetChecked(radioKnet);
                    break;
                }
                case R.id.radio_visa: {
                    radioSetChecked(radioVisa);
                    break;
                }
                case R.id.chk_24_delivery: {
                    checkBoxSetChecked(chk24Delvery);
                    break;
                }
                case R.id.chk_4_delivery: {
                    checkBoxSetChecked(chk4delivery);
                    break;
                }
                case R.id.img_bottom_sheet_close: {
                    bottomSheetDialog.dismiss();
                    break;
                }
                case R.id.img_new_address_close: {
                    dialognewAddress.dismiss();
                    break;
                }
                case R.id.carview_checkout: {
//                    Intent intent = new Intent(getFragmentActivity(), ActivityTransactionResult.class);
//                    startActivityForResult(intent, 111);
//                    getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
//
                    processCheckOut();

                    break;
                }
                case R.id.bottom_sheet_carview_done: {
                    Calendar selectedDate = calendarView.getFirstSelectedDate();
                    Date date = selectedDate.getTime();
                    txtDeliveryDate.setText(AppDateUtils.getInstance().convertDateToStr(date, "MMM dd YYYY"));
                    selectedOrderModal.orderDate = AppDateUtils.getInstance().convertDateToStr(date, "MMM dd YYYY");
                    bottomSheetDialog.dismiss();
                    if (selectedOrderModal.addressModel != null) {
                        ProceesDelveryCharge();
                    }
                    break;
                }

            }
        }
    };

    private void processCheckOut() {
        if (getFragmentActivity().isUserLoged()) {
            if (selectedOrderModal.isSingleProductOrder) {
                SingleProductCheckOut();
            } else {
                checkout();
            }
        } else {
            if (selectedOrderModal.isSingleProductOrder) {
                gustSingleProductCheckOut();
            } else {
                gustCheckout();
            }

        }
    }

    private void checkout() {
        try {
            JSONObject jsonObject = new JSONObject();
AppLogger.log("dadsfasdfdsafads         "+selectedOrderModal.orderDate);
            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }


            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_address_id", selectedOrderModal.addressModel.id);
            }
            storeService.storeCheckOut(ServiceNames.REQUEST_ID_GET_STORE_CHECKOUT, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gustCheckout() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guest_id", getFragmentActivity().deviceUID());
            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }


            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_name", selectedOrderModal.addressModel.fullName);
                jsonObject.put("shipping_email", selectedOrderModal.addressModel.email);
                jsonObject.put("shipping_phone_code", selectedOrderModal.addressModel.mobilePrefix);
                jsonObject.put("shipping_phone", selectedOrderModal.addressModel.mobile);
                jsonObject.put("shipping_country", selectedOrderModal.addressModel.country);
                jsonObject.put("shipping_state", selectedOrderModal.addressModel.Area);
                jsonObject.put("shipping_pincode", selectedOrderModal.addressModel.zipCOde);
                jsonObject.put("shipping_street", selectedOrderModal.addressModel.streetName);
                jsonObject.put("shipping_area", selectedOrderModal.addressModel.Area);
                jsonObject.put("shipping_block", selectedOrderModal.addressModel.block);
                jsonObject.put("shipping_building", selectedOrderModal.addressModel.block);
                jsonObject.put("shipping_type", selectedOrderModal.addressModel.AddressType);
                jsonObject.put("shipping_gender", selectedOrderModal.addressModel.gender);
            }
            storeService.gustCheckout(ServiceNames.REQUEST_ID_GET_CHECKOUT, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gustSingleProductCheckOut() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guest_id", getFragmentActivity().deviceUID());

            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }

            if (selectedOrderModal.productModelList != null) {
                jsonObject.put("product_id", selectedOrderModal.productModelList.get(0).productId);
                jsonObject.put("quantity", "1");
            }
            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_name", selectedOrderModal.addressModel.fullName);
                jsonObject.put("shipping_email", selectedOrderModal.addressModel.email);
                jsonObject.put("shipping_phone_code", selectedOrderModal.addressModel.mobilePrefix);
                jsonObject.put("shipping_phone", selectedOrderModal.addressModel.mobile);
                jsonObject.put("shipping_country", selectedOrderModal.addressModel.country);
                jsonObject.put("shipping_state", selectedOrderModal.addressModel.Area);
                jsonObject.put("shipping_pincode", selectedOrderModal.addressModel.zipCOde);
                jsonObject.put("shipping_street", selectedOrderModal.addressModel.streetName);
                jsonObject.put("shipping_area", selectedOrderModal.addressModel.Area);
                jsonObject.put("shipping_block", selectedOrderModal.addressModel.block);
                jsonObject.put("shipping_building", selectedOrderModal.addressModel.block);
                jsonObject.put("shipping_type", selectedOrderModal.addressModel.AddressType);
                jsonObject.put("shipping_gender", selectedOrderModal.addressModel.gender);
            }
            storeService.gustSingleProductCheckOUt(ServiceNames.REQUEST_ID_GET_GUST_SINGLE_PRODUCT_CHECKOUT, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SingleProductCheckOut() {
        try {
            JSONObject jsonObject = new JSONObject();

            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }

            if (selectedOrderModal.productModelList != null) {
                jsonObject.put("product_id", selectedOrderModal.productModelList.get(0).productId);
                jsonObject.put("quantity", "1");
            }
            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_address_id", selectedOrderModal.addressModel.id);

            }
            storeService.singleProductCheckout(ServiceNames.REQUEST_ID_GET_SINGLE_PRODUCT_CHECKOUT, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void goback() {
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

    private void ProceesDelveryCharge() {
        if (selectedOrderModal != null && selectedOrderModal.addressModel != null) {
            if (getFragmentActivity().isUserLoged()) {
                if (selectedOrderModal.isSingleProductOrder) {
                    getDeliveryChargeSingleProduct();
                } else {
                    deliveryCharge();
                }
            } else {
                if (selectedOrderModal.isSingleProductOrder) {
                    gustSingleProductDeliveryCharge();
                } else {
                    gustDeliveryCharge();
                }

            }
        }

    }

    private void gustDeliveryCharge() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guest_id", getFragmentActivity().deviceUID());
            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }


            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_pincode", selectedOrderModal.addressModel.zipCOde);
            }

            storeService.gustDeliveryCharge(ServiceNames.REQUEST_ID_GET_SINGLE_PRODUCT_DELIVERY_CHARGE, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deliveryCharge() {
        try {
            JSONObject jsonObject = new JSONObject();

            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }

            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_address_id", selectedOrderModal.addressModel.id);
            }


            storeService.getDeliveryCharge(ServiceNames.REQUEST_ID_GET_DELIVERY_CHARGE, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gustSingleProductDeliveryCharge() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guest_id", getFragmentActivity().deviceUID());
            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }

            if (selectedOrderModal.productModelList != null) {
                jsonObject.put("product_id", selectedOrderModal.productModelList.get(0).productId);
                jsonObject.put("quantity", "1");
            }
            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_pincode", selectedOrderModal.addressModel.zipCOde);
            }

            storeService.gustSingleProductDeliveryCharge(ServiceNames.REQUEST_ID_GET_SINGLE_PRODUCT_DELIVERY_CHARGE, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDeliveryChargeSingleProduct() {

        try {
            JSONObject jsonObject = new JSONObject();

            Date date = calendarView.getSelectedDate().getTime();


            jsonObject.put("delivery_date", AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
//            jsonObject.put("delivery_date", AppDateUtils.convertDateToOtherFormat(selectedOrderModal.orderDate, "MMM dd YYYY", "YYY-MM-DD"));
            if (chk4delivery.isChecked()) {
                jsonObject.put("delivery_type", "express");
            }
            if (chk24Delvery.isChecked()) {
                jsonObject.put("delivery_type", "normal");
            }
            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_address_id", selectedOrderModal.addressModel.id);
            }
            if (selectedOrderModal.productModelList != null) {
                jsonObject.put("product_id", selectedOrderModal.productModelList.get(0).productId);
                jsonObject.put("quantity", "1");
            }
            if (selectedOrderModal.addressModel != null) {
                jsonObject.put("shipping_pincode", selectedOrderModal.addressModel.zipCOde);
            }

            storeService.getDeliveryChargeSingleProduct(ServiceNames.REQUEST_ID_GET_DELIVERY_CHARGE_SINGLE_PRODUCT, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}