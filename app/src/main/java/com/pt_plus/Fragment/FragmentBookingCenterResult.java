package com.pt_plus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONObject;


public class FragmentBookingCenterResult extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_center_result, container, false);
        initView(view);
        return view;
    }

    private CenterService centerService;
    private ImageView imgTransaction, imgTrainer;
    private String bookingStatus;
    private TextView txtPaymentStatus, txtMessage, txtDate, txtTime, txtAddress,txtName;
private String  bookingId;
    private void initView(View view) {
        imgTrainer = view.findViewById(R.id.img_trainer);
        txtTime = view.findViewById(R.id.txt_time);
        imgTransaction = view.findViewById(R.id.img_transaction);
        txtPaymentStatus = view.findViewById(R.id.txt_payment_status);
        txtMessage = view.findViewById(R.id.txt_message);
        txtDate = view.findViewById(R.id.txt_date);
        txtAddress = view.findViewById(R.id.txt_address);
        txtName = view.findViewById(R.id.txt_name);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);

        centerService = new CenterService(getFragmentActivity(), _callBack);
        try {
            Bundle bundle = this.getArguments();
            bookingStatus = bundle.getString("status");
            bookingId = bundle.getString("bookingId");
            centerService.centerBookingDetails(ServiceNames.REQUEST_GET_CENTER_BOOKING_DETAILS, new JSONObject().put("id", bundle.getString("bookingId")));
            getFragmentActivity().showProgress(true);
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
                    bundle.putString("id", bookingId);
                    bundle.putString("from", "booking");
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentCenterBookingHistoryDetails fragment2 = new FragmentCenterBookingHistoryDetails();
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
                if (serviceId == ServiceNames.REQUEST_GET_CENTER_BOOKING_DETAILS) {
                    setDataToVview(jsonObject);
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

    private void setDataToVview(JSONObject jsonObject) {
        try {
            Glide.with(this).load(AppUtils.getStringValueFromJson(jsonObject, "center_image")).placeholder(R.drawable.no_image).into(imgTrainer);
//            txtName.setText(AppUtils.getStringValueFromJson(jsonObject, "trainer_name"));
//            txtSplec.setText(AppUtils.getStringValueFromJson(jsonObject, "trainer_category"));
//            txtStatus.setText(AppUtils.getStringValueFromJson(jsonObject, "booking_status"));
            txtAddress.setText(AppUtils.getStringValueFromJson(jsonObject, "center_address"));
            txtName.setText(AppUtils.getStringValueFromJson(jsonObject, "center_name"));
//            txtTotalPrice.setText("Total Price " + AppUtils.getStringValueFromJson(jsonObject, "plan_currency") + "  " + AppUtils.getStringValueFromJson(jsonObject, "plan_price"));
            txtDate.setText(AppDateUtils.getInstance().formateDate(AppUtils.getStringValueFromJson(jsonObject, "booked_on")));
            txtTime.setText(AppDateUtils.convertDateToOtherFormat(AppUtils.getStringValueFromJson(jsonObject, "booked_on"), AppCons.DATE_FORAMTE, "h:mm a"));

            if (bookingStatus.equalsIgnoreCase("success")) {
                imgTransaction.setBackgroundResource(R.drawable.ic_transaction_susscess);
                txtPaymentStatus.setText(R.string.success);
                txtMessage.setText(R.string.thank_you_for_choosing_our_services_and_trust_our_center_to_help_and_make_and_healthy);
            } else {
                txtPaymentStatus.setText(R.string.failed);
                txtPaymentStatus.setTextColor(getResources().getColor(R.color.red));
                txtMessage.setText(R.string.your_booking_has_been_failed_choosing_our_services);
                imgTransaction.setBackgroundResource(R.drawable.ic_transaction_failed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}