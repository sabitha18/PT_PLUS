package com.pt_plus.Fragment;

import android.annotation.SuppressLint;
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

import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentCheckoutResult extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_result, container, false);
        initview(view);
        return view;
    }

    private StoreService storeService;
    private LinearLayout lnrMain, lnrProducts;
    private TextView txtDate, txtTime, txtAmount, txtOrderaId, txtMessage,txtPaymnetStatus;
    private ImageView imgTransaction;
String orderId;
    private void initview(View view) {
        txtPaymnetStatus = view.findViewById(R.id.txt_payment_status);
        lnrProducts = view.findViewById(R.id.lnr_products);
        lnrMain = view.findViewById(R.id.lnr_main);
        txtDate = view.findViewById(R.id.txt_date);
        txtTime = view.findViewById(R.id.txt_time);
        txtAmount = view.findViewById(R.id.txt_amount);
        imgTransaction = view.findViewById(R.id.img_transaction);
        txtMessage = view.findViewById(R.id.txt_message);
        txtOrderaId = view.findViewById(R.id.txt_order_id);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        lnrMain.setVisibility(View.GONE);
        storeService = new StoreService(getFragmentActivity(), _callBack);
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                orderId = bundle.getString("orderId");
                storeService.getOrderTransactionDeatils(ServiceNames.REQUEST_GET_ORDER_TRANSACTION_DETAILS, new JSONObject().put("order_id", bundle.getString("orderId")));
                getFragmentActivity().showProgress(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnDetails = view.findViewById(R.id.btn_details);
        btnDetails.setOnClickListener(_click);
    }
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_details: {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", orderId);
                    bundle.putString("from", "order");
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentOrderDetails fragment2 = new FragmentOrderDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }



            }
        }
    };
    private Button btnDetails;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_GET_ORDER_TRANSACTION_DETAILS) {
                    setDataToview(jsonObject);
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

    private void setDataToview(JSONObject jsonObject) {
        try {
            lnrMain.setVisibility(View.VISIBLE);
            txtDate.setText(AppDateUtils.getInstance().formateDate(AppUtils.getStringValueFromJson(jsonObject, "transaction_date")));
            txtTime.setText(AppDateUtils.convertDateToOtherFormat(AppUtils.getStringValueFromJson(jsonObject, "transaction_date"), AppCons.DATE_FORAMTE, "h:mm a"));
            txtAmount.setText(AppUtils.getStringValueFromJson(jsonObject, "currency") + " " + AppUtils.getStringValueFromJson(jsonObject, "total_amount"));
            txtOrderaId.setText(AppUtils.getStringValueFromJson(jsonObject, "order_id"));
            if (AppUtils.getBooleanValueFromJson(jsonObject, "payment_status")) {
                imgTransaction.setBackgroundResource(R.drawable.ic_transaction_susscess);
                txtPaymnetStatus.setText(R.string.payment_successful);
                txtMessage.setText(R.string.you_have_successfully_placed_your_order_a_confirmation_message_has_been_sent_to_you_via_e_mail);
            } else {
                txtPaymnetStatus.setText(R.string.payment_failed);
                txtPaymnetStatus.setTextColor(getResources().getColor(R.color.red));
                txtMessage.setText(R.string.your_payment_has_been_failed_you_have_entered_wrong_security_code);
                imgTransaction.setBackgroundResource(R.drawable.ic_transaction_failed);
            }
            LayoutInflater inflater = LayoutInflater.from(getFragmentActivity()); // 'this' refers to the current Activity or Context
            JSONArray products = jsonObject.has("products") ? jsonObject.getJSONArray("products") : null;


            for (int i = 0; i < products.length(); i++) {

                View childLayout = inflater.inflate(R.layout.layout_order_result_products, lnrProducts, false);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView txtProduct = childLayout.findViewById(R.id.txt_product);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView txt_qty = childLayout.findViewById(R.id.txt_qty);
                JSONObject product = products.getJSONObject(i);
                txtProduct.setText(AppUtils.getStringValueFromJson(product, "name"));
                txt_qty.setText(R.string.qty+"" + AppUtils.getStringValueFromJson(product, "quantity"));
                lnrProducts.addView(childLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}